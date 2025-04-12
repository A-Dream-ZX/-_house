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
 * JWT拦截器实现
 * 用于验证请求中的JWT令牌，确保API访问的安全性
 * 拦截所有需要授权的接口请求，进行token验证
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    AdminService adminService; // 注入管理员服务

    @Resource
    UserService userService; // 注入用户服务

    /**
     * 请求预处理方法
     * 在Controller处理请求前被调用
     * 
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器对象
     * @return 如果返回true则继续处理，返回false则中断请求
     * @throws Exception 处理过程中可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从HTTP请求头中获取token
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            // 如果请求头中没有token，则尝试从URL参数中获取
            token = request.getParameter("token");
        }
        
        // 2. 检查token是否存在
        if (StrUtil.isBlank(token)) {
            // token不存在，抛出401未授权异常
            throw new CustomerException("401", "未提供token");
        }
        
        Account account = null;
        try {
            // 3. 解析token，从JWT的audience声明中获取用户信息
            String audience = JWT.decode(token).getAudience().get(0);
            
            // 4. 验证token格式
            if (StrUtil.isBlank(audience) || !audience.contains("-")) {
                throw new CustomerException("401", "无效的token格式");
            }
            
            // 5. 解析用户ID和角色
            // token中的audience格式应为"userId-role"
            String[] split = audience.split("-");
            if (split.length != 2) {
                throw new CustomerException("401", "无效的token格式");
            }
            
            String userId = split[0]; // 用户ID
            String role = split[1];   // 用户角色
            
            // 6. 验证用户ID和角色是否有效
            if (StrUtil.isBlank(userId) || StrUtil.isBlank(role)) {
                throw new CustomerException("401", "无效的用户ID或角色");
            }
            
            // 7. 根据角色和用户ID从数据库查询用户信息
            if ("admin".equals(role)) {
                // 查询管理员信息
                account = adminService.selectById(userId);
            } else if ("user".equals(role)) {
                // 查询普通用户信息
                account = userService.selectById(userId);
            } else {
                // 未知的用户角色
                throw new CustomerException("401", "未知的用户角色: " + role);
            }
        } catch (JWTDecodeException e) {
            // 8. 处理JWT解码异常
            throw new CustomerException("401", "token解析失败: " + e.getMessage());
        } catch (CustomerException e) {
            // 9. 直接抛出已经包含详细信息的自定义异常
            throw e;
        } catch (Exception e) {
            // 10. 处理其他异常
            throw new CustomerException("401", "token验证异常: " + e.getMessage());
        }
        
        // 11. 确认用户存在
        if (account == null) {
            throw new CustomerException("401", "用户不存在");
        }
        
        try {
            // 12. 使用用户密码作为密钥验证token签名
            // 通过验证签名可以确保token没有被篡改
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            // 13. 处理签名验证失败异常
            throw new CustomerException("401", "token签名验证失败: " + e.getMessage());
        }
        
        // 14. 所有验证通过，允许请求继续处理
        return true;
    }
}
