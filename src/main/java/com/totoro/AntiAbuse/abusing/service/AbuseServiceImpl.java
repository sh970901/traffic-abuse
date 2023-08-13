package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseRuleDocument;
import com.totoro.AntiAbuse.couchbase.domain.AbuseLimitDocument;
import com.totoro.AntiAbuse.couchbase.domain.AbuseLogDocument;
import com.totoro.AntiAbuse.abusing.dto.AbuseLogDto;
import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDto;
import com.totoro.AntiAbuse.abusing.dto.AbuseResponseDto;
import com.totoro.AntiAbuse.core.rateLimiter.RateLimiter;
import com.totoro.AntiAbuse.core.TotoroResponse;
import com.totoro.AntiAbuse.couchbase.service.CouchService;
import com.totoro.AntiAbuse.tools.storage.Blacklist;
import com.totoro.AntiAbuse.core.rateLimiter.LimitStatus;
import com.totoro.AntiAbuse.tools.storage.Rule;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.totoro.AntiAbuse.AbuseContext.*;
import static com.totoro.AntiAbuse.utils.RequestUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class  AbuseServiceImpl implements AbuseService<AbuseResponseDto>{
    //Todo RateLimiters rule 인스턴스 재활용

    private final CouchService<AbuseLogDocument> abuseLogService;
    private final CouchService<AbuseRuleDocument> abuseRuleService;
    private final CouchService<AbuseLimitDocument> abuseLimitService;
    private static Map<String, RateLimiter> rateLimiters = new HashMap<>();
    private static int firstVisitLimit = 20;
    @Override
    public TotoroResponse<AbuseResponseDto> checkAbuse(HttpServletRequest request) throws Exception {
        AbuseRequestDto requestDTO = AbuseRequestDto.of(request);
        System.out.println("11112333331"+request.getHeader("User-Agent"));
        return check(requestDTO);
    }

    @Override
    public TotoroResponse<AbuseResponseDto> checkAbuse(AbuseRequestDto requestDTO) throws Exception {
        return check(requestDTO);
    }

    @Override
    public RateLimiter updateRule() {
        AbuseRuleDocument abuseRule = abuseRuleService.getData("rule");
        int requestsLimit = abuseRule.getRule().getRequestsLimit();

        // rule document에 정의된 값으로 업데이트
        RateLimiter commonRateLimiter = RateLimiter.builder().requestsLimit(requestsLimit).abuseLimitService(abuseLimitService).build();
        rateLimiters.put("common", commonRateLimiter);
        blackUserAgent = abuseRule.getRule().getBlackUserAgent();
        whiteUserAgent = abuseRule.getRule().getWhiteUserAgent();
        firstVisitLimit = abuseRule.getRule().getFirstVisitLimit();

        log.info("updateRule");
        return commonRateLimiter;
    }

    @Override
    public void updateBlackList() {
        AbuseRuleDocument abuseRule = abuseRuleService.getData("blacklist");
        memberIds = abuseRule.getBlacklist().getMemberIds();
        ipAddress = abuseRule.getBlacklist().getIpAddress();
        log.info("updateBlackList");
    }

    @PostConstruct
    private void init() {
        //TODO 값을 넣어줄 때는 DTO를 사용하도록 수정
        abuseRuleService.addData(AbuseRuleDocument.builder().type("rule").rule(new Rule(5, whiteUserAgent, blackUserAgent)).build());
        abuseRuleService.addData(AbuseRuleDocument.builder().type("blacklist").blacklist(new Blacklist(ipAddress, memberIds)).build());
//        abuseLimitService.addData(new AbuseLimitDocument("1","2","3","4",5));
//        abuseLogService.addData(AbuseLogDocument.convertDtoToDocument(AbuseLogDto.createNewLog(requestDTO, "example2")));
    }
    private TotoroResponse<AbuseResponseDto> check(AbuseRequestDto req) throws Exception {

        RateLimiter rateLimiter = findRateLimiter(req);

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

//        if(!ipValidCheck(req)){
//            AbuseLogDto dto = AbuseLogDto.createNewLog(req, IP_WRONG);
//            abuseLogService.addData(AbuseLogDocument.convertDtoToDocument(dto));
//            return TotoroResponse.<AbuseResponseDto>from()
//                                    .data(AbuseResponseDto.abuse(null, IP_WRONG))
//                                    .build();
//        }

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
        //TODO BlackList 접근 차단 추가로직 필요


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
                    .data(AbuseResponseDto.nonAbuse("noBlock","KeyInc", limitStatus.getCurrentRate(),limitStatus.getCurrentRemainRequests()))
                    .build();
        }
    }
    private boolean ipValidCheck(AbuseRequestDto req) {
        String fsId = req.getFsId();
        if (req.getPcId() != null && fsId != null && fsId.length() == 20) {
            String ipAddress = fsId.substring(6, 14);
            return isIpv4Net(ipAddress);
        }
        return false;
    }

    private Boolean isNullPcId(AbuseRequestDto req) {
        return req.getFsId() != null && req.getPcId() == null;
    }

    private Boolean isBlackOrNullUser(AbuseRequestDto req) {
        return req.getUserAgent() == null || isBlackUserAgent(req.getUserAgent());
    }

    private RateLimiter findRateLimiter(AbuseRequestDto req) {
        return ruleRateLimiter(req);
    }

    private RateLimiter ruleRateLimiter(AbuseRequestDto req){
        RateLimiter rateLimiter;
        if(rateLimiters.containsKey(req.getDomain())){
            rateLimiter = rateLimiters.get(req.getDomain());
            if (rateLimiter.getUrls().containsKey(req.getUrl())) {
                return rateLimiter;
            }
        }
        return getCommonRateLimiter();
    }
    private RateLimiter getCommonRateLimiter() {
        RateLimiter commonRateLimiter = rateLimiters.get("common");
        if (commonRateLimiter == null) {
            return updateRule();
        }
        return commonRateLimiter;
    }

    private Boolean isFirstVisit(AbuseRequestDto req) {
        if (req.getPcId() == null && req.getFsId() == null) {
            AbuseLogDto logDto = AbuseLogDto.createNewLog(req, FIRST_VISIT);

            AbuseLogDocument logDocument = abuseLogService.getData(logDto.generateId());
            if (logDocument != null) {
                if (logDocument.getCount() > firstVisitLimit) {
                    //계속 pcId와 fsId가 null로 요청이 오는데 이 count가 firstVisitLimit을 넘길 경우
                    logDto.setCount(firstVisitLimit);
                    // count 초기화 한 값을 다시 저장해야하나?
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
