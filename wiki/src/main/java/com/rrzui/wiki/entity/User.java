package com.rrzui.wiki.entity;

import lombok.Data;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-16 15:49
 **/

@Data
public class User {
    private Long id;
    private String login_name;//登录名称
    private String name;//昵称
    private String password;//密码
}
