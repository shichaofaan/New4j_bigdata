package demo.service;

import demo.Entity.Mongodb.MongodbFamous;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MyspringbootCache {

    //缓存名人名言
    List<MongodbFamous> getMyFamous();

}
