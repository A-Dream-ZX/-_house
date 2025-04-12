package com.house.mapper;

import com.house.entity.Notice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 公告信息数据访问层接口
 * 负责处理公告相关的数据库操作
 */
@Mapper
@Repository
public interface NoticeMapper {

    /**
     * 查询所有公告信息
     * 可根据标题、内容和时间进行模糊搜索
     * 
     * @param notice 包含查询条件的公告对象，可包含title、content、time属性
     * @return 符合条件的公告列表
     */
    List<Notice> selectAll(Notice notice);

    /**
     * 添加新公告
     * 
     * @param notice 要添加的公告对象，应包含title、content和time属性
     * @return 影响的记录数
     */
    int insert(Notice notice);

    /**
     * 根据ID更新公告信息
     * 
     * @param notice 包含更新内容的公告对象，id属性必须指定
     * @return 影响的记录数
     */
    int updateById(Notice notice);

    /**
     * 根据ID删除公告
     * 
     * @param id 要删除的公告ID
     */
    @Delete("delete from t_notice where id = #{id}")
    void deleteById(Integer id);

}
