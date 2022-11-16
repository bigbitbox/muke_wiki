package com.rrzui.wiki.resp;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-15 16:41
 **/

@Data
public class ContentQueryResp {
    private int id;
    private String content;//内容
}