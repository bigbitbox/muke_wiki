package com.rrzui.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrzui.wiki.entity.Doc;
import com.rrzui.wiki.mapper.DocMapper;
import com.rrzui.wiki.service.DocService;
import org.springframework.stereotype.Service;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-15 09:38
 **/

@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements DocService {
    @Override
    public int increaseViewCount(int id) {
        return this.baseMapper.increaseViewCount(id);
    }
}