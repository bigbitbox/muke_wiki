package com.rrzui.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrzui.wiki.entity.User;
import com.rrzui.wiki.mapper.UserMapper;
import com.rrzui.wiki.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-16 15:51
 **/

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}