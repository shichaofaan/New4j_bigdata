package demo.HanderException;


import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice              //可以跳转页面就不能用Rest了
public class HtmlExceptionController {

    @ExceptionHandler(value = NotLoginException.class)
    public String NotLoginException(NotLoginException e, HttpServletRequest request){
        log.error("sa-token登录异常:{},异常类型:{},请求的url:{},位置信息:{}",e.getMessage(),e.getClass(),request.getRequestURL());
        return "login/index";
    }

}
