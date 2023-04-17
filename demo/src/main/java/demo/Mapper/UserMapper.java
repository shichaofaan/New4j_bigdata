package demo.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import demo.Entity.Mysql.User;
import org.apache.ibatis.annotations.Mapper;


//持久层
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
