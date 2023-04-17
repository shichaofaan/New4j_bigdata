package demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import demo.Entity.Mysql.User;
import org.springframework.stereotype.Service;

@Service
public interface Userservice extends IService<User> {

    //注册用户
    boolean insertUser(User user);

    //修改密码
    boolean userModifyPasswd(String email,String oldPasswd, String newPasswd);

    //查询用户
    User SelectUserbynumber(String user_number);

    User SelectUserbyemail(String user_email);

    boolean UpdateUserbyemail(User user);

    boolean SelectUserbyemailbynamenocache(String user_email,String user_name);

    User SelectUserbyID(String userid);
}
