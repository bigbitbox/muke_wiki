package com.rrzui.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrzui.wiki.entity.Ebook;
import com.rrzui.wiki.mapper.EbookMapper;
import com.rrzui.wiki.service.EbookService;
import org.springframework.stereotype.Service;

@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper,Ebook> implements EbookService {
}
