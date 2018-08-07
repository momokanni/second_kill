package com.kill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger 配置类
 *
 * @author Administrator
 * @create 2018-08-07 20:07
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket alipayApi() {

        return new Docket(DocumentationType.SWAGGER_2).groupName("秒杀接口文档")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kill.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("秒杀")
                .description("商品秒杀")
                .termsOfServiceUrl("http://kill.limit-tech.com")
                .contact(new Contact("SunLin", "http://kill.limit-tech.com/secondKill/goods/list", "740949744@qq.com"))
                .version("1.0").build();
    }
}
