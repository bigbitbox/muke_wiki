package com.rrzui.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rrzui.wiki.entity.Category;
import com.rrzui.wiki.req.CategoryQueryReq;
import com.rrzui.wiki.resp.CategoryQueryResp;
import com.rrzui.wiki.resp.CommonResp;
import com.rrzui.wiki.resp.PageResp;
import com.rrzui.wiki.service.CategoryService;
import com.rrzui.wiki.service.EbookService;
import com.rrzui.wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryListByPage")
    public CommonResp getCategoryListByPage(CategoryQueryReq req){
        Page<Category> page = new Page<>(req.getPage(), req.getSize());
        page = categoryService.page(page);
        List<Category> list = page.getRecords();

        LOG.info("总行数：{}",page.getTotal()+"");
        LOG.info("总页数：{}",page.getPages()+"");

        List<CategoryQueryResp> ebookResps = CopyUtil.copyList(list, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> respPage = new PageResp<>();
        respPage.setTotal(page.getTotal());
        respPage.setList(ebookResps);

        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        resp.setContent(respPage);

        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody CategoryQueryReq req){
        Category category = CopyUtil.copy(req, Category.class);
        categoryService.saveOrUpdate(category);

        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @GetMapping("/remove")
    public CommonResp remove(int id){

        categoryService.removeById(id);

        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }

    @GetMapping("/allList")
    public CommonResp allList(CategoryQueryReq req){
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        List<Category> list = categoryService.list(wrapper);

        List<CategoryQueryResp> resp = CopyUtil.copyList(list, CategoryQueryResp.class);

        CommonResp<List<CategoryQueryResp>> listCommonResp = new CommonResp<>();
        listCommonResp.setContent(resp);
        return listCommonResp;
    }
}