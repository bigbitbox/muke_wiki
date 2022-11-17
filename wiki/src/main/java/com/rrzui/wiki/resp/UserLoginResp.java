package com.rrzui.wiki.resp;

import lombok.Data;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-17 15:21
 **/

@Data
public class UserLoginResp {
    private Long id;
    private String login_name;//登录名称
    private String name;//昵称
}
