package com.house.controller;

import com.house.common.Result;
import com.house.entity.Admin;
import com.house.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 添加信息
     *
     * @param admin
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) { //RequestBody 表示接收json格式的数据
        adminService.add(admin);
        return Result.success();
    }

    /**
     * 修改信息
     *
     * @param admin
     * @return
     */
    @PutMapping("/update")
    public Result updata(@RequestBody Admin admin) { //RequestBody 表示接收json格式的数据
        adminService.update(admin);
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
        adminService.deleteByid(id);
        return Result.success();
    }


    /**
     * 批量删除
     *
     * @return
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Admin> listAdmin) {  //RequestBody 表示接收json数组
        adminService.deleteBatch(listAdmin);
        return Result.success();
    }


    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Admin> adminList = adminService.selectAll();
        return Result.success(adminList);
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
                             Admin admin) {
        PageInfo<Admin> pageInfo = adminService.selectPage(pageNum, pageSize, admin);
        return Result.success(pageInfo);    // 返回分页对象
    }
}
