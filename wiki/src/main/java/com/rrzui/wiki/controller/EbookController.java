package com.rrzui.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rrzui.wiki.entity.Ebook;
import com.rrzui.wiki.req.EbookReq;
import com.rrzui.wiki.req.PageReq;
import com.rrzui.wiki.resp.CommonResp;
import com.rrzui.wiki.resp.EbookResp;
import com.rrzui.wiki.resp.PageResp;
import com.rrzui.wiki.service.EbookService;
import com.rrzui.wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EbookController {

    @Autowired
    private EbookService ebookService;

//    @GetMapping("/getEbooks")
//    public List<Ebook> getEbooks(){
//        List<Ebook> list = ebookService.list();
//        for (Ebook ebook : list) {
//            System.out.println(ebook.toString());
//        }
//        return list;
//    }

    @GetMapping("/getEbooks")
    public CommonResp getEbooks(){
        List<Ebook> list = ebookService.list();
        for (Ebook ebook : list) {
            System.out.println(ebook.toString());
        }
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/getEbookByName")
    public CommonResp getEbookByName(EbookReq ebookReq){
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like("name",ebookReq.getName());
        List<Ebook> list = ebookService.list(wrapper);
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/getEbookByEbookReq")
    public CommonResp getEbookByEbookReq(EbookReq ebookReq){

        List<Ebook> list;
        if (!ObjectUtils.isEmpty(ebookReq.getName())) {
            QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
            wrapper.like("name",ebookReq.getName());
            list = ebookService.list(wrapper);
        }else {
            list = ebookService.list();
        }



        List<EbookResp> ebookResps = CopyUtil.copyList(list, EbookResp.class);
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        resp.setContent(ebookResps);
        return resp;
    }

    //打印日志
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @GetMapping("/getEbookListByPage")
    public CommonResp getEbookListByPage(PageReq pageReq){
        Page<Ebook> page = new Page<>(pageReq.getPage(), pageReq.getSize());
        page = ebookService.page(page);
        List<Ebook> list = page.getRecords();

        LOG.info("总行数：{}",page.getTotal()+"");
        LOG.info("总页数：{}",page.getPages()+"");

        List<EbookResp> ebookResps = CopyUtil.copyList(list, EbookResp.class);

        PageResp<EbookResp> respPage = new PageResp<>();
        respPage.setTotal(page.getTotal());
        respPage.setList(ebookResps);

        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        resp.setContent(respPage);
        return resp;
    }
}
