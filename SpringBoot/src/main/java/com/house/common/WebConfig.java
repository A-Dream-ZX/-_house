package com.house.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                    // 登录注册相关
                    "/login", 
                    "/register", 
                    
                    // 文件资源相关
                    "/files/**",
                    "/files/download/**", 
                    "/files/house_images/**",
                    "/house/available",
                    
                    // 错误页面
                    "/error", 
                    "/error/**",
                    "/favicon.ico"
                );
    }

    @Bean
    public JWTInterceptor jwtInterceptor() {
        return new JWTInterceptor();
    }
}
