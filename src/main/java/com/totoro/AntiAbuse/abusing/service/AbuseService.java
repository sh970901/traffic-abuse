package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDTO;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AbuseService<T> {
    AbuseResponseDTO<T> checkAbuse(HttpServletRequest request) throws Exception;
    AbuseResponseDTO<T> checkAbuse(AbuseRequestDTO requestDTO) throws Exception;
}
