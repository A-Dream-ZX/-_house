package com.house.mapper;


import com.house.entity.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<Notice> selectAll(Notice notice);

    void insert(Notice notice);

    void updateById(Notice notice);

    @Delete("delete from t_notice where id = #{id}")
    void deleteById(Integer id);

}
