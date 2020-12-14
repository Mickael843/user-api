package com.mikaeru.user_api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Value("${server.servlet.context-path}")
    private String contextPath;

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
