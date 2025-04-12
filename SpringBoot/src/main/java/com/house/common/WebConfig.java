package com.house.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 
 * 配置MVC相关组件，包括拦截器、跨域处理等
 * 主要负责拦截器链的组织和排除路径的配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * 添加拦截器到拦截器链
     * 配置JWT拦截器，以实现API接口的认证授权
     * 
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(
                    // 登录注册相关 - 这些路径无需认证即可访问
                    "/",        // 添加根路径，避免首页访问需要token
                    "/login",   // 登录接口
                    "/register", // 注册接口
                    
                    // 文件资源相关 - 允许直接访问文件资源
                    "/files/**", // 所有文件请求
                    "/files/download/**", // 文件下载
                    "/files/house_images/**", // 房屋图片
                    "/house/available", // 可用房源查询
                    
                    // 错误页面和其他静态资源
                    "/error",    // 错误页面
                    "/error/**", // 所有错误页面
                    "/favicon.ico" // 网站图标
                );
    }

    /**
     * 创建JWT拦截器Bean
     * 将拦截器注册为Spring Bean以便自动注入依赖
     * 
     * @return 配置好的JWT拦截器实例
     */
    @Bean
    public JWTInterceptor jwtInterceptor() {
        return new JWTInterceptor();
    }
}
