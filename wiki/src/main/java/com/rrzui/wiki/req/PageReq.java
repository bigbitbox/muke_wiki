package com.rrzui.wiki.req;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class PageReq {
    @NotNull(message = "页码不能为空")
    private int page;

    @Max(value = 1000,message = "每页条数 不能超过1000")
    @NotNull(message = "每页条数 不能为空")
    private int size;
}