package demo.Controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import demo.Enum.CheckUserEnum;
import demo.common.R;
import demo.utils.Checknumber;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/login")
@Slf4j
@Component
@Api(tags = "login")
public class loginController {

    @Resource
    private Checknumber checknumber;

    /**
     * 账号登录
     * 账号密码检测
     * @param login_Usernumber
     * @param login_Password
     * @return
     */
    @ApiOperation(value = "账号登录页面",httpMethod = "POST")
    @PostMapping( "/login")
    public R login_001(@RequestParam("login_Usernumber") String login_Usernumber, @RequestParam("login_Password") String login_Password){

        log.info("账号: "+login_Usernumber+" 长度{}",login_Usernumber.length()+"---"+" 密码:"+login_Password+" 长度{}",login_Password.length());

        //调用方法进行枚举调用
        CheckUserEnum checklogin = checknumber.checklogin(login_Usernumber, login_Password);

        //获取返回信息
        int index = checklogin.getIndex();

        String msg = checklogin.getMsg();

        //打印输出信息
        log.info(checklogin.toString());

        //封装给返回类型R
        String code=String.valueOf(index);

        //检查sa-token是否登录过
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        String loginId =(String) tokenInfo.getLoginId();

        log.info("sa-token:"+loginId);

        if(checklogin.isflag()){

            //这里不需要satoken进行赋值了了,checknumber已经实现了
            return R.ok(msg);
        }else{
            return R.error(code,msg);
        }
    }

}
