package demo.Controller;

import demo.Enum.CheckUserEnum;
import demo.common.R;
import demo.utils.Checknumber;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/registerpage")
@Slf4j
@Api(tags = "registerpage")
public class registerpageController {

    @Resource
    private Checknumber checknumber;

    /**
     * 注册
     * @param password
     * @param email
     * @param name
     * @param email_password
     * @return
     */
    @ApiOperation(value = "注册页面(注册成功就直接进入主页面)")
    @PostMapping( "/registerpage")
    public R login(@RequestParam("registerpage_Password") String password,@RequestParam("registerpage_email") String email,
                        @RequestParam("registerpage_Name") String name, @RequestParam("registerpage_email_password") String email_password){
        log.info("key:"+email_password);

        CheckUserEnum checkregisterpage = checknumber.checkregisterpage(password, email, name, email_password);

        //获取返回信息
        int index = checkregisterpage.getIndex();

        String msg = checkregisterpage.getMsg();

        //打印输出信息
        log.info(checkregisterpage.toString());

        //封装给返回类型R
        String code=String.valueOf(index);

        //或者用code值判读也行
        if(checkregisterpage.isflag())
        {
            return R.ok(msg);
        }else{
            return R.error(code,msg);
        }
    }

}
