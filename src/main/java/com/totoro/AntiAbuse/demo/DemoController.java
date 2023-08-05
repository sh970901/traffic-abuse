package com.totoro.AntiAbuse.demo;

import com.totoro.AntiAbuse.core.RateLimiter;
import com.totoro.AntiAbuse.core.TotoroResponse;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDto;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {

//    private final DemoRepository demoRepository;
//    @GetMapping("/v1/test1")
//    public String v1(){
//        DemoDomain domain = new DemoDomain(1L, "이승훈");
//        demoRepository.save(domain);
//        return "ok";
//    }
    @GetMapping("/v1/healthy")
    public TotoroResponse<AbuseResponseDto> test2(){
        RateLimiter rateLimiter = new RateLimiter();
        System.out.println(LocalDateTime.now());
        return TotoroResponse.<AbuseResponseDto>from()
                               .data(AbuseResponseDto.abuse(null, "healthy"))
                               .build();
    }
}
