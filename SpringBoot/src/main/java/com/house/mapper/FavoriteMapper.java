package com.house.mapper;

import java.util.List;
import com.house.entity.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * FavoriteMapper接口用于定义与收藏功能相关的数据库操作
 * 它主要负责在数据库中执行CRUD(创建、读取、更新、删除)操作
 */
@Mapper
public interface FavoriteMapper {

    /**
     * 查询所有收藏记录
     *
     * @param favorite 一个包含查询条件的Favorite对象
     * @return 包含所有符合条件的收藏记录的列表
     */
    List<Favorite> selectAll(Favorite favorite);

    /**
     * 插入一条新的收藏记录
     *
     * @param favorite 要插入的收藏记录对象
     */
    void insert(Favorite favorite);

    /**
     * 根据ID更新收藏记录
     *
     * @param favorite 包含更新信息的收藏记录对象
     */
    void updateById(Favorite favorite);

    /**
     * 根据ID删除收藏记录
     *
     * @param id 要删除的收藏记录的ID
     */
    @Delete("delete from t_favorite where id = #{id}")
    void deleteById(Integer id);

    /**
     * 根据用户ID查询收藏记录
     *
     * @param userId 用户ID
     * @return 包含指定用户所有收藏记录的列表
     */
    List<Favorite> selectByUserId(@Param("userId") Integer userId);

    /**
     * 获取收藏记录的总数量
     *
     * @return 收藏记录的总数量
     */
    @Select("SELECT COUNT(*) FROM t_favorite")
    long getTotalCount();
}
