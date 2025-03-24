package com.house.controller;

import com.house.common.Result;
import com.house.entity.Rental;
import com.house.service.RentalService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;



@RestController
@RequestMapping("/rental")
public class RentalController {

    @Resource
    private RentalService rentalService;

    /**
     * 添加信息
     *
     * @param rental
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Rental rental) { //RequestBody 表示接收json格式的数据
        rentalService.add(rental);
        return Result.success();
    }

    /**
     * 修改信息
     *
     * @param rental
     * @return
     */
    @PutMapping("/update")
    public Result updata(@RequestBody Rental rental) { //RequestBody 表示接收json格式的数据
        rentalService.update(rental);
        return Result.success();
    }

    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //PathVariable 表示接收url中的参数
        rentalService.deleteByid(id);
        return Result.success();
    }


    /**
     * 批量删除
     *
     * @return
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Rental> listRental) {  //RequestBody 表示接收json数组
        rentalService.deleteBatch(listRental);
        return Result.success();
    }


    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Rental> rentalList = rentalService.selectAll();
        return Result.success(rentalList);
    }

    /**
     * 分页查询
     * pageNum 当前页码
     * pageSize 每页显示条数
     * pageInfo 包含分页信息的对象
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Rental rental) {
        PageInfo<Rental> pageInfo = rentalService.selectPage(pageNum, pageSize, rental);
        return Result.success(pageInfo);    // 返回分页对象
    }


    /**
     * 根据用户id查询租赁记录
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public Result list(@RequestParam Integer userId) {
        List<Rental> rentalList = rentalService.listByUserId(userId);
        return Result.success(rentalList);
    }

    /**
     * 更新租约状态
     * @param id 租约ID
     * @param status 新状态
     * @return
     */
    @PutMapping("/updateStatus/{id}")
    public Result updateStatus(@PathVariable Integer id, @RequestParam String status) {
        rentalService.updateStatus(id, status);
        return Result.success();
    }

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
