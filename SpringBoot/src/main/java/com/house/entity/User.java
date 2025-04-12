package com.house.entity;

import java.time.LocalDateTime;  // 添加这行导入

/**
 * 用户实体类
 * 
 * 继承自Account基类，存储普通用户的详细信息
 * 用户可以浏览房源、收藏房源、申请租赁等
 */
public class User extends Account {
    /**
     * 用户ID，主键
     * 继承自Account类，但仍需要单独定义以便与数据库映射
     */
    private Integer id;
    
    /**
     * 用户账号名
     * 用于登录系统，必须唯一
     */
    private String username;
    
    /**
     * 用户密码
     * 存储加密后的密码字符串
     */
    private String password;
    
    /**
     * 用户角色
     * 固定值为"user"，标识普通用户身份
     */
    private String role;
    
    /**
     * 用户真实姓名
     */
    private String name;
    
    /**
     * 用户联系电话
     */
    private String phone;
    
    /**
     * 用户电子邮箱
     */
    private String email;
    
    /**
     * 用户JWT令牌
     * 登录成功后生成，用于身份验证
     */
    private String token;
    
    /**
     * 用户头像URL
     */
    private String avatar;
    
    /**
     * 用户注册时间
     * 记录用户创建账号的时间点
     */
    private LocalDateTime createdAt;

    /**
     * 获取用户头像URL
     * 重写父类方法
     * @return 头像URL字符串
     */
    @Override
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置用户头像URL
     * 重写父类方法
     * @param avatar 头像URL字符串
     */
    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取JWT令牌
     * 重写父类方法
     * @return JWT令牌字符串
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * 设置JWT令牌
     * 重写父类方法
     * @param token JWT令牌字符串
     */
    @Override
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置用户ID
     * @param id 用户ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户账号名
     * @return 账号名字符串
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户账号名
     * @param username 账号名字符串
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取用户密码
     * @return 密码字符串
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码
     * @param password 密码字符串
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取用户角色
     * @return 角色字符串
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置用户角色
     * @param role 角色字符串
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取用户真实姓名
     * @return 姓名字符串
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户真实姓名
     * @param name 姓名字符串
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取用户联系电话
     * @return 电话字符串
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置用户联系电话
     * @param phone 电话字符串
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取用户电子邮箱
     * @return 邮箱字符串
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置用户电子邮箱
     * @param email 邮箱字符串
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取用户注册时间
     * @return 注册时间
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置用户注册时间
     * @param createdAt 注册时间
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
