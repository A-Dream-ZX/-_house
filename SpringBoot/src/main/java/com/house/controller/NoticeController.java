package com.house.controller;

import com.house.common.Result;
import com.house.entity.Notice;
import com.house.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 公告管理控制器
 * 
 * 处理与系统公告相关的HTTP请求，提供公告的增删改查功能
 * 主要由管理员使用，用于向用户发布和管理系统公告
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService; // 公告服务

    /**
     * 添加公告
     * 创建新的公告记录，仅管理员可操作
     *
     * @param notice 包含公告详细信息的实体对象
     * @return 添加成功返回成功消息，失败抛出异常
     */
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) { //RequestBody 表示接收json格式的数据
        noticeService.add(notice);
        return Result.success();
    }

    /**
     * 修改公告
     * 更新现有公告的内容或标题，仅管理员可操作
     *
     * @param notice 包含更新后公告信息的实体对象
     * @return 修改成功返回成功消息，失败抛出异常
     */
    @PutMapping("/update")
    public Result updata(@RequestBody Notice notice) { //RequestBody 表示接收json格式的数据
        noticeService.update(notice);
        return Result.success();
    }

    /**
     * 删除公告
     * 根据ID删除指定公告，仅管理员可操作
     *
     * @param id 要删除的公告ID
     * @return 删除成功返回成功消息，失败抛出异常
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) { //PathVariable 表示接收url中的参数
        noticeService.deleteByid(id);
        return Result.success();
    }



    /**
     * 查询所有公告
     * 获取系统中所有公告的列表，供前端展示
     *
     * @return 返回所有公告的集合
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Notice> noticeList = noticeService.selectAll();
        return Result.success(noticeList);
    }

    /**
     * 分页查询公告
     * 支持分页和条件查询，用于管理界面展示公告列表
     * 
     * @param pageNum 当前页码，默认为1
     * @param pageSize 每页显示条数，默认为5
     * @param notice 查询条件封装对象
     * @return 返回包含分页信息和公告数据的PageInfo对象
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Notice notice) {
        PageInfo<Notice> pageInfo = noticeService.selectPage(pageNum, pageSize, notice);
        return Result.success(pageInfo);    // 返回分页对象
    }
}
