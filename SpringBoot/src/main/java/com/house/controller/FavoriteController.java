package com.house.controller;

import com.house.common.Result;
import com.house.entity.Favorite;
import com.house.service.FavoriteService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏管理控制器
 * 
 * 处理与用户收藏相关的HTTP请求，提供收藏的增删改查功能
 * 允许用户对感兴趣的房源进行收藏和管理
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Resource
    FavoriteService favoriteService; // 收藏服务

    /**
     * 添加收藏
     * 创建新的收藏记录，记录用户对房源的收藏行为
     * 
     * @param favorite 包含用户ID和房源ID的收藏对象
     * @return 添加成功返回成功消息，失败抛出异常
     */
    @PostMapping("/add")
    public Result add(@RequestBody Favorite favorite) {
        favoriteService.add(favorite);
        return Result.success();
    }

    /**
     * 修改收藏信息
     * 更新现有收藏的信息，如备注等
     * 
     * @param favorite 包含更新后收藏信息的实体对象
     * @return 修改成功返回成功消息，失败抛出异常
     */
    @PutMapping("/update")
    public Result update(@RequestBody Favorite favorite) {
        favoriteService.update(favorite);
        return Result.success();
    }

    /**
     * 删除收藏
     * 根据ID删除指定收藏记录
     * 
     * @param id 要删除的收藏ID
     * @return 删除成功返回成功消息，失败抛出异常
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        favoriteService.deleteByid(id);
        return Result.success();
    }

    /**
     * 批量删除收藏
     * 同时删除多个收藏记录
     * 
     * @param favorites 包含多个收藏ID的列表
     * @return 删除成功返回成功消息，失败抛出异常
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Favorite> favorites) {
        favoriteService.deleteBatch(favorites);
        return Result.success();
    }

    /**
     * 分页查询收藏
     * 支持分页和条件查询，用于管理界面展示收藏列表
     * 
     * @param pageNum 当前页码，默认为1
     * @param pageSize 每页显示条数，默认为5
     * @param favorite 查询条件封装对象
     * @return 返回包含分页信息和收藏数据的PageInfo对象
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "5") Integer pageSize,
                           Favorite favorite) {
        PageInfo<Favorite> pageInfo = favoriteService.selectPage(pageNum, pageSize, favorite);
        return Result.success(pageInfo);
    }

    /**
     * 查询用户收藏列表
     * 获取指定用户的所有收藏记录，包含房源详情
     * 
     * @param userId 用户ID
     * @return 返回包含房源详情的收藏列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Integer userId) {
        List<Favorite> favoriteList = favoriteService.listByUserId(userId);
        return Result.success(favoriteList);
    }
}
