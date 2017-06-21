package com.sen.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuangJS
 * @date 2017/6/21 15:02.
 */
@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello(String name) {
        return "hello " + name;
    }

}
