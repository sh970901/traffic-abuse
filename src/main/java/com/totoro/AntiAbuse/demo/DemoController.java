package com.totoro.AntiAbuse.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/v1/test1")
    public String v1(){
        return "ok";
    }
}
