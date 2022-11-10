package com.rrzui.wiki.req;

import lombok.Data;

@Data
public class EbookReq extends PageReq{
    private String name;
    private int id;
}
