package com.rrzui.wiki.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @program: 粤嵌项目
 * @description: 统计响应
 * @author: Ethercat
 * @create: 2022-11-22 23:14
 **/

@Data
public class StatisticResp {
    @JsonFormat(pattern = "MM-dd",timezone = "GMT+8")
    private Date date;
    private Integer view_count;
    private Integer vote_count;
    private Integer view_increase;
    private Integer vote_increase;
}