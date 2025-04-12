package com.house.controller;

import com.house.common.Result;
import com.house.entity.Admin;
import com.house.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 管理员管理控制器
 * 
 * 处理与管理员账号相关的HTTP请求，提供管理员的增删改查功能
 * 仅限超级管理员访问，用于管理其他管理员账号
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService; // 管理员服务

    /**
     * 添加管理员
     * 创建新的管理员账号，通常由超级管理员操作
     *
     * @param admin 包含管理员详细信息的实体对象
     * @return 添加成功返回成功消息，失败抛出异常
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) { //RequestBody 表示接收json格式的数据
        adminService.add(admin);
        return Result.success();
    }

    /**
     * 修改管理员信息
     * 更新现有管理员的基本信息
     *
     * @param admin 包含更新后管理员信息的实体对象
     * @return 修改成功返回成功消息，失败抛出异常
     */
    @PutMapping("/update")
    public Result updata(@RequestBody Admin admin) { //RequestBody 表示接收json格式的数据
        adminService.update(admin);
        return Result.success();
    }

    /**
     * 删除管理员
     * 根据ID删除指定管理员账号
     *
     * @param id 要删除的管理员ID
     * @return 删除成功返回成功消息，失败抛出异常
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //PathVariable 表示接收url中的参数
        adminService.deleteByid(id);
        return Result.success();
    }


    /**
     * 批量删除管理员
     * 同时删除多个管理员账号
     *
     * @param listAdmin 包含多个管理员ID的列表
     * @return 删除成功返回成功消息，失败抛出异常
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Admin> listAdmin) {  //RequestBody 表示接收json数组
        adminService.deleteBatch(listAdmin);
        return Result.success();
    }


    /**
     * 查询所有管理员
     * 获取系统中所有管理员的列表
     *
     * @return 返回所有管理员的集合
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Admin> adminList = adminService.selectAll();
        return Result.success(adminList);
    }

    /**
     * 分页查询管理员
     * 支持分页和条件查询，用于管理界面展示管理员列表
     *
     * @param pageNum 当前页码，默认为1
     * @param pageSize 每页显示条数，默认为5
     * @param admin 查询条件封装对象
     * @return 返回包含分页信息和管理员数据的PageInfo对象
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Admin admin) {
        PageInfo<Admin> pageInfo = adminService.selectPage(pageNum, pageSize, admin);
        return Result.success(pageInfo);    // 返回分页对象
    }
}
