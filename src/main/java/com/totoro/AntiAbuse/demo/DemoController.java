package com.totoro.AntiAbuse.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final DemoRepository demoRepository;
    @GetMapping("/v1/test1")
    public String v1(){
        DemoDomain domain = new DemoDomain(1L, "이승훈");
        demoRepository.save(domain);
        return "ok";
    }
}
