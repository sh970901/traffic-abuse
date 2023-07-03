package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDTO;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

// https://www.mimul.com/blog/about-rate-limit-algorithm/
// Sliding Window Counter 수식 참고
@Service
public class AbuseServiceImpl implements AbuseService{
    @Override
    public AbuseResponseDTO checkAbuse(HttpServletRequest request) {
        AbuseRequestDTO requestDTO = AbuseRequestDTO.of(request);

        return null;
    }

    @Override
    public AbuseResponseDTO checkAbuse(AbuseRequestDTO requestDTO) {
        return null;
    }

}
