package com.rrzui.wiki.controller;

import com.rrzui.wiki.entity.Ebook;
import com.rrzui.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/getEbooks")
    public List<Ebook> getEbooks(){
        List<Ebook> list = ebookService.list();
        for (Ebook ebook : list) {
            System.out.println(ebook.toString());
        }
        return list;
    }
}
