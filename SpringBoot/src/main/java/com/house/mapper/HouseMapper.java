package com.house.mapper;


import com.house.entity.House;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 房源数据访问接口
 * 
 * 提供房源相关的数据库操作方法，包括CRUD操作和统计查询
 * 对应表: t_house
 */
@Mapper
public interface HouseMapper {
    /**
     * 条件查询房源列表
     * 根据传入的房源对象属性进行条件筛选
     * 
     * @param house 查询条件封装对象，属性为null则不作为筛选条件
     * @return 符合条件的房源列表
     */
    List<House> selectAll(House house);

    /**
     * 根据ID查询房源详情
     * 
     * @param id 房源ID
     * @return 房源对象，不存在则返回null
     */
    House selectById(Integer id);

    /**
     * 根据价格区间查询房源
     * 
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 符合价格区间的房源列表
     */
    List<House> selectByPriceRange(@Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);

    /**
     * 插入新房源
     * 
     * @param house 待插入的房源对象
     */
    void insert(House house);

    /**
     * 更新房源信息
     * 
     * @param house 包含更新内容的房源对象，必须包含ID
     */
    void updateById(House house);

    /**
     * 删除房源
     * 
     * @param id 要删除的房源ID
     */
    @Delete("delete from t_house where id = #{id}")
    void deleteById(Integer id);

    /**
     * 获取房源状态分布统计
     * 按状态分组统计各状态的房源数量
     * 
     * @return 包含状态和对应数量的Map列表
     */
    Object getHouseStats();

    /**
     * 获取房源价格区间分布统计
     * 按价格区间分组统计各区间的房源数量
     * 
     * @return 包含价格区间和对应数量的Map列表
     */
    Object getHousePriceStats();

    /**
     * 获取房源总数
     * 
     * @return 系统中的总房源数量
     */
    @Select("SELECT COUNT(*) FROM t_house")
    long getTotalCount();
}
