package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.AbuseContext;
import com.totoro.AntiAbuse.abusing.RequestUtils;
import com.totoro.AntiAbuse.abusing.domain.AbuseLog;
import com.totoro.AntiAbuse.abusing.domain.CouchbaseClient;
import com.totoro.AntiAbuse.abusing.domain.RateLimiter;
import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDTO;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.totoro.AntiAbuse.abusing.RequestUtils.*;

// https://www.mimul.com/blog/about-rate-limit-algorithm/
// Sliding Window Counter 수식 참고
@Service
@RequiredArgsConstructor
public class AbuseServiceImpl implements AbuseService{

    private final RateLimiter commonRateLimiter;
    Map<String, Map<String, RateLimiter>> rateLimiters = new HashMap<>();
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

    //ToDo response from 데이터 만들기
    private String check(AbuseRequestDTO req){
        RateLimiter rateLimiter = commonRateLimiter;

        if (rateLimiters.containsKey(req.getDomain())) {
            Map<String, RateLimiter> val = rateLimiters.get(req.getDomain());

            if (val.containsKey(req.getUrl())) {
                rateLimiter = val.get(req.getUrl());
            }
        }

        if (isWhiteUserAgent(req.getUserAgent())) {
            return "ok";
        }

        if (req.getUserAgent() == null || isBlackUserAgent(req.getUserAgent())) {
            AbuseLog l = new AbuseLog(req, req.getUserAgent());
//            cbClient.addLog(l);
            AbuseResponseDTO abuse = AbuseResponseDTO.abuse();
            return "ok";
        }

        if (req.getPcId() == null && req.getFsId() == null) {
            CouchbaseClient cbClient = new CouchbaseClient();
            AbuseLog l = new AbuseLog(req, AbuseContext.FIRST_VISIT);
            int firstVisitLimit = 10;
            if (cbClient.exist(l.generateId())) {
//                VisitCountDoc doc =
                AbuseLog firstVisit = cbClient.getFirstVisit(l.generateId());
                if (firstVisit != null && firstVisit.getCount() > firstVisitLimit) {
                    l.setCount(firstVisitLimit);
                    cbClient.addLog(l);
                    // res.setBlock(true);
                    // res.setMessage("첫 방문 형태로 계속 요청");
                    // return ResponseEntity.ok(res);
                } else {
                    cbClient.addFirstVisit(l);
                }
            } else {
                cbClient.addFirstVisit(l);
            }
        }

        return "";
    }
}
