package com.rrzui.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rrzui.wiki.entity.Content;
import com.rrzui.wiki.entity.Doc;
import com.rrzui.wiki.req.DocQueryReq;
import com.rrzui.wiki.req.DocSaveReq;
import com.rrzui.wiki.resp.CommonResp;
import com.rrzui.wiki.resp.DocQueryResp;
import com.rrzui.wiki.resp.PageResp;
import com.rrzui.wiki.service.ContentService;
import com.rrzui.wiki.service.DocService;
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
 * @create: 2022-11-15 09:40
 **/

@RestController
@RequestMapping("/doc")
public class DocController {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Autowired
    private DocService docService;

    @Autowired
    private ContentService contentService;

    @GetMapping("/getDocListByPage")
    public CommonResp getdocListByPage(DocQueryReq req){
        Page<Doc> page = new Page<>(req.getPage(), req.getSize());
        page = docService.page(page);
        List<Doc> list = page.getRecords();

        LOG.info("总行数：{}",page.getTotal()+"");
        LOG.info("总页数：{}",page.getPages()+"");

        List<DocQueryResp> ebookResps = CopyUtil.copyList(list, DocQueryResp.class);

        PageResp<DocQueryResp> respPage = new PageResp<>();
        respPage.setTotal(page.getTotal());
        respPage.setList(ebookResps);

        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        resp.setContent(respPage);

        return resp;
    }


//    @GetMapping("/allList")
//    public CommonResp allList(DocQueryReq req){
//        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
//        wrapper.orderByAsc("sort");
//        List<Doc> list = docService.list(wrapper);
//
//        List<DocQueryResp> resp = CopyUtil.copyList(list, DocQueryResp.class);
//
//        CommonResp<List<DocQueryResp>> listCommonResp = new CommonResp<>();
//        listCommonResp.setContent(resp);
//        return listCommonResp;
//    }

    @GetMapping("/allList/{ebookId}")
    public CommonResp allList(@PathVariable Long ebookId){
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        wrapper.eq("ebook_id",ebookId);
        List<Doc> list = docService.list(wrapper);

        List<DocQueryResp> resp = CopyUtil.copyList(list, DocQueryResp.class);

        CommonResp<List<DocQueryResp>> listCommonResp = new CommonResp<>();
        listCommonResp.setContent(resp);
        return listCommonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        Doc doc = CopyUtil.copy(req, Doc.class);
        docService.saveOrUpdate(doc);

        Content content = CopyUtil.copy(req, Content.class);
        contentService.saveOrUpdate(content);

        CommonResp resp = new CommonResp<>();
        return resp;
    }



    @GetMapping("/remove")
    public CommonResp remove(Integer [] ids){
        List<Integer> list = Arrays.asList(ids);
        docService.removeByIds(list);

        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }

    @GetMapping("/findContentById/{id}")
    public CommonResp findContentById(@PathVariable int id){
        Content content = contentService.getById(id);
        //每次调用这个方法证明文档被浏览，浏览数+1
        docService.increaseViewCount(id);
        CommonResp<String> resp = new CommonResp<>();
        if (content!=null && content.getContent()!=null){
            resp.setContent(content.getContent());
        }
        return resp;
    }

    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable int id){
        docService.increaseVoteCount(id);
        CommonResp<String> resp = new CommonResp<>();

        return resp;
    }


}