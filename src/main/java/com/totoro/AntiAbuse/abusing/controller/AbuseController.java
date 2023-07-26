package com.totoro.AntiAbuse.abusing.controller;

import com.totoro.AntiAbuse.abusing.core.RateLimiter;
import com.totoro.AntiAbuse.abusing.core.TotoroResponse;
import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDto;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDto;
import com.totoro.AntiAbuse.abusing.service.AbuseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/v2")
@RequiredArgsConstructor
@Slf4j
public class AbuseController {
    private final AbuseService<AbuseResponseDto> abuseService;

    private final RateLimiter commonRateLimiter;


    @PostMapping("/check-abuse")
    public TotoroResponse<AbuseResponseDto> httpCheckAbuse(HttpServletRequest request) throws Exception {
        return abuseService.checkAbuse(request);
    }

    @PostMapping("/check-abuse2")
    public TotoroResponse<AbuseResponseDto> dtoCheckAbuse(AbuseRequestDto requestDTO) throws Exception {
        return abuseService.checkAbuse(requestDTO);
    }

    @GetMapping("/test")
    public String test(){
        RateLimiter rateLimiter = new RateLimiter();
        System.out.println(LocalDateTime.now());
//        LocalDateTime localDateTime = rateLimiter.truncateToMinutes(LocalDateTime.now());
//        System.out.println(localDateTime);
        return "ok";
    }

}
