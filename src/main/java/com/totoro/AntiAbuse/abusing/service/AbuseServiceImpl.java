package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.AbuseContext;
import com.totoro.AntiAbuse.abusing.RequestUtils;
import com.totoro.AntiAbuse.abusing.domain.AbuseLog;
import com.totoro.AntiAbuse.abusing.domain.CouchbaseClient;
import com.totoro.AntiAbuse.abusing.domain.LimitStatus;
import com.totoro.AntiAbuse.abusing.domain.RateLimiter;
import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDTO;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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
    Map<String, RateLimiter> rateLimiters = new HashMap<>();
    @Override
    public AbuseResponseDTO checkAbuse(HttpServletRequest request) throws Exception {
        AbuseRequestDTO requestDTO = AbuseRequestDTO.of(request);
        check(requestDTO);
        return null;
    }

    @Override
    public AbuseResponseDTO checkAbuse(AbuseRequestDTO requestDTO) throws Exception {
        check(requestDTO);
        return null;
    }

    //ToDo response from 데이터 만들기
    private String check(AbuseRequestDTO req) throws Exception {
        RateLimiter rateLimiter = commonRateLimiter;
        CouchbaseClient cbClient = new CouchbaseClient();


        if (rateLimiters.containsKey(req.getDomain())) {
            RateLimiter val  = rateLimiters.get(req.getDomain());

            if (val.getUrls().containsKey(req.getUrl())) {
                rateLimiter = val;
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

// IE bug로 발생하는 케이스 절대 다수라 로그 남기지 않아도 될듯..
//        if (req.getFsId() != null && req.getPcId() == null) {
//            AbuseLog l = new AbuseLog(req, unusualFs);
//            cbClient.addLog(l);
//            // res.setBlock(true);
//            return ResponseEntity.ok(res);
//        }
        if (req.getPcId() != null && req.getFsId() != null) {
            if (req.getFsId().length() == 20) {
                LocalDateTime fsIdTime = timestampToTime(req.getFsId().substring(0, 6));

                if (!isIpv4Net(req.getFsId().substring(6, 14))) {
                    AbuseLog l = new AbuseLog(req, "ipWrong");
                    cbClient.addLog(l);
                    // res.setBlock(true);
                    return "ok";
                }
                // var userAgent = req.getFsId().substring(14, 19);
            } else {
                AbuseLog l = new AbuseLog(req, "lenWrong");
                cbClient.addLog(l);
                // res.setBlock(true);
                return "ok";
            }
        }

        String key = req.generateKey();
        LimitStatus limitStatus = rateLimiter.check(key);

        if (limitStatus.isLimited()) {
            AbuseLog l = new AbuseLog(req, "limited");
            cbClient.addLog(l);
//            res.setBlock(true);
//            res.setBlockTime(Long.toString(limitStatus.getLimitDuration().toMillis()));
        } else {
            rateLimiter.incrementKey(key);
//            res.setBlock(false);
//            res.setRemainLimit(Long.toString(limitStatus.getCurrentRemainRequests()));
        }

        return "";
    }
}
