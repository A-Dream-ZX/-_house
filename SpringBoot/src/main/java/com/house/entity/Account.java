package com.house.entity;

/**
 * 账户实体类 - 系统用户的基类
 * 
 * 作为Admin和User类的父类，包含了所有用户共有的属性和方法
 * 用于统一管理用户认证、授权和基本信息
 */
public class Account {
    /**
     * 用户ID，主键
     */
    private Integer id;
    
    /**
     * 登录账号名称
     */
    private String username;
    
    /**
     * 登录密码
     */
    private String password;
    
    /**
     * 用户角色，用于权限控制
     * 可选值: "admin"(管理员), "user"(普通用户)
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
     * JWT令牌，用于身份验证
     * 由系统生成，不存储在数据库中
     */
    private String token;
    
    /**
     * 用户头像URL
     */
    private String avatar;
    
    /**
     * 修改密码时的新密码
     * 仅用于密码修改过程，不存储在数据库中
     */
    private String newPassword;
    
    /**
     * 修改密码时的确认密码
     * 仅用于密码修改过程，不存储在数据库中
     */
    private String new2Password;

    /**
     * 获取新密码
     * @return 新密码字符串
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * 设置新密码
     * @param newPassword 新密码字符串
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * 获取确认密码
     * @return 确认密码字符串
     */
    public String getNew2Password() {
        return new2Password;
    }

    /**
     * 设置确认密码
     * @param new2Password 确认密码字符串
     */
    public void setNew2Password(String new2Password) {
        this.new2Password = new2Password;
    }

    /**
     * 获取用户头像URL
     * @return 头像URL字符串
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置用户头像URL
     * @param avatar 头像URL字符串
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取JWT令牌
     * @return JWT令牌字符串
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置JWT令牌
     * @param token JWT令牌字符串
     */
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
     * 获取用户名
     * @return 用户名字符串
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     * @param username 用户名字符串
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     * @return 密码字符串
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
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
     * 获取用户电话
     * @return 电话字符串
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置用户电话
     * @param phone 电话字符串
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取用户邮箱
     * @return 邮箱字符串
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置用户邮箱
     * @param email 邮箱字符串
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
