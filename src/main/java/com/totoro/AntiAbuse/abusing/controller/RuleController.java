package com.totoro.AntiAbuse.abusing.controller;

import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDto;
import com.totoro.AntiAbuse.abusing.service.AbuseService;
import com.totoro.AntiAbuse.core.TotoroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RuleController {
    private final AbuseService<AbuseResponseDto> abuseService;
    @PostMapping("/update/rule")
    public TotoroResponse<AbuseResponseDto> updateRule() throws Exception {
        abuseService.updateRule();
        return TotoroResponse.<AbuseResponseDto>from().build();
    }

}
