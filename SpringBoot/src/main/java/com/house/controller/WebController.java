package com.house.controller;

import com.house.common.Result;
import com.house.entity.Account;
import com.house.entity.User;
import com.house.service.AdminService;
import com.house.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Web通用控制器
 * 
 * 处理系统公共功能，如登录、注册、修改密码等
 * 这些功能不需要身份验证或对所有用户开放
 */
@RestController
public class WebController {

    @Resource
    AdminService adminService; // 管理员服务

    @Resource
    UserService userService; // 用户服务

    /**
     * 根路径访问
     * 提供简单的欢迎信息，用于测试API是否可用
     * 
     * @return 包含欢迎信息的Result对象
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success("Hello World");
    }

    /**
     * 用户登录接口
     * 根据角色类型调用不同的服务进行登录验证
     * 
     * @param account 包含用户名、密码和角色的账户对象
     * @return 登录成功返回用户信息和token，失败返回错误信息
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        // 根据角色类型调用相应的登录方法
        if ("admin".equals(account.getRole())) {
            // 管理员登录
            dbAccount = adminService.login(account);
        } else if ("user".equals(account.getRole())) {
            // 普通用户登录
            dbAccount = userService.login(account);
        } else {
            // 角色不存在
            return Result.error("-1", "角色不存在");
        }
        // 返回包含用户信息和token的结果
        return Result.success(dbAccount);
    }

    /**
     * 用户注册接口
     * 仅支持普通用户注册，管理员由系统管理员创建
     * 
     * @param user 包含注册信息的用户对象
     * @return 注册成功返回成功消息，失败抛出异常
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }

    /**
     * 修改密码接口
     * 根据用户角色调用相应的密码修改方法
     * 
     * @param account 包含新密码和确认密码的账户对象
     * @return 修改成功返回成功消息，失败抛出异常
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        // 根据角色调用相应的密码修改方法
        if ("admin".equals(account.getRole())) {
            // 管理员修改密码
            adminService.updatePassword(account);
        }
        if ("user".equals(account.getRole())) {
            // 普通用户修改密码
            userService.updatePassword(account);
        }

        return Result.success();
    }
}
