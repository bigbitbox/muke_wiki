package com.rrzui.wiki.entity;

import lombok.Data;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-15 09:36
 **/

@Data
public class Doc {
    private int id;
    private long ebook_id;//电子书id
    private long parent;//父id
    private String name;//名称
    private int sort;//顺序
    private int view_count;//阅读数
    private int vote_count;//点赞数
}
