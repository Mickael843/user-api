package com.mikaeru.user_api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Responsável pela configuração da Api.
 *
 * @author Mickael Luiz
 */
public class WebConfig implements WebMvcConfigurer {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * Libera o CORS para todos os serviços que forem consumirem a api.
     *
     * @param registry {@link CorsRegistry}
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping(contextPath + "/**")
                .allowedOrigins("*")
                .allowedMethods("POST,PUT,GET,DELETE,OPTIONS")
                .allowedHeaders("Location", "Content-type", "authorization")
                .exposedHeaders("Location", "Content-Disposition");
    }
}
