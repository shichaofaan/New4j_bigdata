package demo.Controller;


import demo.common.R;
import demo.utils.EmailUtils;
import demo.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/email")
@Slf4j
@Api(tags = "email")
@Component
public class emailController {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private EmailUtils emailUtils;

    @Value("${email.number}")
    public  String qq_email;
    @Value("${email.key}")
    public  String qq_emil_key;

    public static String title="项目验证码";

    //300为5分钟有效
    public static Integer time=300;


    /**
     * 发送qq邮箱
     * @param email
     * @return
     */
    @ApiOperation("发送qq邮箱的方法")
    @PostMapping ("/sendEmail")
    public R sendMsg(@RequestParam("email") String email){


        //获取邮箱号
        String phone = email;

        if (!checkNum(email)) {
            return R.error("邮箱格式不对");
        }

        if(StringUtils.isNotEmpty(phone)){
            //qq邮箱生成随机数
            String code= emailUtils.random1();

            log.info("code={}",code);


            try {
                emailUtils.sendMail(qq_email, qq_emil_key, phone, title, "<html><h1>本次项目的验证码:"+code+"(五分钟内有效)"+"</h1></html>");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

            //需要将生成的验证码保存到Redis
            redisUtil.set(email,code,time);
            return R.ok("邮箱发送成功");
        }
        return R.error("邮箱发送失败,仔细检查邮箱格式问题!!!!");
    }

    /**
     * 正则校验邮箱是否正确
     * @param mailname
     * @return
     */
    private static boolean checkNum(String mailname ) {
        // 包含所有邮箱
        String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mailname);
        return matcher.matches();
    }

}
