package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

    //测试环境采用true,上线时转换为false
    private Boolean flag=true;

    /*
    * 配置docket实现
    * */
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(MyApiInfo())
                .enable(flag)//enable是否启动Swagger，如果为false，那么swagger-ui无法在网页中访问
                .groupName("医疗服务")
                .select()
                //限制扫描包的路径
                .apis(RequestHandlerSelectors.basePackage("demo.Controller"))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                ;

    }

    /*
    * 配置网页信息
    * */
    private ApiInfo MyApiInfo(){
        //项目负责人信息
        return new ApiInfoBuilder()
                .title("基于spark的医疗系统文档")
                .description("一天一苹果,医生远离我!")
                .version("3.0")
                .contact(new Contact("史超凡","http://101.43.182.188:8090/","573877411@qq.com"))
                .build();
    }

    /*
    * 配置测试信息
    * */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new ApiKey("test", "access-token", "header"));
        return schemeList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContextList = new ArrayList<>();
        List<SecurityReference> securityReferenceList = new ArrayList<>();
        securityReferenceList.add(new SecurityReference("access-token", new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")}));
        securityContextList.add(SecurityContext
                .builder()
                .securityReferences(securityReferenceList)
                .operationSelector(operationContext -> true)
                .build()
        );
        return securityContextList;
    }

}
