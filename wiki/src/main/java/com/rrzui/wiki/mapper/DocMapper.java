package com.rrzui.wiki.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrzui.wiki.entity.Doc;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface DocMapper extends BaseMapper<Doc> {

    @Update("update doc set view_count = view_count+1 where id = #{id}")
    public int increaseViewCount(@Param("id") int id);
}