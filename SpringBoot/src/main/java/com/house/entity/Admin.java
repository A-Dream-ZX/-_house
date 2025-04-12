package com.house.entity;

/**
 * 管理员实体类
 * 
 * 继承自Account基类，存储系统管理员的详细信息
 * 管理员具有最高权限，可以管理房源、用户、租约和系统公告等
 */
public class Admin extends Account {
    /**
     * 管理员ID，主键
     * 继承自Account类，但仍需要单独定义以便与数据库映射
     */
    private Integer id;
    
    /**
     * 管理员账号名
     * 用于登录系统，必须唯一
     */
    private String username;
    
    /**
     * 管理员密码
     * 存储加密后的密码字符串
     */
    private String password;
    
    /**
     * 管理员角色
     * 固定值为"admin"，标识管理员身份
     */
    private String role;
    
    /**
     * 管理员真实姓名
     */
    private String name;
    
    /**
     * 管理员联系电话
     */
    private String phone;
    
    /**
     * 管理员电子邮箱
     */
    private String email;
    
    /**
     * 管理员JWT令牌
     * 登录成功后生成，用于身份验证
     */
    private String token;

    /**
     * 管理员头像URL
     */
    private String avatar;

    /**
     * 获取管理员头像URL
     * 重写父类方法
     * @return 头像URL字符串
     */
    @Override
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置管理员头像URL
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
     * 获取管理员ID
     * @return 管理员ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置管理员ID
     * @param id 管理员ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取管理员账号名
     * @return 账号名字符串
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置管理员账号名
     * @param username 账号名字符串
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取管理员密码
     * @return 密码字符串
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置管理员密码
     * @param password 密码字符串
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取管理员角色
     * @return 角色字符串
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置管理员角色
     * @param role 角色字符串
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取管理员真实姓名
     * @return 姓名字符串
     */
    public String getName() {
        return name;
    }

    /**
     * 设置管理员真实姓名
     * @param name 姓名字符串
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取管理员联系电话
     * @return 电话字符串
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置管理员联系电话
     * @param phone 电话字符串
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取管理员电子邮箱
     * @return 邮箱字符串
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置管理员电子邮箱
     * @param email 邮箱字符串
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
