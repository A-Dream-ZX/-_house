package com.house.mapper;


import com.house.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectAll(User user);

    void insert(User user);

    @Select("select * from t_user where username = #{username}")
    User selectByUsername(String username);

    void updateById(User user);

    @Delete("delete from t_user where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from t_user where id = #{id}")
    User selectById(String id);

    /**
     * 获取用户注册趋势统计
     * @return 包含日期和对应新增用户数量的统计数据
     */
    Object getUserStats();

    @Select("SELECT COUNT(*) FROM t_user")
    long getTotalCount();

    @Select("SELECT COUNT(*) FROM t_user WHERE DATE_FORMAT(created_at, '%Y-%m') = DATE_FORMAT(CURRENT_DATE, '%Y-%m')")
    long getNewUsersThisMonth();

    @Select("SELECT COUNT(*) FROM t_user WHERE YEARWEEK(created_at) = YEARWEEK(CURRENT_DATE)")
    long getNewUsersThisWeek();
}
