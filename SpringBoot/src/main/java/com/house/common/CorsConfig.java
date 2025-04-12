package com.house.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置类，用于解决前端跨域请求问题
 */
@Configuration
public class CorsConfig {
    /**
     * 创建并配置跨域过滤器
     *
     * @return CorsFilter 跨域过滤器实例
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建URL基于的跨域配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 创建跨域配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许所有源地址访问
        corsConfiguration.addAllowedOrigin("*");
        // 允许所有请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许所有请求方法
        corsConfiguration.addAllowedMethod("*");
        // 对所有接口路径应用跨域配置
        source.registerCorsConfiguration("/**", corsConfiguration);
        // 返回跨域过滤器实例
        return new CorsFilter(source);
    }
}

