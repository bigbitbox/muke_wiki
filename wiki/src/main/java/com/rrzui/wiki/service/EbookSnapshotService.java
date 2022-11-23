package com.rrzui.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrzui.wiki.entity.EbookSnapshot;
import com.rrzui.wiki.resp.StatisticResp;

import java.util.List;

public interface EbookSnapshotService extends IService<EbookSnapshot> {
    public void genSnapshot();

    public List<StatisticResp> getStatistic();

    public List<StatisticResp> get30Statistic();


}