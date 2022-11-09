package com.rrzui.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrzui.wiki.entity.Ebook;
import com.rrzui.wiki.req.EbookReq;
import com.rrzui.wiki.resp.CommonResp;
import com.rrzui.wiki.resp.EbookResp;
import com.rrzui.wiki.service.EbookService;
import com.rrzui.wiki.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
}
