package com.rrzui.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello222";
    }

    //测试从配置文件中获取属性值
    @Value("${test.hello:Test}")
    private String testHello;

    @GetMapping("/testHello")
    public String testHello(){
        return "Hello World!"+testHello;
    }
}
