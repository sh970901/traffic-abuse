package com.totoro.AntiAbuse.core.rateLimiter;

import com.totoro.AntiAbuse.couchbase.domain.AbuseLimitDocument;
import com.totoro.AntiAbuse.couchbase.service.AbuseLimitService;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class RateLimiter {
    private int requestsLimit;
    private final Duration windowSize = Duration.ofMinutes(1);
    private Map<String, Integer> urls = new HashMap<>();

    private AbuseLimitService abuseLimitService;

    public void incrementKey(String key) throws Exception {
        LocalDateTime currentWindow = truncateToMinutes(LocalDateTime.now());
        abuseLimitService.incKey(new AbuseLimitDocument(key+"::"+currentWindow));
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
        // r.requestsLimit = x * prevRequests + currentRequests : requestsLimit 에 딱맞는 이전 요청 개수를 정하는 x 값을 계산
        // 그 다음에 (1.0 - x) * windowSize : x 값으로 현재 윈도우 시작시간부터 요청이 거부되지 않을 기간 계산
        // 그 다음에 ((1.0 - x) * windowSize) - durationFromCurrWindow : 요청이 블락되지 않을 기간에 durationFromCurrWindow 빼서 요청 거부(블락) 시간을 계산
        // ---
        // 이전 요청 값이 0 인데, 요청 거부 기간 계산이 필요하다는 것은 다음 윈도우에서 요청 거부가 해제된다는 것을 의미한다.
        // x * currentRequests + nextWindowValue = r.requestsLimit 수식으로 x 값을 계산해야 한다.
        Duration limitDuration;
        if (prevRequests == 0) {
            // currentRequests 가 prevRequests 가 되고, 요청은 계속 거부되어서 currentRequests 는 0 인 다음 윈도우에서 차단 해제 된다고 가정
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
