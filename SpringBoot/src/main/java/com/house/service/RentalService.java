package com.house.service;

import com.house.entity.Rental;
import com.house.mapper.RentalMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalService {

    @Resource
    RentalMapper rentalMapper;

    @Resource
    HouseService houseService;

    /**
     * 添加信息
     *
     * @param rental
     */
    public void add(Rental rental) {
        rentalMapper.insert(rental);
        
        // 如果是预订状态，将房源状态更新为已租
        if ("pending".equals(rental.getStatus()) || "ongoing".equals(rental.getStatus())) {
            updateHouseStatus(rental.getHouseId(), "rented");
        }
    }

    /**
     * 修改信息
     *
     * @param rental
     */
    public void update(Rental rental) {
        rentalMapper.updateById(rental);
    }

    /**
     * 删除信息
     *
     * @param id
     */
    public void deleteByid(Integer id) {
        rentalMapper.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @return
     */
    public void deleteBatch(List<Rental> rentalList) {
        for (Rental rental : rentalList) {  //iter 遍历
            this.deleteByid(rental.getId());
        }
    }



    public List<Rental> selectAll() {
        return rentalMapper.selectAll(null);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     */
    public PageInfo<Rental> selectPage(Integer pageNum, Integer pageSize, Rental rental) {
        // 测试是否拿到当前用户信息
        //Account currentUser = TokenUtils.getCurrentUser();

        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<Rental> list = rentalMapper.selectAll(rental);
        return PageInfo.of(list);
    }


    public List<Rental> listByUserId(Integer userId) {
        return rentalMapper.selectByUserId(userId);
    }

    /**
     * 更新租约状态
     * @param id
     * @param status
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
     * @param houseId
     * @param status
     */
    private void updateHouseStatus(Integer houseId, String status) {
        houseService.updateStatus(houseId, status);
    }

    /**
     * 根据房源ID查询当前有效的租约
     * @param houseId
     * @return
     */
    public List<Rental> getActiveRentalsByHouseId(Integer houseId) {
        return rentalMapper.selectActiveByHouseId(houseId);
    }

    public long getTotalRentals() {
        return rentalMapper.getTotalCount();
    }

    public long getNewRentalsThisMonth() {
        return rentalMapper.getNewRentalsThisMonth();
    }

    public long getNewRentalsThisWeek() {
        return rentalMapper.getNewRentalsThisWeek();
    }
}
