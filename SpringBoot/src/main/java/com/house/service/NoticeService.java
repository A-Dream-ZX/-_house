package com.house.service;

import cn.hutool.core.date.DateUtil;
import com.house.entity.Notice;
import com.house.mapper.NoticeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Resource
    NoticeMapper noticeMapper;


    /**
     * 添加信息
     *
     * @param notice
     */
    public void add(Notice notice) {
        notice.setTime(DateUtil.now());

        noticeMapper.insert(notice);
    }

    /**
     * 修改信息
     *
     * @param notice
     */
    public void update(Notice notice) {
        noticeMapper.updateById(notice);
    }

    /**
     * 删除信息
     *
     * @param id
     */
    public void deleteByid(Integer id) {
        noticeMapper.deleteById(id);
    }



    /**
     * 查询所有
     *
     * @return
     */
    public List<Notice> selectAll() {
        return noticeMapper.selectAll(null);
    }



    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     */
    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize, Notice notice) {
        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }


}
