package com.rrzui.wiki.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-22 15:10
 **/

@Data
public class EbookSnapshot {
    private Long id;
    private Long ebook_id;
    private Date date;
    private Integer view_count;
    private Integer vote_count;
    private Integer view_increase;
    private Integer vote_increase;
}