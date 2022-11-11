package com.rrzui.wiki.req;

import lombok.Data;

@Data
public class CategorySaveReq {
    private Long id;
    private Long parent;
    private String name;
    private int sort;
}
