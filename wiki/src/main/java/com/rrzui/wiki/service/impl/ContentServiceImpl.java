package com.rrzui.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrzui.wiki.entity.Content;
import com.rrzui.wiki.mapper.ContentMapper;
import com.rrzui.wiki.service.ContentService;
import org.springframework.stereotype.Service;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-15 16:38
 **/

@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements ContentService {
}
