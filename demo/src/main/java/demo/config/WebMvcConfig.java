package demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 资源转发器
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
         *配置转发器
         */
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        log.info("静态资源映射成功...");
    }

}
