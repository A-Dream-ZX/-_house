package com.house.controller;

import com.house.common.Result;
import com.house.entity.Rental;
import com.house.service.RentalService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 租约管理控制器
 * <p>
 * 处理与租约相关的HTTP请求，包括租约的创建、修改、查询和状态更新等操作
 * 提供租赁合同全生命周期的管理功能
 */
@RestController
@RequestMapping("/rental")
public class RentalController {

    @Resource
    private RentalService rentalService; // 租约服务

    /**
     * 添加租约信息
     * 创建新的租约记录
     *
     * @param rental 包含租约详细信息的实体对象
     * @return 添加成功返回成功消息，失败抛出异常
     */
    @PostMapping("/add")
    public Result add(@RequestBody Rental rental) { //RequestBody 表示接收json格式的数据
        rentalService.add(rental);
        return Result.success();
    }

    /**
     * 修改租约信息
     * 更新现有租约的基本信息
     *
     * @param rental 包含更新后租约信息的实体对象
     * @return 修改成功返回成功消息，失败抛出异常
     */
    @PutMapping("/update")
    public Result updata(@RequestBody Rental rental) { //RequestBody 表示接收json格式的数据
        rentalService.update(rental);
        return Result.success();
    }

    /**
     * 删除租约信息
     * 根据ID删除指定租约
     *
     * @param id 要删除的租约ID
     * @return 删除成功返回成功消息，失败抛出异常
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //PathVariable 表示接收url中的参数
        rentalService.deleteByid(id);
        return Result.success();
    }


    /**
     * 批量删除租约
     * 同时删除多个租约记录
     *
     * @param listRental 包含多个租约ID的列表
     * @return 删除成功返回成功消息，失败抛出异常
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Rental> listRental) {  //RequestBody 表示接收json数组
        rentalService.deleteBatch(listRental);
        return Result.success();
    }


    /**
     * 查询所有租约
     * 获取系统中所有租约的列表
     *
     * @return 返回所有租约的集合
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Rental> rentalList = rentalService.selectAll();
        return Result.success(rentalList);
    }

    /**
     * 分页查询租约
     * 支持分页和条件查询，用于管理界面展示租约列表
     *
     * @param pageNum  当前页码，默认为1
     * @param pageSize 每页显示条数，默认为5
     * @param rental   查询条件封装对象
     * @return 返回包含分页信息和租约数据的PageInfo对象
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Rental rental) {
        PageInfo<Rental> pageInfo = rentalService.selectPage(pageNum, pageSize, rental);
        return Result.success(pageInfo);    // 返回分页对象
    }


    /**
     * 根据用户ID查询租赁记录
     * 获取指定用户的所有租约
     *
     * @param userId 用户ID
     * @return 返回该用户的所有租约列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Integer userId) {
        List<Rental> rentalList = rentalService.listByUserId(userId);
        return Result.success(rentalList);
    }

    /**
     * 更新租约状态
     * 修改租约的状态，如确认、拒绝或完成租约
     *
     * @param id     租约ID
     * @param status 新状态值，如"confirmed"、"rejected"等
     * @return 更新成功返回成功消息，失败抛出异常
     */
    @PutMapping("/updateStatus/{id}")
    public Result updateStatus(@PathVariable Integer id, @RequestParam String status) {
        rentalService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 房屋预订接口
     * 创建新的租约申请，设置初始状态为待确认
     *
     * @param rental 包含预订信息的租约对象
     * @return 预订成功返回成功消息，失败返回错误信息
     */
    @PostMapping("/book")
    public Result book(@RequestBody Rental rental) {
        try {
            // 设置创建时间
            rental.setCreatedAt(LocalDateTime.now());
            // 设置初始状态为待确认
            rental.setStatus("pending");

            // 保存租约信息
            rentalService.add(rental);

            return Result.success();
        } catch (Exception e) {
            return Result.error("500", "预订失败：" + e.getMessage());
        }
    }
}
