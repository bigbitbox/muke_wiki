package com.rrzui.wiki.config;


import com.rrzui.wiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test/**",
                        "/user/userLogin",
                        "/user/logout/**",
                        "/category/allList",
                        "/ebook/getEbookListByPage",
                        "/doc/allList/**",
                        "/doc/findContentById/**"
                );
    }


}
