package com.house.controller;

import com.house.common.Result;
import com.house.entity.Favorite;
import com.house.service.FavoriteService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Resource
    FavoriteService favoriteService;

    @PostMapping("/add")
    public Result add(@RequestBody Favorite favorite) {
        favoriteService.add(favorite);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Favorite favorite) {
        favoriteService.update(favorite);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        favoriteService.deleteByid(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Favorite> favorites) {
        favoriteService.deleteBatch(favorites);
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "5") Integer pageSize,
                           Favorite favorite) {
        PageInfo<Favorite> pageInfo = favoriteService.selectPage(pageNum, pageSize, favorite);
        return Result.success(pageInfo);
    }

    @GetMapping("/list")
    public Result list(@RequestParam Integer userId) {
        List<Favorite> favoriteList = favoriteService.listByUserId(userId);
        return Result.success(favoriteList);
    }
}
