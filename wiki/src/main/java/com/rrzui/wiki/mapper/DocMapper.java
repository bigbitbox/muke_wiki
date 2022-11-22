package com.rrzui.wiki.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrzui.wiki.entity.Doc;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface DocMapper extends BaseMapper<Doc> {

    @Update("update doc set view_count = view_count+1 where id = #{id}")
    public int increaseViewCount(@Param("id") int id);

    @Update("update doc set vote_count = vote_count+1 where id = #{id}")
    public int increaseVoteCount(@Param("id") int id);

    @Update("update ebook t1, (select ebook_id, count(1) doc_count, sum(view_count) view_count, sum(vote_count) vote_count from doc group by ebook_id) t2\n" +
            "set t1.doc_count = t2.doc_count, t1.view_count = t2.view_count, t1.vote_count = t2.vote_count\n" +
            "where t1.id = t2.ebook_id")
    public int updateEbookInfo();
}