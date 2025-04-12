package com.house.service;

import cn.hutool.core.date.DateUtil;
import com.house.entity.Notice;
import com.house.mapper.NoticeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告服务类
 * 
 * 提供公告相关的业务逻辑处理，包括添加、修改、删除和查询公告
 * 主要为控制层提供服务，处理公告的增删改查操作
 */
@Service
public class NoticeService {

    @Resource
    NoticeMapper noticeMapper; // 注入公告数据访问层接口


    /**
     * 添加公告
     * 
     * 创建新的公告记录，系统会自动设置当前时间为公告发布时间
     *
     * @param notice 待添加的公告信息对象，包含标题和内容
     */
    public void add(Notice notice) {
        // 设置公告发布时间为当前时间
        notice.setTime(DateUtil.now());

        // 调用Mapper层插入数据
        noticeMapper.insert(notice);
    }

    /**
     * 修改公告信息
     * 
     * 更新已有公告的内容，根据公告ID进行更新
     *
     * @param notice 包含更新内容的公告对象，必须包含ID
     */
    public void update(Notice notice) {
        noticeMapper.updateById(notice);
    }

    /**
     * 删除公告
     * 
     * 根据ID删除指定公告记录
     *
     * @param id 要删除的公告ID
     */
    public void deleteByid(Integer id) {
        noticeMapper.deleteById(id);
    }


    /**
     * 查询所有公告
     * 
     * 获取系统中的所有公告列表，不分页
     *
     * @return 所有公告的列表
     */
    public List<Notice> selectAll() {
        // 传入null表示查询所有公告，不设置筛选条件
        return noticeMapper.selectAll(null);
    }


    /**
     * 分页查询公告
     * 
     * 根据条件分页查询公告信息，支持按标题、内容和时间筛选
     *
     * @param pageNum 当前页码，从1开始
     * @param pageSize 每页显示的记录数
     * @param notice 查询条件，可包含标题、内容和时间属性
     * @return 分页结果，包含当前页数据和分页信息
     */
    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize, Notice notice) {
        // 开启分页功能，设置页码和每页大小
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<Notice> list = noticeMapper.selectAll(notice);
        // 将查询结果封装为PageInfo对象返回
        return PageInfo.of(list);
    }

}
