package com.totoro.AntiAbuse.core;

import com.totoro.AntiAbuse.couchbase.domain.AbuseLimitDocument;
import com.totoro.AntiAbuse.couchbase.service.CouchService;
import com.totoro.AntiAbuse.tools.storage.LimitStatus;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class RateLimiter {
    private static final int requestsLimit = 5;
    private final Duration windowSize = Duration.ofMinutes(1);
    private Map<String, Integer> urls = new HashMap<>();

    private CouchService<AbuseLimitDocument> abuseLimitService;

    public RateLimiter(CouchService<AbuseLimitDocument> abuseLimitService){
        this.abuseLimitService = abuseLimitService;
    }
    public void incrementKey(String key) throws Exception {
        LocalDateTime currentWindow = truncateToMinutes(LocalDateTime.now());
        abuseLimitService.addData(new AbuseLimitDocument(key+"::"+currentWindow));
    }


    //https://www.mimul.com/blog/about-rate-limit-algorithm/
    //Sliding Window Counter -> 이전 요청의 비율과 현재 요청을 합하여 count 계산
    public LimitStatus check(String key) throws Exception {
        LocalDateTime currentWindowStartTime = truncateToMinutes(LocalDateTime.now()); // 현재 윈도우 시작 시간
        LocalDateTime prevWindowStartTime = currentWindowStartTime.minus(windowSize); //이전 윈도우 시작 시간
        LimitStatus limitStatus;

        long prevRequests, currentRequests;
        try {
            AbuseLimitDocument prevLimit = abuseLimitService.getData(key + "::" + prevWindowStartTime);
            if( prevLimit == null ){
                prevRequests = 0;
            } else {
                prevRequests = prevLimit.getCount();
            }
            AbuseLimitDocument currentLimit = abuseLimitService.getData(key + "::" + currentWindowStartTime);
            if( currentLimit == null ){
                currentRequests = 0;
            } else {
                currentRequests = currentLimit.getCount();
            }
        }
        catch (Exception e) {
            throw new Exception("Error while getting requests count from data store", e);
        }

        Duration durationFromCurrWindow = Duration.between(currentWindowStartTime, LocalDateTime.now());

        double rate = ((windowSize.toMillis() - durationFromCurrWindow.toMillis()) / (double) windowSize.toMillis()) * prevRequests + currentRequests;
        limitStatus = new LimitStatus();

        if (rate >= requestsLimit) {
            limitStatus.setLimited(true);
            Duration limitDuration = calcLimitDuration(prevRequests, currentRequests, durationFromCurrWindow);
            limitStatus.setLimitDuration(limitDuration);
        }
        limitStatus.setCurrentRate(rate);
        limitStatus.setCurrentRemainRequests(requestsLimit - (long) rate - 1); // Inc 때문에 1 차감
        return limitStatus;
    }

    private Duration calcLimitDuration(long prevRequests, long currentRequests, Duration durationFromCurrWindow) {
        Duration limitDuration;
        if (prevRequests == 0) {
            if (currentRequests != 0) {
                double nextWindowUnblockPoint = windowSize.toMillis() * (1.0 - ((double) requestsLimit / currentRequests));
                Duration durationToNextWindow = windowSize.minus(durationFromCurrWindow);
                limitDuration = durationToNextWindow.plusMillis((long) nextWindowUnblockPoint + 1);
            } else {
                // requestsLimit를 0으로 설정하면 모든 요청을 차단, - limitDuration을 -1로 설정
                limitDuration = Duration.ofMillis(-1);
            }
        } else {
            double currWindowUnblockPoint = windowSize.toMillis() * (1.0 - ((double) (requestsLimit - currentRequests) / prevRequests));
            limitDuration = Duration.ofMillis((long) currWindowUnblockPoint + 1).minus(durationFromCurrWindow);
        }
        return limitDuration;
    }
    private LocalDateTime truncateToMinutes(LocalDateTime dateTime) {
        return dateTime.withSecond(0).withNano(0);
//        long truncatedSeconds = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond() / 60 * 60;
//        return LocalDateTime.ofEpochSecond(truncatedSeconds, 0, ZoneOffset.UTC);
    }
}
