package com.garv.task_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root swagger-ui to the actual path
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui.html");
        registry.addRedirectViewController("/", "/swagger-ui.html");
    }
}
