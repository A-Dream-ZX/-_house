package com.house.mapper;

import com.house.entity.Rental;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;  // 添加这行导入

import java.util.List;

@Mapper
public interface RentalMapper {
    List<Rental> selectAll(Rental rental);

    void insert(Rental rental);

    void updateById(Rental rental);

    @Delete("delete from t_rental where id = #{id}")
    void deleteById(Integer id);

    List<Rental> selectByUserId(@Param("userId") Integer userId);

    List<Rental> selectActiveByHouseId(@Param("houseId") Integer houseId);

    Rental selectById(@Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM t_rental")
    long getTotalCount();

    @Select("SELECT COUNT(*) FROM t_rental WHERE DATE_FORMAT(created_at, '%Y-%m') = DATE_FORMAT(CURRENT_DATE, '%Y-%m')")
    long getNewRentalsThisMonth();

    @Select("SELECT COUNT(*) FROM t_rental WHERE YEARWEEK(created_at) = YEARWEEK(CURRENT_DATE)")
    long getNewRentalsThisWeek();
}
