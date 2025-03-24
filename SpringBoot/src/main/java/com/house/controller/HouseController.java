package com.house.controller;

import com.house.common.Result;
import com.house.entity.House;
import com.house.service.HouseService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/house")
public class HouseController {

    @Resource
    private HouseService houseService;

    /**
     * 添加信息
     *
     * @param house
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody House house) { //RequestBody 表示接收json格式的数据
        houseService.add(house);
        return Result.success();
    }

    /**
     * 修改信息
     *
     * @param house
     * @return
     */
    @PutMapping("/update")
    public Result updata(@RequestBody House house) { //RequestBody 表示接收json格式的数据
        houseService.update(house);
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
        houseService.deleteByid(id);
        return Result.success();
    }


    /**
     * 批量删除
     *
     * @return
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<House> listHouse) {  //RequestBody 表示接收json数组
        houseService.deleteBatch(listHouse);
        return Result.success();
    }


    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/selectAll")
    public Result selectAll(House house) {  // 修改方法签名，接收查询参数
        List<House> houseList = houseService.selectAll(house);  // 传入查询参数
        return Result.success(houseList);
    }

    /**
     * 分页查询
     * pageNum 当前页码
     * pageSize 每页显示条数
     * pageInfo 包含分页信息的对象
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
     * 根据价格区间查询房源
     */

    /**
     * 预订房源
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
     */
    @GetMapping("/available")
    public Result available(House house) {
        if (house == null) {
            house = new House();
        }
        house.setStatus("available");
        List<House> houseList = houseService.selectAll(house);
        return Result.success(houseList);
    }
}
