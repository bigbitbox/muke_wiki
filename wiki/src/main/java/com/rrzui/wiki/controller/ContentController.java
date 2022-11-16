package com.rrzui.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rrzui.wiki.entity.Content;
import com.rrzui.wiki.req.ContentQueryReq;
import com.rrzui.wiki.req.ContentSaveReq;
import com.rrzui.wiki.resp.CommonResp;
import com.rrzui.wiki.resp.ContentQueryResp;
import com.rrzui.wiki.resp.PageResp;
import com.rrzui.wiki.service.ContentService;
import com.rrzui.wiki.service.EbookService;
import com.rrzui.wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-15 16:41
 **/

@RestController
@RequestMapping("/content")
public class ContentController {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Autowired
    private ContentService contentService;

    @GetMapping("/getContentListByPage")
    public CommonResp getContentListByPage(ContentQueryReq req){
        Page<Content> page = new Page<>(req.getPage(), req.getSize());
        page = contentService.page(page);
        List<Content> list = page.getRecords();

        LOG.info("总行数：{}",page.getTotal()+"");
        LOG.info("总页数：{}",page.getPages()+"");

        List<ContentQueryResp> contentQueryResps = CopyUtil.copyList(list, ContentQueryResp.class);

        PageResp<ContentQueryResp> respPage = new PageResp<>();
        respPage.setTotal(page.getTotal());
        respPage.setList(contentQueryResps);

        CommonResp<PageResp<ContentQueryResp>> resp = new CommonResp<>();
        resp.setContent(respPage);

        return resp;
    }


    @GetMapping("/allList")
    public CommonResp allList(ContentQueryReq req){
        QueryWrapper<Content> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        List<Content> list = contentService.list(wrapper);

        List<ContentQueryResp> resp = CopyUtil.copyList(list, ContentQueryResp.class);

        CommonResp<List<ContentQueryResp>> listCommonResp = new CommonResp<>();
        listCommonResp.setContent(resp);
        return listCommonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody ContentSaveReq req){
        Content Content = CopyUtil.copy(req, Content.class);
        contentService.saveOrUpdate(Content);

        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @GetMapping("/remove")
    public CommonResp remove(Integer [] ids){
        List<Integer> list = Arrays.asList(ids);
        contentService.removeByIds(list);

        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }

}