package demo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

//User填充方法使用
@Component
public class DataTimeConfig implements MetaObjectHandler {

    @Override
    //使用mp实现添加操作，这个方法执行
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()),metaObject);
        this.setFieldValByName("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()),metaObject);
    }

    @Override
    //使用mp实现修改操作，这个方法执行
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()),metaObject);
    }
}
