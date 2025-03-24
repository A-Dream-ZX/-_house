package com.house.controller;

import com.house.common.Result;
import com.house.entity.Notice;
import com.house.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 添加信息
     *
     * @param notice
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) { //RequestBody 表示接收json格式的数据
        noticeService.add(notice);
        return Result.success();
    }

    /**
     * 修改信息
     *
     * @param notice
     * @return
     */
    @PutMapping("/update")
    public Result updata(@RequestBody Notice notice) { //RequestBody 表示接收json格式的数据
        noticeService.update(notice);
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
        noticeService.deleteByid(id);
        return Result.success();
    }





    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Notice> noticeList = noticeService.selectAll();
        return Result.success(noticeList);
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
                             Notice notice) {
        PageInfo<Notice> pageInfo = noticeService.selectPage(pageNum, pageSize, notice);
        return Result.success(pageInfo);    // 返回分页对象
    }
}
