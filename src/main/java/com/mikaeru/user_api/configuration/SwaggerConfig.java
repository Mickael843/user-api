package com.mikaeru.user_api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Classe responsável pela configuração do documentação fornecida pelo swagger.
 *
 * @author Mickael Luiz
 */
public class SwaggerConfig {

    @Value("${release.version}")
    private String releaseVersion;

    @Value("${api.version}")
    private String apiVersion;

    @Value("${swagger.enable}")
    private boolean enableSwagger;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mikkaeru.user_api.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(enableSwagger);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("User API")
                .description("User API - Documentação dos Endpoints")
                .version(releaseVersion.concat("_").concat(apiVersion))
                .build();
    }
}
