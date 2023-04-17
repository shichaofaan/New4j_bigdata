package demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.Entity.Mysql.User;
import demo.Mapper.UserMapper;
import demo.service.Userservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Slf4j
@Service
public class Userserviceimpl extends ServiceImpl<UserMapper, User> implements Userservice {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Userserviceimpl Userserviceimpl;

    /**
     * 当插入数据时候用email做redis缓存
     * 插入只能用put缓存
     * @param user
     * @return
     */
    @CachePut(value = {"useremil"},key = "#user.userEmil")
    @Override
    public boolean insertUser(User user) {

        userMapper.insert(user);

        return false;
    }

    /**
     * 利用邮箱将账号读取出来，然后进行修改密码
     * 修改密码只能用put缓存
     * @param email
     * @param oldPasswd
     * @param newPasswd
     * @return
     */
    @CachePut(value = {"useremil"},key = "#email")
    @Override
    public boolean userModifyPasswd(String email, String oldPasswd, String newPasswd) {

        User user=Userserviceimpl.SelectUserbyemail(email);

        user.setUserPassword(newPasswd);

        int i = userMapper.updateById(user);

        if(i!=0){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 利用wrapper读取user里面的number
     * 读取缓存
     * result防止缓存穿透 unless = "#result==null"(结果为null的时候才缓存)
     * @param user_number
     * @return
     */
    @Cacheable(value = {"usernumber"},key = "#user_number")
    @Override
    public User SelectUserbynumber(String user_number) {

        QueryWrapper<User> wrapper=new QueryWrapper<>();

        wrapper.eq("user_number",user_number);

        User user = userMapper.selectOne(wrapper);

        log.info("信息"+user);

        return user;
    }

    /**
     * 利用wrapper读取user里面的number和emil
     * Cacheable:如果缓存里面有就直接取出来即可，加快速度
     * result缓存穿透 unless = "#result==null"(结果为null的时候才缓存)
     * @param user_email
     * @return
     */
    @Cacheable(value = {"useremil"},key = "#user_email")
    @Override
    public User SelectUserbyemail(String user_email) {

        QueryWrapper<User> wrapper=new QueryWrapper<>();

        //封装到hashmap里面
        HashMap map=new HashMap<>();

        map.put("user_emil",user_email);

        wrapper.allEq(map);

        User user = userMapper.selectOne(wrapper);

        log.info("{}",user);

        return user;
    }

    /**
     * 修改用户类
     * @param user
     * @return
     */
    @CachePut(value = {"useremil"},key = "#user.userEmil")
    @Override
    public boolean UpdateUserbyemail(User user) {

        QueryWrapper<User> wrapper=new QueryWrapper<>();

        //封装到hashmap里面
        HashMap map=new HashMap<>();

        map.put("user_emil",user.getUserEmil());

        wrapper.allEq(map);

        int update = userMapper.update(user, wrapper);

        /*
        * 如果不为null则插入成功
        * */
        if(update!=0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 不做缓存只是用来查询当前是否有这个邮箱
     * @param user_email
     * @return
     */
    @Override
    public boolean SelectUserbyemailbynamenocache(String user_email,String user_name) {
        QueryWrapper<User> wrapper=new QueryWrapper<>();

        //封装到hashmap里面
        HashMap map_email=new HashMap<>();

        map_email.put("user_emil",user_email);

        wrapper.allEq(map_email);

        User user1 = userMapper.selectOne(wrapper);

        HashMap map_name=new HashMap<>();

        map_name.put("user_name",user_name);

        wrapper.allEq(map_name);

        User user2 = userMapper.selectOne(wrapper);

        log.info("user_email:{}   ",user1+"user_name:{}:",user2);

        //如果任意一个有的话说明这个账号不能注册
        if(user1!=null||user2!=null){
            return false;
        }else {
            return true;
        }
    }

    @Cacheable(value = {"userID"},key = "#userid")
    @Override
    public User SelectUserbyID(String userid) {

        User user = userMapper.selectById(userid);

        if(user!=null){
            return user;
        }else {
            return null;
        }

    }
}
