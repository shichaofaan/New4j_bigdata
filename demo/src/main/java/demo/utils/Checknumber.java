package demo.utils;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import demo.Entity.Mysql.User;
import demo.Enum.CheckUserEnum;
import demo.service.Userservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class Checknumber {

    @Resource
    private Userservice userservice;

    @Resource
    private RedisUtil redisUtil;

    public CheckUserEnum checklogin(String login_Usernumber,String login_Password){

        //账号在5-17位置之间
        if(login_Usernumber.length()<5||login_Usernumber.length()>17){
            //账号输入错误   错误代码1
            return CheckUserEnum.NumberError;
        }

        if(login_Password.length()!=6){
            //密码输入错误 错误代码2
            return CheckUserEnum.KeyError;
        }

        /*
         *去数据库提取user(已经做了springcache缓存);
         * */
        User user = userservice.SelectUserbynumber(login_Usernumber);

        if(user==null){
            //账号异常 3错误
            return CheckUserEnum.NumberException;
        }

        if(user.getIsdelete()==1){
            //账号被禁用了 4错误
            return CheckUserEnum.NumberDisabled;
        }

        String user_password_sql=user.getUserPassword();

        Long userId = user.getUserId();

        if(login_Password.equals(user_password_sql)){
            log.info("输入密码"+login_Password+"实际密码:"+user_password_sql);
            //如果成功，将登录信息加入sa-token进行检测
            StpUtil.login(userId);
            log.info("已经将信息存入Sa-token了");
        }else{
            //密码输入错误 错误代码2
            return CheckUserEnum.KeyError;
        }
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        String loginId =(String) tokenInfo.getLoginId();

        //将用户id存入session里面
        StpUtil.login(user.getUserId());

        log.info("已经将用户信息和token的信息存入Redis里面了,这是用户id:"+loginId);

        //正确响应 返回0
        return CheckUserEnum.Right;
    }

    public CheckUserEnum checkregisterpage(String password,String email,String name,String email_password){

        String[] number = email.split("@");

        String userNumber = number[0];

        //账号在5-17位置之间
        if(email.length()<11||email.length()>24){
            //邮箱输入错误
            return CheckUserEnum.EmailTextError;
        }

        if(password.length()!=6){
            //密码输入错误 错误代码2
            return CheckUserEnum.KeyError;
        }

        //如果已经存在则无法注册，目的为了之后的登录不会报异常
        boolean flag = userservice.SelectUserbyemailbynamenocache(email,name);

        if(!flag){
            //已经存在这个账号了
            return CheckUserEnum.NumberExist;
        }

        //取出邮箱验证码
        String session_key= redisUtil.get(email).toString();

        if(session_key==null||email_password==null){
            //邮箱验证码错误
            return CheckUserEnum.EmailError;
        }

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(email_password.equals(session_key)){
            /**
             * 这里面存入数据库即可
             */
            User user = new User();
            user.setUserNumber(userNumber)
                    .setUserPassword(password)
                    .setUserName(name)
                    .setUserEmil(email);

            //插入数据库
            userservice.insertUser(user);

            return CheckUserEnum.Right;
        }

        //如果没有插入则是数据库连接异常,统一归到系统异常
        return CheckUserEnum.SystemException;
    }

}
