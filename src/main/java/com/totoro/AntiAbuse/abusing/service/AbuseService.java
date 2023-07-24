package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDTO;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AbuseService {
    AbuseResponseDTO checkAbuse(HttpServletRequest request) throws Exception;
    AbuseResponseDTO checkAbuse(AbuseRequestDTO requestDTO) throws Exception;
}
