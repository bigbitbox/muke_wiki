package com.rrzui.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-15 16:40
 **/

@Data
public class ContentSaveReq {
    private int id;
    @NotNull(message = "【内容】不能为空")
    private String content;//内容
}