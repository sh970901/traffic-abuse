package com.totoro.AntiAbuse.demo;

import com.totoro.AntiAbuse.abusing.core.RateLimiter;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDTO;
import java.time.LocalDateTime;
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
    @GetMapping("/v1/test2")
    public AbuseResponseDTO<String> test2(){
        RateLimiter rateLimiter = new RateLimiter();
        System.out.println(LocalDateTime.now());
        //        LocalDateTime localDateTime = rateLimiter.truncateToMinutes(LocalDateTime.now());
        //        System.out.println(localDateTime);
        return AbuseResponseDTO.<String>from()
                               .block(true)
                               .data("dd")
                               .build();
    }
}
