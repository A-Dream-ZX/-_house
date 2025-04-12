package com.house.controller;

import com.house.common.Result;
import com.house.service.FavoriteService;
import com.house.service.HouseService;
import com.house.service.RentalService;
import com.house.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计数据控制器
 * 
 * 处理与系统统计数据相关的HTTP请求，提供系统运营数据和关键指标
 * 主要用于管理员仪表盘，展示系统概览和业务状况
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private HouseService houseService; // 房源服务，用于获取房源统计数据
    
    @Resource
    private UserService userService; // 用户服务，用于获取用户统计数据
    
    @Resource
    private RentalService rentalService; // 租约服务，用于获取租约统计数据
    
    @Resource
    private FavoriteService favoriteService; // 收藏服务，用于获取收藏统计数据

    /**
     * 获取系统概览统计数据
     * 汇总系统关键指标，包括总量和增长数据
     * 
     * @return 包含多个统计指标的数据集合
     */
    @GetMapping("/overview")
    public Result getOverview() {
        Map<String, Object> data = new HashMap<>();
        
        // 获取总房源数
        data.put("totalHouses", houseService.getTotalHouses());
        
        // 获取总用户数
        data.put("totalUsers", userService.getTotalUsers());
        
        // 获取总收藏数
        data.put("totalFavorites", favoriteService.getTotalFavorites());
        
        // 获取总租约数
        data.put("totalRentals", rentalService.getTotalRentals());
        
        // 获取本月新增用户数
        data.put("newUsersThisMonth", userService.getNewUsersThisMonth());
        
        // 获取本月新增租约数
        data.put("newRentalsThisMonth", rentalService.getNewRentalsThisMonth());

        return Result.success(data);
    }
}