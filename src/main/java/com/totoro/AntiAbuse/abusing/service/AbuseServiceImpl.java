package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDTO;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
