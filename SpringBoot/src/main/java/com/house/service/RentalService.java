package com.house.service;

import com.house.entity.Rental;
import com.house.mapper.RentalMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 租约服务类
 * 
 * 提供租约相关的业务逻辑实现，包括CRUD操作和特定业务功能
 * 如租约的添加、修改、查询和状态更新等，并维护租约与房源状态的关联
 */
@Service
public class RentalService {

    @Resource
    RentalMapper rentalMapper; // 租约数据访问层接口

    @Resource
    HouseService houseService; // 房源服务，用于更新房源状态

    /**
     * 添加租约信息
     * 创建新的租约记录，并根据租约状态更新关联房源的状态
     *
     * @param rental 包含租约详细信息的实体对象
     */
    public void add(Rental rental) {
        rentalMapper.insert(rental);
        
        // 如果是预订状态，将房源状态更新为已租
        if ("pending".equals(rental.getStatus()) || "ongoing".equals(rental.getStatus())) {
            updateHouseStatus(rental.getHouseId(), "rented");
        }
    }

    /**
     * 修改租约信息
     * 更新现有租约的基本信息
     *
     * @param rental 包含更新后租约信息的实体对象
     */
    public void update(Rental rental) {
        rentalMapper.updateById(rental);
    }

    /**
     * 删除租约信息
     * 根据ID删除指定租约
     *
     * @param id 要删除的租约ID
     */
    public void deleteByid(Integer id) {
        rentalMapper.deleteById(id);
    }

    /**
     * 批量删除租约
     * 循环删除多个租约记录
     *
     * @param rentalList 包含多个租约实体的列表
     */
    public void deleteBatch(List<Rental> rentalList) {
        for (Rental rental : rentalList) {  //iter 遍历
            this.deleteByid(rental.getId());
        }
    }


    /**
     * 查询所有租约
     * 获取系统中所有租约的列表
     * 
     * @return 所有租约的列表
     */
    public List<Rental> selectAll() {
        return rentalMapper.selectAll(null);
    }

    /**
     * 分页查询租约
     * 支持分页和条件查询，用于管理界面展示租约列表
     *
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param rental 查询条件封装对象
     * @return 返回包含分页信息和租约数据的PageInfo对象
     */
    public PageInfo<Rental> selectPage(Integer pageNum, Integer pageSize, Rental rental) {
        // 测试是否拿到当前用户信息
        //Account currentUser = TokenUtils.getCurrentUser();

        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<Rental> list = rentalMapper.selectAll(rental);
        return PageInfo.of(list);
    }

    /**
     * 根据用户ID查询租赁记录
     * 获取指定用户的所有租约
     * 
     * @param userId 用户ID
     * @return 该用户的所有租约列表
     */
    public List<Rental> listByUserId(Integer userId) {
        return rentalMapper.selectByUserId(userId);
    }

    /**
     * 更新租约状态
     * 修改租约的状态，并根据新状态更新相关房源的状态
     * 
     * @param id 租约ID
     * @param status 新状态值，如"confirmed"、"ongoing"、"completed"等
     */
    public void updateStatus(Integer id, String status) {
        // 获取原始租约信息
        Rental originalRental = rentalMapper.selectById(id);
        if (originalRental == null) {
            return;
        }
        
        // 更新租约状态
        Rental rental = new Rental();
        rental.setId(id);
        rental.setStatus(status);
        rental.setUpdatedAt(LocalDateTime.now());
        rentalMapper.updateById(rental);
        
        // 根据状态变更更新房源状态
        if ("completed".equals(status) || "canceled".equals(status)) {
            // 检查该房源是否还有其他有效租约
            List<Rental> activeRentals = getActiveRentalsByHouseId(originalRental.getHouseId());
            if (activeRentals.isEmpty()) {
                // 如果没有其他有效租约，将房源状态更新为可租
                updateHouseStatus(originalRental.getHouseId(), "available");
            }
        } else if ("ongoing".equals(status)) {
            // 如果状态变为进行中，确保房源状态为已租
            updateHouseStatus(originalRental.getHouseId(), "rented");
        }
    }
    
    /**
     * 更新房源状态
     * 调用房源服务更新指定房源的状态
     * 
     * @param houseId 房源ID
     * @param status 新状态值，如"available"、"rented"等
     */
    private void updateHouseStatus(Integer houseId, String status) {
        houseService.updateStatus(houseId, status);
    }

    /**
     * 根据房源ID查询当前有效的租约
     * 获取指定房源的所有未完成租约
     * 
     * @param houseId 房源ID
     * @return 该房源的所有有效租约列表
     */
    public List<Rental> getActiveRentalsByHouseId(Integer houseId) {
        return rentalMapper.selectActiveByHouseId(houseId);
    }

    /**
     * 获取租约总数
     * 用于统计和展示在仪表盘
     * 
     * @return 系统中的总租约数量
     */
    public long getTotalRentals() {
        return rentalMapper.getTotalCount();
    }

    /**
     * 获取本月新增租约数
     * 用于统计和展示在仪表盘
     * 
     * @return 本月新创建的租约数量
     */
    public long getNewRentalsThisMonth() {
        return rentalMapper.getNewRentalsThisMonth();
    }

    /**
     * 获取本周新增租约数
     * 用于统计和展示在仪表盘
     * 
     * @return 本周新创建的租约数量
     */
    public long getNewRentalsThisWeek() {
        return rentalMapper.getNewRentalsThisWeek();
    }
}
