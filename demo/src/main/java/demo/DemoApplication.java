package demo;

import cn.dev33.satoken.SaManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Slf4j
@EnableRedisRepositories                 //让Redis的配置生效
@SpringBootApplication(scanBasePackages = {"demo"})
@EnableTransactionManagement  //开启事务
@EnableWebMvc
@MapperScan(basePackages = {"demo.Mapper"})      //mybatis-plus扫描包
@EnableWebSocket                    //启用websocket服务
@EnableNeo4jRepositories            //开启neo4j服务
public class DemoApplication {

    /**
     * 修改四个1.properties 2.修改spark工具 3.修改ChatController 4.修改websocket协议html文件
     * @param args
     * @throws JsonProcessingException
     */
    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(DemoApplication.class, args);
        log.info("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }

}

