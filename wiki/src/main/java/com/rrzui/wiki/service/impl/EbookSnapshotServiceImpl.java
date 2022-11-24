package com.rrzui.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrzui.wiki.entity.EbookSnapshot;
import com.rrzui.wiki.mapper.EbookSnapshotMapper;
import com.rrzui.wiki.resp.StatisticResp;
import com.rrzui.wiki.service.EbookSnapshotService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-22 15:12
 **/

@Service
public class EbookSnapshotServiceImpl extends ServiceImpl<EbookSnapshotMapper, EbookSnapshot> implements EbookSnapshotService {
    @Override
    public void genSnapshot() {
        this.baseMapper.genSnapshot();
    }

    @Override
    public List<StatisticResp> getStatistic() {
        List<StatisticResp> statistic = this.baseMapper.getStatistic();
        return statistic;
    }

    @Override
    public List<StatisticResp> get30Statistic() {
        List<StatisticResp> statistic = this.baseMapper.get30Statistic();
        return statistic;
    }


}