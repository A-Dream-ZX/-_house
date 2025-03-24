package com.house.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.house.entity.Account;
import com.house.exception.CustomerException;
import com.house.service.AdminService;
import com.house.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器实现
 */

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    AdminService adminService;

    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的token
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            // 如果没有token，则从参数中获取
            token = request.getParameter("token");
        }
        // 2. 开始验证token
        if (StrUtil.isBlank(token)) {
            throw new CustomerException("401", "未提供token");
        }
        Account account = null;
        try {
            // 从token中获取用户id和角色
            String audience = JWT.decode(token).getAudience().get(0);
            if (StrUtil.isBlank(audience) || !audience.contains("-")) {
                throw new CustomerException("401", "无效的token格式");
            }
            
            String[] split = audience.split("-");
            if (split.length != 2) {
                throw new CustomerException("401", "无效的token格式");
            }
            
            String userId = split[0];
            String role = split[1];
            
            if (StrUtil.isBlank(userId) || StrUtil.isBlank(role)) {
                throw new CustomerException("401", "无效的用户ID或角色");
            }
            
            // 根据token解析出来的userId去对应的表中查询用户信息
            if ("admin".equals(role)) {
                account = adminService.selectById(userId);
            } else if ("user".equals(role)) {
                account = userService.selectById(userId);
            } else {
                throw new CustomerException("401", "未知的用户角色: " + role);
            }
        } catch (JWTDecodeException e) {
            throw new CustomerException("401", "token解析失败: " + e.getMessage());
        } catch (CustomerException e) {
            throw e; // 直接抛出已经包含详细信息的异常
        } catch (Exception e) {
            throw new CustomerException("401", "token验证异常: " + e.getMessage());
        }
        
        if (account == null) {
            throw new CustomerException("401", "用户不存在");
        }
        
        try {
            // 验证签名
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new CustomerException("401", "token签名验证失败: " + e.getMessage());
        }
        return true;
    }

}
