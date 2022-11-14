package com.rrzui.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rrzui.wiki.entity.Ebook;
import com.rrzui.wiki.req.EbookQueryReq;
import com.rrzui.wiki.req.EbookSaveReq;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ebook")
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
    public CommonResp getEbookByName(EbookQueryReq ebookQueryReq){
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like("name", ebookQueryReq.getName());
        List<Ebook> list = ebookService.list(wrapper);
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/getEbookByEbookReq")
    public CommonResp getEbookByEbookReq(EbookQueryReq ebookQueryReq){

        List<Ebook> list;
        if (!ObjectUtils.isEmpty(ebookQueryReq.getName())) {
            QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
            wrapper.like("name", ebookQueryReq.getName());
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
    public CommonResp getEbookListByPage(@Valid EbookQueryReq pageReq){
        Page<Ebook> page = new Page<>(pageReq.getPage(), pageReq.getSize());

        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(pageReq.getCategory2_id())){
            wrapper.like("category2_id",pageReq.getCategory2_id());
        }
        if (!ObjectUtils.isEmpty(pageReq.getName())){
            wrapper.like("name",pageReq.getName());
        }

        page = ebookService.page(page,wrapper);


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

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq req){
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        //会根据是否有id判断   修改（有id）  新增（无id）
        ebookService.saveOrUpdate(ebook);

        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @GetMapping("/remove")
    public CommonResp remove(int id){
        ebookService.removeById(id);
        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }
}
