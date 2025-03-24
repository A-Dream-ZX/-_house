package com.house.mapper;


import com.house.entity.House;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HouseMapper {
    List<House> selectAll(House house);

    //    @Select("select * from t_house where id = #{id}")
    House selectById(Integer id);

    List<House> selectByPriceRange(@Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);


    void insert(House house);

    void updateById(House house);

    @Delete("delete from t_house where id = #{id}")
    void deleteById(Integer id);

    /**
     * 获取房源状态分布统计
     */
    Object getHouseStats();

    /**
     * 获取房源价格区间分布统计
     */
    Object getHousePriceStats();

    @Select("SELECT COUNT(*) FROM t_house")
    long getTotalCount();
}
