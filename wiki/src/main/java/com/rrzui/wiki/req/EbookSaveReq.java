package com.rrzui.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EbookSaveReq extends PageReq{
    private int id;
    @NotNull(message = "【名称】 不能为空")
    private String name;
    private long  category1_id;  //分类1
    private long category2_id;  //分类2
    private String  description;//描述
    private String cover ;     //封面
    private int doc_count;     //文档数
    private int view_count;    //阅读数
    private int vote_count;    //点赞数
}
