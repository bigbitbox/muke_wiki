package com.rrzui.wiki.req;

import lombok.Data;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-15 16:40
 **/

@Data
public class ContentQueryReq extends PageReq{
    private int id;
    private String content;//内容
}