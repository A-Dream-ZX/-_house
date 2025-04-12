package com.house.mapper;


import com.house.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据访问接口
 * 
 * 提供用户相关的数据库操作方法，包括CRUD操作和统计查询
 * 对应表: t_user
 */
@Mapper
public interface UserMapper {
    /**
     * 条件查询用户列表
     * 根据传入的用户对象属性进行条件筛选
     * 
     * @param user 查询条件封装对象，属性为null则不作为筛选条件
     * @return 符合条件的用户列表
     */
    List<User> selectAll(User user);

    /**
     * 插入新用户
     * 
     * @param user 待插入的用户对象
     */
    void insert(User user);

    /**
     * 根据用户名查询用户
     * 用于登录验证和用户名查重
     * 
     * @param username 用户名
     * @return 用户对象，不存在则返回null
     */
    @Select("select * from t_user where username = #{username}")
    User selectByUsername(String username);

    /**
     * 更新用户信息
     * 
     * @param user 包含更新内容的用户对象，必须包含ID
     */
    void updateById(User user);

    /**
     * 删除用户
     * 
     * @param id 要删除的用户ID
     */
    @Delete("delete from t_user where id = #{id}")
    void deleteById(Integer id);

    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return 用户对象，不存在则返回null
     */
    @Select("select * from t_user where id = #{id}")
    User selectById(String id);

    /**
     * 获取用户注册趋势统计
     * 按月份分组统计每月新注册的用户数量
     * 
     * @return 包含日期和对应新增用户数量的统计数据
     */
    Object getUserStats();

    /**
     * 获取用户总数
     * 
     * @return 系统中的总用户数量
     */
    @Select("SELECT COUNT(*) FROM t_user")
    long getTotalCount();

    /**
     * 获取本月新增用户数
     * 统计当前月份注册的用户数量
     * 
     * @return 本月新注册的用户数量
     */
    @Select("SELECT COUNT(*) FROM t_user WHERE DATE_FORMAT(created_at, '%Y-%m') = DATE_FORMAT(CURRENT_DATE, '%Y-%m')")
    long getNewUsersThisMonth();

    /**
     * 获取本周新增用户数
     * 统计当前周注册的用户数量
     * 
     * @return 本周新注册的用户数量
     */
    @Select("SELECT COUNT(*) FROM t_user WHERE YEARWEEK(created_at) = YEARWEEK(CURRENT_DATE)")
    long getNewUsersThisWeek();
}
