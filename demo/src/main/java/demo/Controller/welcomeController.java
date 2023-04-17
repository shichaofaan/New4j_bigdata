package demo.Controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import demo.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
@Api(tags = "static/welcome")
public class welcomeController {

    /**
     * 主页面
     * @return
     */
    @ApiOperation(value = "主页面")
    @RequestMapping(value = {"/"})
    public String welcomehome(){
        return "welcome/index";
    }

    /**
     * 主页面
     * @return
     */
    @ApiOperation(value = "主页面")
    @SaCheckLogin
    @RequestMapping(value = {"/welcome/*,/*"})
    public String welcome(){
        boolean login;
        long loginId;
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        StpUtil.checkLogin();
        login = StpUtil.isLogin();
        loginId = StpUtil.getLoginIdAsLong();

        if(login){
            log.info("是否登录:"+login+"   登录id:"+loginId+"     tokenInfo:"+tokenInfo);
            return "welcome/index";
        }else{
            log.info("是否登录:"+login+"   登录id:"+loginId+"     tokenInfo:"+tokenInfo);
            return "login/index";
        }
    }

    /**
     * 正在开发的页面
     * @return
     */
    @ApiOperation(value = "Workering页面")
    @SaCheckLogin
    @RequestMapping(value = {"/Error/*"})
    public String Error(){
        boolean login;
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        StpUtil.checkLogin();
        login = StpUtil.isLogin();
        long loginId = StpUtil.getLoginIdAsLong();

        if(login){
            log.info("是否登录:"+login+"   登录id:"+loginId+"     tokenInfo:"+tokenInfo);
            return "Error/index";
        }else{
            log.info("是否登录:"+login+"   登录id:"+loginId+"     tokenInfo:"+tokenInfo);
            return "login/index";
        }
    }

    /**
     * user页面访问
     * @return
     */
    @RequestMapping(value = {"/UserPage/*"})
    public String welcomeAL(){
        boolean login;
        long loginId;
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        StpUtil.checkLogin();
        login = StpUtil.isLogin();
        loginId = StpUtil.getLoginIdAsLong();

        if(login){
            log.info("是否登录:"+login+"   登录id:"+loginId+"     tokenInfo:"+tokenInfo);
            return "UserPage/index";
        }else{
            log.info("是否登录:"+login+"   登录id:"+loginId+"     tokenInfo:"+tokenInfo);
            return "login/index";
        }
    }

    /**
     * user页面访问
     * @return
     */
    @RequestMapping(value = {"/Personalpage/*"})
    public String welcomePersonalPage(){
        boolean login;
        long loginId;
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        StpUtil.checkLogin();
        login = StpUtil.isLogin();
        loginId = StpUtil.getLoginIdAsLong();

        if(login){
            log.info("是否登录:"+login+"   登录id:"+loginId+"     tokenInfo:"+tokenInfo);
            return "Personalpage/index";
        }else{
            log.info("是否登录:"+login+"   登录id:"+loginId+"     tokenInfo:"+tokenInfo);
            return "login/index";
        }
    }

    /*
     * 注销
     * */
    @ApiOperation(value = "退出登录方法")
    @PostMapping("/logout")
    public String logout() {
        StpUtil.logout();
        log.info("退出系统:"+R.ok("退出成功"));
        return "login/index";
    }

    /*
     * 查询信息
     * */
    @ApiOperation(value = "查询信息方法")
    @PostMapping("/tokenInfo")
    public void tokenInfo() {
        System.out.println(SaResult.data(StpUtil.getTokenInfo()));
    }
}
