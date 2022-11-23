package com.rrzui.wiki.controller;

import com.rrzui.wiki.resp.CommonResp;
import com.rrzui.wiki.resp.StatisticResp;
import com.rrzui.wiki.service.EbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-22 23:25
 **/

@RestController
@RequestMapping("ebook-snapshot")
public class EbookSnapshotController {

    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("getStatistic")
    public CommonResp getStatistic(){
        CommonResp<List<StatisticResp>> resp = new CommonResp<>();
        List<StatisticResp> statistic = ebookSnapshotService.getStatistic();
        resp.setContent(statistic);
        return resp;
    }

    @GetMapping("get30Statistic")
    public CommonResp get30Statistic(){
        CommonResp<List<StatisticResp>> resp = new CommonResp<>();
        List<StatisticResp> statistic = ebookSnapshotService.get30Statistic();
        resp.setContent(statistic);
        return resp;
    }
}
