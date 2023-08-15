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
    public TotoroResponse<AbuseResponseDto> tetCheckAbuse(HttpServletRequest request) throws Exception {
        return abuseService.checkAbuse(request);
    }

    @PostMapping("/pcId")
    public TotoroResponse<AbuseResponseDto> dtoCheckAbuse(AbuseRequestDto requestDTO) throws Exception {
        return abuseService.checkAbuse(requestDTO);
    }

    @PostMapping("/httpRequest")
    public TotoroResponse<AbuseResponseDto> httpCheckAbuse(@RequestBody HttpServletRequest request) throws Exception {
        return abuseService.checkAbuse(request);
    }

}
