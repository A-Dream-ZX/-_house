package com.house.mapper;


import com.house.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<Admin> selectAll(Admin admin);

    void insert(Admin admin);

    @Select("select * from t_admin where username = #{username}")
    Admin selectByUsername(String username);

    void updateById(Admin admin);

    @Delete("delete from t_admin where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from t_admin where id = #{id}")
    Admin selectById(String id);
}
