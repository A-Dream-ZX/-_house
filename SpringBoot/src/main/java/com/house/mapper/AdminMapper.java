package com.house.mapper;

import java.util.List;
import com.house.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * AdminMapper接口用于定义管理员数据访问层的方法
 * 它负责在数据库中执行CRUD（创建、读取、更新、删除）操作
 */
@Mapper
public interface AdminMapper {

    /**
     * 查询所有管理员信息
     *
     * @param admin 一个Admin对象，用于传递查询条件（目前未使用）
     * @return 包含所有管理员信息的列表
     */
    List<Admin> selectAll(Admin admin);

    /**
     * 插入一个新的管理员记录
     *
     * @param admin 要插入的管理员对象，包含管理员的详细信息
     */
    void insert(Admin admin);

    /**
     * 根据用户名查询管理员信息
     *
     * @param username 管理员的用户名，用于查询特定的管理员记录
     * @return 匹配用户名的管理员对象，如果不存在则返回null
     */
    @Select("select * from t_admin where username = #{username}")
    Admin selectByUsername(String username);

    /**
     * 更新管理员信息
     *
     * @param admin 包含更新信息的管理员对象，根据id字段更新数据库中的记录
     */
    void updateById(Admin admin);

    /**
     * 根据管理员ID删除管理员记录
     *
     * @param id 要删除的管理员的ID
     */
    @Delete("delete from t_admin where id = #{id}")
    void deleteById(Integer id);

    /**
     * 根据管理员ID查询管理员信息
     *
     * @param id 管理员的ID，用于查询特定的管理员记录
     * @return 匹配ID的管理员对象，如果不存在则返回null
     */
    @Select("select * from t_admin where id = #{id}")
    Admin selectById(String id);
}
