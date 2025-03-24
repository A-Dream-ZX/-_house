package com.house.mapper;


import com.house.entity.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;  // 添加这行导入

import java.util.List;

@Mapper
public interface FavoriteMapper {
    List<Favorite> selectAll(Favorite favorite);

    void insert(Favorite favorite);

    void updateById(Favorite favorite);

    @Delete("delete from t_favorite where id = #{id}")
    void deleteById(Integer id);

    List<Favorite> selectByUserId(@Param("userId") Integer userId);

    @Select("SELECT COUNT(*) FROM t_favorite")
    long getTotalCount();
}
