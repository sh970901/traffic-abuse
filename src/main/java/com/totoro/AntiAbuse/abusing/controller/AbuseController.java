package com.totoro.AntiAbuse.abusing.controller;

import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDto;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDto;
import com.totoro.AntiAbuse.abusing.service.AbuseService;
import com.totoro.AntiAbuse.core.TotoroResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/check-abuse")
@RequiredArgsConstructor
@Slf4j
public class AbuseController {
    private final AbuseService<AbuseResponseDto> abuseService;


    @GetMapping("/test")
    public TotoroResponse<AbuseResponseDto> testCheckAbuse(HttpServletRequest request) throws Exception {
        String domain = request.getServerName();
        String url = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String remoteAddr = request.getRemoteAddr();
        System.out.println("Domain: " + domain);
        System.out.println("URL: " + url);
        System.out.println("User Agent: " + userAgent);
        System.out.println("Remote Address: " + remoteAddr);
        System.out.println("Session " + request.getSession().getId());

        return abuseService.checkAbuse(request);
    }

    @PostMapping("/pcId")
    public TotoroResponse<AbuseResponseDto> dtoCheckAbuse(AbuseRequestDto requestDTO) throws Exception {
        return abuseService.checkAbuse(requestDTO);
    }

    @PostMapping("/hr")
    public TotoroResponse<AbuseResponseDto> httpCheckAbuse(@RequestBody HttpServletRequest request) throws Exception {
        return abuseService.checkAbuse(request);
    }

}
