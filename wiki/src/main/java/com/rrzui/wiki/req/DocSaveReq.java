package com.rrzui.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-15 09:39
 **/

@Data
public class DocSaveReq {
    private int id;
    @NotNull(message = "【电子书】不能为空")
    private long ebook_id;//电子书id
    @NotNull(message = "【父文档】不能为空")
    private long parent;//父id
    @NotNull(message = "【名称】不能为空")
    private String name;//名称
    @NotNull(message = "【排序】不能为空")
    private int sort;//顺序
    private int view_count;//阅读数
    private int vote_count;//点赞数
}