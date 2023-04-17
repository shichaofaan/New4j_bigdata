package demo.service.impl;

import demo.Entity.Mongodb.MongodbFamous;
import demo.service.MyspringbootCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class MyspringbootCacheimpl implements MyspringbootCache {

    /**
     * mongodb的使用工具
     */
    @Resource
    private MongoTemplate mongoTemplate;


    /**
     * 缓存名人名言
     * @return
     */
    @Override
    @Cacheable(value = {"FamousMsg"}, key = "'List'")
    public List<MongodbFamous> getMyFamous() {

        // 从数据库或其他数据源获取List集合数据
        List<MongodbFamous> List;

        /**
         * 获取所有名人名言集合
         */
        List=mongoTemplate.findAll(MongodbFamous.class);

        log.info("进行了一次MongodbFamous查询,缓存到了Springboot缓存池当中");

        return List;
    }

}
