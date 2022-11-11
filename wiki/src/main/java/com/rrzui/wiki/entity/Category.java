package com.rrzui.wiki.entity;

import lombok.Data;

@Data
public class Category {
    private Long id;
    private Long parent;
    private String name;
    private int sort;

}