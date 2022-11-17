package com.rrzui.wiki.req;

import lombok.Data;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-16 15:52
 **/

@Data
public class UserQueryReq extends PageReq{
    private String login_name;         //名称
}