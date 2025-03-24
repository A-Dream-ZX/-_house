package com.house.controller;

import com.house.common.Result;
import com.house.entity.Account;
import com.house.entity.User;
import com.house.service.AdminService;
import com.house.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Resource
    AdminService adminService;

    @Resource
    UserService userService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("Hello World");
    }


    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if ("admin".equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else if ("user".equals(account.getRole())) {
            dbAccount = userService.login(account);

        } else {
            return Result.error("-1", "角色不存在");
        }
        return Result.success(dbAccount);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }

    /**
     * 修改密码
     *
     * @param account
     * @return
     */

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("admin".equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        if ("user".equals(account.getRole())) {
            userService.updatePassword(account);
        }

        return Result.success();
    }


}
