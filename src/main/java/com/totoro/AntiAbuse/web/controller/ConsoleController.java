package com.totoro.AntiAbuse.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/console")
public class ConsoleController {
    @GetMapping
    public String home(){
        return "index";
    }
}
