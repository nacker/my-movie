package org.movie.config;

import org.movie.common.config.BaseSwaggerConfig;
import org.movie.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * Created by macro on 2018/4/26.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("org.movie.modules")
                .title("电影项目后台管理系统")
                .description("电影项目后台管理接口文档")
                .contactName("nacker")
                .version("1.0")
                .enableSecurity(false)
                .build();
    }
}