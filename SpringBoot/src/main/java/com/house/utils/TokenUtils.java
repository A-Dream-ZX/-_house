package com.house.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.house.entity.Account;
import com.house.service.AdminService;
import com.house.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * Token工具类
 * 
 * 提供JWT令牌的生成、验证和用户信息获取等功能
 * 包含静态方法，可在任何地方调用，不需要依赖注入实例
 */
@Component
public class TokenUtils {

    @Resource
    AdminService adminService; // 管理员服务，注入后会复制给静态变量
    
    @Resource
    UserService userService; // 用户服务，注入后会复制给静态变量

    // 静态服务引用，用于在静态方法中访问
    static AdminService staticAdminService;
    static UserService staticUserService;

    /**
     * 初始化方法
     * Spring Boot启动时自动执行，将注入的服务复制到静态变量
     * 解决静态方法中无法直接使用依赖注入的问题
     */
    @PostConstruct
    public void init() {
        staticAdminService = adminService;
        staticUserService = userService;
    }

    /**
     * 生成JWT令牌
     * 
     * @param data 存储在令牌中的数据，格式为"userId-role"
     * @param sign 签名密钥，通常使用用户密码
     * @return 生成的JWT令牌字符串
     */
    public static String createToken(String data, String sign) {
        return JWT.create()
                .withAudience(data)  // 将userId-role保存到token中作为载荷
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1))  // 设置1天后过期
                .sign(Algorithm.HMAC256(sign));  // 使用HMAC256算法和密码作为密钥进行签名
    }


    /**
     * 获取当前登录用户信息
     * 从请求中获取token，解析出用户ID和角色，然后查询用户信息
     * 
     * @return 当前登录的用户对象，未登录或解析失败时返回null
     */
    public static Account getCurrentUser() {
        try {
            // 获取当前请求对象
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            
            // 从请求头或参数中获取token
            String token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }
            
            // token为空则返回null
            if (StrUtil.isBlank(token)) {
                return null;
            }
            
            // 解析token的载荷数据，格式为"userId-role"
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            String userId = split[0]; // 用户ID
            String role = split[1];   // 用户角色
            
            // 根据角色类型查询对应的用户信息
            if ("admin".equals(role)) {
                return staticAdminService.selectById(userId); // 查询管理员信息
            } else if ("user".equals(role)) {
                return staticUserService.selectById(userId); // 查询普通用户信息
            }
        } catch (Exception e) {
            // 解析过程中出现任何异常都返回null
            return null;
        }
        return null;
    }
}
