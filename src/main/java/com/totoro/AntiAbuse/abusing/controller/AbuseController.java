package com.totoro.AntiAbuse.abusing.controller;

import com.totoro.AntiAbuse.abusing.domain.RateLimiter;
import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDTO;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDTO;
import com.totoro.AntiAbuse.abusing.service.AbuseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Enumeration;

@RestController
@RequestMapping(value = "/v2")
@RequiredArgsConstructor
@Slf4j
public class AbuseController {
    private final AbuseService abuseService;

    private final RateLimiter commonRateLimiter;


    @PostMapping("/check-abuse")
    public AbuseResponseDTO httpCheckAbuse(HttpServletRequest request) throws Exception {
        AbuseResponseDTO responseDTO = abuseService.checkAbuse(request);
        return responseDTO;
    }

    @PostMapping("/check-abuse3")
    public AbuseResponseDTO dtoCheckAbuse(AbuseRequestDTO requestDTO){
        AbuseResponseDTO responseDTO = abuseService.checkAbuse(requestDTO);
        return responseDTO;
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
