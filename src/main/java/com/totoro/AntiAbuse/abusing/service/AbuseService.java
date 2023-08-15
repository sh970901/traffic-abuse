package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDto;
import com.totoro.AntiAbuse.core.TotoroResponse;
import com.totoro.AntiAbuse.core.rateLimiter.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;

public interface AbuseService<T> {
    TotoroResponse<T> checkAbuse(HttpServletRequest request) throws Exception;
    TotoroResponse<T> checkAbuse(AbuseRequestDto requestDTO) throws Exception;
    RateLimiter updateRule();
    void updateBlackList();
}
