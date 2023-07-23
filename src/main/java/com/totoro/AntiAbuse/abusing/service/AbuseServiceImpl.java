package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.domain.RateLimiter;
import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDTO;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// https://www.mimul.com/blog/about-rate-limit-algorithm/
// Sliding Window Counter 수식 참고
@Service
@RequiredArgsConstructor
public class AbuseServiceImpl implements AbuseService{

    private final RateLimiter commonRateLimiter;
    @Override
    public AbuseResponseDTO checkAbuse(HttpServletRequest request) {
        AbuseRequestDTO requestDTO = AbuseRequestDTO.of(request);
        check(requestDTO);
        return null;
    }

    @Override
    public AbuseResponseDTO checkAbuse(AbuseRequestDTO requestDTO) {
        return null;
    }

    private void check(AbuseRequestDTO req){
        RateLimiter rateLimiter = commonRateLimiter;

        if (rateLimiters.containsKey(req.getDomain())) {
            Map<String, RateLimiter> val = rateLimiters.get(req.getDomain());
            if (val.containsKey(req.getUrl())) {
                rateLimiter = val.get(req.getUrl());
            }
        }
    }
}
