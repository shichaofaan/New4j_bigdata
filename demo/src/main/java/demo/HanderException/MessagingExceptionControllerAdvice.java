package demo.HanderException;

import com.fasterxml.jackson.core.JsonProcessingException;
import demo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice          //@RestControllerAdvice = @ControllerAdvice + @ResponseBody
public class MessagingExceptionControllerAdvice {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public R IllegalArgumentException(IllegalArgumentException e, HttpServletRequest request){
        log.error("查询user出现异常(属性为null):{},异常类型:{},请求的url:{}",e.getMessage(),e.getClass(),request.getRequestURL());
        return R.error("属性没有查询出来");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public R RuntimeException(RuntimeException e, HttpServletRequest request){
        log.error("运行时异常:{},异常类型:{},请求的url:{}",e.getMessage(),e.getClass(),request.getRequestURL());
        return R.error("1,该账号已经被注册(可能性比较大) 2.数据库连接超时");
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    public R JsonProcessingException(JsonProcessingException e,HttpServletRequest request){
        log.error("websocket异常:{},异常类型:{},请求的url:{}",e.getMessage(),e.getClass(),request.getRequestURL());
        return R.error("websocket服务端获取信息异常");
    }

    @ExceptionHandler(value = RedisSystemException.class)
    public R JsonProcessingException(RedisSystemException e,HttpServletRequest request){
        log.error("Redis系统连接异常:{},异常类型:{},请求的url:{}",e.getMessage(),e.getClass(),request.getRequestURL());
        return R.error("Redis服务端连接异常");
    }

    @ExceptionHandler(value = NullPointerException.class)
    public R NullPointerException(NullPointerException e,HttpServletRequest request){
        log.error("空指针异常:{},异常类型:{},请求的url:{}",e.getMessage(),e.getClass(),request.getRequestURL());
        return R.error("服务端端空指针异常");
    }
}