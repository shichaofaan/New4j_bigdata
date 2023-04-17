package demo.utils;

import demo.Entity.Mongodb.MongodbFamous;
import demo.service.impl.MyspringbootCacheimpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/*  * 相似度算法(莱文斯坦算法)  对比100w数据也仅仅只需要680毫秒*/

/**
 * Levenshtein距离是一种计算两个字符串间的差异程度的字符串度量（string metric）。
 * 我们可以认为Levenshtein距离就是从一个字符串修改到另一个字符串时，其中编辑单个字符（比如修改、插入、删除）所需要的最少次数。
 * 俄罗斯科学家Vladimir Levenshtein于1965年提出了这一概念。
 */
@Component
@Slf4j
public class SimilarityAlgorithm {

    @Resource
    private MyspringbootCacheimpl myspringbootCacheimpl;

    /**
     * 推荐名人名言
     * @return
     */
    public String FamousMsg(){

        String result="";

        //获取随机数
        int random =(int)System.currentTimeMillis();

        //得到集合
        List<MongodbFamous> myFamous = myspringbootCacheimpl.getMyFamous();

        //获取集合总长度
        int size = myFamous.size();

        //求出0-length的随机数
        int v = random % (size);

        //防止时间戳成负数
        if(v<0)
        {
            v=-v;
        }

        //获取数
        MongodbFamous mongodbFamous = myFamous.get(v);

        log.info("随机数为:"+v+" 获取了id为:"+mongodbFamous.getId()+"的名人名言");

        //拼接
        result=result.concat(mongodbFamous.getFamousMsg());

        return result;
    }

}
