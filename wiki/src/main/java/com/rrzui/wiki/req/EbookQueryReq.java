package com.rrzui.wiki.req;

import lombok.Data;

@Data
public class EbookQueryReq extends PageReq{
    private String name;
    private int id;
    private long category2_id;
}
