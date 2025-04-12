package com.house.controller;

import com.house.common.Result;
import com.house.entity.House;
import com.house.service.HouseService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 房源管理控制器
 * 
 * 处理所有与房源相关的HTTP请求，包括添加、修改、查询和删除房源信息
 * 提供房源的CRUD操作和特定业务逻辑，如房源预订、可用房源查询等
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Resource
    private HouseService houseService; // 房源服务

    /**
     * 添加房源信息
     * 创建新的房源记录，通常由管理员操作
     *
     * @param house 包含房源详细信息的实体对象
     * @return 添加成功返回成功消息，失败抛出异常
     */
    @PostMapping("/add")
    public Result add(@RequestBody House house) { //RequestBody 表示接收json格式的数据
        houseService.add(house);
        return Result.success();
    }

    /**
     * 修改房源信息
     * 更新现有房源的信息，通常由管理员操作
     *
     * @param house 包含更新后房源信息的实体对象，必须包含ID
     * @return 修改成功返回成功消息，失败抛出异常
     */
    @PutMapping("/update")
    public Result updata(@RequestBody House house) { //RequestBody 表示接收json格式的数据
        houseService.update(house);
        return Result.success();
    }

    /**
     * 删除房源信息
     * 根据ID删除指定房源，通常由管理员操作
     *
     * @param id 要删除的房源ID
     * @return 删除成功返回成功消息，失败抛出异常
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //PathVariable 表示接收url中的参数
        houseService.deleteByid(id);
        return Result.success();
    }

    /**
     * 批量删除房源
     * 同时删除多个房源记录，通常用于管理界面的批量操作
     *
     * @param listHouse 包含多个房源ID的列表
     * @return 删除成功返回成功消息，失败抛出异常
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<House> listHouse) {  //RequestBody 表示接收json数组
        houseService.deleteBatch(listHouse);
        return Result.success();
    }

    /**
     * 查询所有房源
     * 支持条件查询，可根据传入的房源对象属性进行筛选
     *
     * @param house 查询条件封装对象，属性值为null则不作为筛选条件
     * @return 返回符合条件的房源列表
     */
    @GetMapping("/selectAll")
    public Result selectAll(House house) {  // 修改方法签名，接收查询参数
        List<House> houseList = houseService.selectAll(house);  // 传入查询参数
        return Result.success(houseList);
    }

    /**
     * 分页查询房源
     * 支持分页和条件查询，主要用于前端展示房源列表
     *
     * @param pageNum 当前页码，默认为1
     * @param pageSize 每页显示条数，默认为12
     * @param house 查询条件封装对象
     * @return 返回包含分页信息和房源数据的PageInfo对象
     */
    @GetMapping("/selectPage")
    public Result list(@RequestParam(defaultValue = "1") Integer pageNum,
                     @RequestParam(defaultValue = "12") Integer pageSize,
                     House house) {
        PageInfo<House> pageInfo = houseService.selectPage(pageNum, pageSize, house);
        return Result.success(pageInfo);    // 返回分页对象
    }

    /**
     * 根据ID查询房源详情
     * 获取指定房源的完整信息，用于展示房源详情页
     *
     * @param id 房源ID
     * @return 返回指定房源的详细信息，不存在则返回错误消息
     */
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Integer id) {
        House house = houseService.selectById(id);
        if (house == null) {
            return Result.error("房源不存在");
        }
        return Result.success(house);
    }

    /**
     * 预订房源
     * 将房源状态更改为已租，表示房源已被预订
     *
     * @param params 包含房源ID的请求参数
     * @return 预订成功返回成功消息，失败返回错误信息
     */
    @PostMapping("/book")
    public Result book(@RequestBody Map<String, Integer> params) {
        Integer houseId = params.get("houseId");
        if (houseId == null) {
            return Result.error("房源ID不能为空");
        }
        return houseService.bookHouse(houseId);
    }

    /**
     * 查询所有可租房源
     * 查询状态为"available"的房源列表，支持条件筛选
     *
     * @param house 查询条件封装对象
     * @return 返回所有可租房源列表
     */
    @GetMapping("/available")
    public Result available(House house) {
        if (house == null) {
            house = new House();
        }
        house.setStatus("available"); // 设置状态为可租
        List<House> houseList = houseService.selectAll(house);
        return Result.success(houseList);
    }
}
