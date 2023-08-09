package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.domain.AbuseDocument;
import com.totoro.AntiAbuse.abusing.domain.AbuseLimitDocument;
import com.totoro.AntiAbuse.abusing.domain.AbuseLogDocument;
import com.totoro.AntiAbuse.abusing.dto.AbuseLogDto;
import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDto;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDto;
import com.totoro.AntiAbuse.core.RateLimiter;
import com.totoro.AntiAbuse.core.TotoroResponse;
import com.totoro.AntiAbuse.couchbase.service.CouchService;
import com.totoro.AntiAbuse.tools.storage.Blacklist;
import com.totoro.AntiAbuse.tools.storage.Limit;
import com.totoro.AntiAbuse.tools.storage.LimitStatus;
import com.totoro.AntiAbuse.tools.storage.Rule;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.totoro.AntiAbuse.AbuseContext.*;
import static com.totoro.AntiAbuse.utils.RequestUtils.*;

// https://www.mimul.com/blog/about-rate-limit-algorithm/
// Sliding Window Counter 수식 참고
@Service
@RequiredArgsConstructor
public class  AbuseServiceImpl implements AbuseService<AbuseResponseDto>{

    private final RateLimiter commonRateLimiter;
    private final CouchService<AbuseLogDocument> abuseLogService;
    private final CouchService<AbuseDocument> abuseService;
    private final CouchService<AbuseLimitDocument> abuseLimitService;
    private Map<String, RateLimiter> rateLimiters = new HashMap<>();
    @Override
    public TotoroResponse<AbuseResponseDto> checkAbuse(HttpServletRequest request) throws Exception {
        AbuseRequestDto requestDTO = AbuseRequestDto.of(request);
//        abuseLogService.addData(AbuseLogDocument.convertDtoToDocument(AbuseLogDto.createNewLog(requestDTO, "example2")));
//        abuseService.addData(AbuseDocument.builder().type("rule").rule(new Rule()).build());
//        abuseLimitService.addData(new AbuseLimitDocument("1","2","3","4",5));
        //Limit 객체의 Inc 함수를 abuseLimitService 사용하도록 로직을 변경해야함
        return check(requestDTO);
    }

    @Override
    public TotoroResponse<AbuseResponseDto> checkAbuse(AbuseRequestDto requestDTO) throws Exception {
        return check(requestDTO);
    }

    //ToDo response from 데이터 만들기
    private TotoroResponse<AbuseResponseDto> check(AbuseRequestDto req) throws Exception {

        RateLimiter rateLimiter = findRateLimiter(req);
        if(rateLimiter == null) rateLimiter = commonRateLimiter;


        if (isWhiteUserAgent(req.getUserAgent())) {
            return TotoroResponse.<AbuseResponseDto>from()
                                   .data(AbuseResponseDto.nonAbuse(null, WHITEUSERAGENT))
                                   .build();
        }

        if(isBlackOrNullUser(req)){
            AbuseLogDto dto = AbuseLogDto.createNewLog(req, req.getUserAgent());
            abuseLogService.addData(AbuseLogDocument.convertDtoToDocument(dto));
            return TotoroResponse.<AbuseResponseDto>from()
                                    .data(AbuseResponseDto.abuse(null, BLACKUSERAGENT))
                                    .build();
        }

        if(!ipVaildCheck(req)){
            AbuseLogDto dto = AbuseLogDto.createNewLog(req, IP_WRONG);
            abuseLogService.addData(AbuseLogDocument.convertDtoToDocument(dto));
            return TotoroResponse.<AbuseResponseDto>from()
                                    .data(AbuseResponseDto.abuse(null, IP_WRONG))
                                    .build();
        }

        if (!isFirstVisit(req)){
            return TotoroResponse.<AbuseResponseDto>from()
                                 .data(AbuseResponseDto.abuse(null, NON_FIRST_VISIT))
                                 .build();
        }

//      IE bug로 발생하는 케이스 절대 다수라 로그 남기지 않아도 될듯..
        if(isNullPcId(req)){
            AbuseLogDto dto = AbuseLogDto.createNewLog(req, UNUSUAL_ID);
            abuseLogService.addData(AbuseLogDocument.convertDtoToDocument(dto));
            return TotoroResponse.<AbuseResponseDto>from()
                                   .data(AbuseResponseDto.nonAbuse(null, UNUSUAL_ID))
                                   .build();
        }


        String key = req.generateKey();
        LimitStatus limitStatus = rateLimiter.check(key);

        if (limitStatus.isLimited()) {
            AbuseLogDto dto = AbuseLogDto.createNewLog(req, "Limited");
            abuseLogService.addData(AbuseLogDocument.convertDtoToDocument(dto));
            return TotoroResponse.<AbuseResponseDto>from()
                                   .data(AbuseResponseDto.abuse(Long.toString(limitStatus.getLimitDuration().toMillis()),"Limited"))
                                   .build();

        } else {
            rateLimiter.incrementKey(key);
            return TotoroResponse.<AbuseResponseDto>from()
                                   .data(AbuseResponseDto.nonAbuse(Long.toString(limitStatus.getLimitDuration().toMillis()),"KeyInc"))
                                   .build();
        }
    }

    private boolean ipVaildCheck(AbuseRequestDto req) {
        String fsId = req.getFsId();
        if (req.getPcId() != null && fsId != null && fsId.length() == 20) {
            String ipAddress = fsId.substring(6, 14);
            return isIpv4Net(ipAddress);
        }
        return false;
    }

    private Boolean isNullPcId(AbuseRequestDto req) {
        if (req.getFsId() != null && req.getPcId() == null) {
            return true;
        }
        return false;
    }

    private Boolean isBlackOrNullUser(AbuseRequestDto req) {
        if (req.getUserAgent() == null || isBlackUserAgent(req.getUserAgent())) {
            return true;
        }
        return false;
    }

    private RateLimiter findRateLimiter(AbuseRequestDto req) {
        if (rateLimiters.containsKey(req.getDomain())) {
            RateLimiter val = rateLimiters.get(req.getDomain());
            if (val.getUrls().containsKey(req.getUrl())) {
                return val;
            }
        }
        return null;
    }

    private Boolean isFirstVisit(AbuseRequestDto req) {
        if (req.getPcId() == null && req.getFsId() == null) {
            AbuseLogDto logDto = AbuseLogDto.createNewLog(req, FIRST_VISIT);

            int firstVisitLimit = 10;
            AbuseLogDocument logDocument = abuseLogService.getData(logDto.generateId());
            if (logDocument != null) {
                if (logDocument.getCount() > firstVisitLimit) {
                    //계속 pcId와 fsId가 null로 요청이 오는데 이 count가 10을 넘길 경우
                    logDto.setCount(firstVisitLimit);
                    abuseLogService.saveForce(AbuseLogDocument.convertDtoToDocument(logDto));
                    return false;
                } else {
                    abuseLogService.addData(AbuseLogDocument.convertDtoToDocument(logDto));
                    return true;
                }
            } else {
                abuseLogService.addData(AbuseLogDocument.convertDtoToDocument(logDto));
                return true;
                //첫번째 방문에 pcId와 fsId가 둘 다 null 인 케이스
            }
        }
        return true;
    }
}
