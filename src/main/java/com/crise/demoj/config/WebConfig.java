package com.crise.demoj.config;

import com.crise.demoj.middlewares.JwtAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtAuth jwtAuth;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuth)
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/login", "/admin/register", "/admin/refresh")
                .excludePathPatterns("/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**");
    }
}
