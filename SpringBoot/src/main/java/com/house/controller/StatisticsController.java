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

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private HouseService houseService;
    @Resource
    private UserService userService;
    @Resource
    private RentalService rentalService;
    @Resource
    private FavoriteService favoriteService;

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