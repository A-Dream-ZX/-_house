package com.house.mapper;

import com.house.entity.Rental;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;  // 添加这行导入

import java.util.List;

/**
 * 租约数据访问接口
 * 
 * 提供租约相关的数据库操作方法，包括CRUD操作和统计查询
 * 对应表: t_rental
 */
@Mapper
public interface RentalMapper {
    /**
     * 条件查询租约列表
     * 根据传入的租约对象属性进行条件筛选
     * 
     * @param rental 查询条件封装对象，属性为null则不作为筛选条件
     * @return 符合条件的租约列表
     */
    List<Rental> selectAll(Rental rental);

    /**
     * 插入新租约
     * 
     * @param rental 待插入的租约对象
     */
    void insert(Rental rental);

    /**
     * 更新租约信息
     * 
     * @param rental 包含更新内容的租约对象，必须包含ID
     */
    void updateById(Rental rental);

    /**
     * 删除租约
     * 
     * @param id 要删除的租约ID
     */
    @Delete("delete from t_rental where id = #{id}")
    void deleteById(Integer id);

    /**
     * 查询用户的租约列表
     * 获取指定用户的所有租约记录
     * 
     * @param userId 用户ID
     * @return 该用户的所有租约列表
     */
    List<Rental> selectByUserId(@Param("userId") Integer userId);

    /**
     * 查询房源的有效租约
     * 获取指定房源的所有未完成租约
     * 
     * @param houseId 房源ID
     * @return 该房源的所有有效租约列表
     */
    List<Rental> selectActiveByHouseId(@Param("houseId") Integer houseId);

    /**
     * 根据ID查询租约详情
     * 
     * @param id 租约ID
     * @return 租约对象，不存在则返回null
     */
    Rental selectById(@Param("id") Integer id);

    /**
     * 获取租约总数
     * 
     * @return 系统中的总租约数量
     */
    @Select("SELECT COUNT(*) FROM t_rental")
    long getTotalCount();

    /**
     * 获取本月新增租约数
     * 统计当前月份创建的租约数量
     * 
     * @return 本月新创建的租约数量
     */
    @Select("SELECT COUNT(*) FROM t_rental WHERE DATE_FORMAT(created_at, '%Y-%m') = DATE_FORMAT(CURRENT_DATE, '%Y-%m')")
    long getNewRentalsThisMonth();

    /**
     * 获取本周新增租约数
     * 统计当前周创建的租约数量
     * 
     * @return 本周新创建的租约数量
     */
    @Select("SELECT COUNT(*) FROM t_rental WHERE YEARWEEK(created_at) = YEARWEEK(CURRENT_DATE)")
    long getNewRentalsThisWeek();
}
