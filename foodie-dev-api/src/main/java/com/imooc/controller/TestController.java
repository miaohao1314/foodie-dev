package com.imooc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("indexsss")
public class TestController {

    @GetMapping("/hellos")
    public  Object hello(){
        String  name="zhangsan";
        System.out.println(name);

        return  "hello";
    }
}
