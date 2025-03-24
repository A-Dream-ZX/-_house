package com.house.controller;

import com.house.common.Result;
import com.house.entity.User;
import com.house.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 添加信息
     *
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) { //RequestBody 表示接收json格式的数据
        userService.add(user);
        return Result.success();
    }

    /**
     * 修改信息
     *
     * @param user
     * @return
     */
    @PutMapping("/update")
    public Result updata(@RequestBody User user) { //RequestBody 表示接收json格式的数据
        userService.updata(user);
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
        userService.deleteByid(id);
        return Result.success();
    }


    /**
     * 批量删除
     *
     * @return
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<User> listUser) {  //RequestBody 表示接收json数组
        userService.deleteBatch(listUser);
        return Result.success();
    }


    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> userList = userService.selectAll();
        return Result.success(userList);
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
                             User user) {
        PageInfo<User> pageInfo = userService.selectPage(pageNum, pageSize, user);
        return Result.success(pageInfo);    // 返回分页对象
    }

    /**
     * 获取用户注册趋势统计
     */
    @GetMapping("/stats")
    public Result getUserStats() {
        return Result.success(userService.getUserStats());
    }
}
