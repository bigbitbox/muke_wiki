package com.rrzui.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrzui.wiki.entity.Doc;

public interface DocService extends IService<Doc> {

    public int increaseViewCount(int id);

    public int increaseVoteCount(int id);

    public int updateEbookInfo();
}
