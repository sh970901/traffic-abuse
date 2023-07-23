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
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Enumeration;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AbuseController {
    private final AbuseService abuseService;

    private final RateLimiter commonRateLimiter;

    @PostMapping("/v1/check-abuse")
    public String getRequestHeader(HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
        }
        return "1";
    }

    @PostMapping("/v2/check-abuse")
    public AbuseResponseDTO checkAbuse2(HttpServletRequest request){
        AbuseResponseDTO responseDTO = abuseService.checkAbuse(request);
        return responseDTO;
    }

    @PostMapping("/v3/check-abuse")
    public AbuseResponseDTO checkAbuse2(AbuseRequestDTO requestDTO){
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
