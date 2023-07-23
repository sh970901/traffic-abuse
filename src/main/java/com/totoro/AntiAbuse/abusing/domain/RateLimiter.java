package com.totoro.AntiAbuse.abusing.domain;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;

@Component
public class RateLimiter {
    private LimitStore dataStore;
    private int requestsLimit;
    private Duration windowSize;
    private Map<String, Integer> urls;

    public RateLimiter(LimitStore dataStore, int requestsLimit, Map<String, Integer> urls) {
        this.dataStore = dataStore;
        this.requestsLimit = requestsLimit;
        this.windowSize = Duration.ofMinutes(1); // 1분 단위로 윈도우 크기 설정
        this.urls = urls;
    }
    public RateLimiter(){

    }
    public void incrementKey(String message) throws Exception {
        LocalDateTime currentWindow = truncateToMinutes(LocalDateTime.now());
        dataStore.incrementKey(message, currentWindow);
    }

    // ToDo: Exception 처리

    public LimitStatus check(String message) throws Exception {
        LocalDateTime currentWindowStartTime = truncateToMinutes(LocalDateTime.now()); // 현재 윈도우 시작 시간
        LocalDateTime prevWindowStartTime = currentWindowStartTime.minus(windowSize); //이전 윈도우 시작 시간

        long prevRequests, currentRequests;
        try {
            long[] requests = dataStore.get(message, prevWindowStartTime, currentWindowStartTime);
            prevRequests = requests[0];
            currentRequests = requests[1];
        } catch (Exception e) {
            throw new Exception("Error while getting requests count from data store", e);
        }

        Duration durationFromCurrWindow = Duration.between(currentWindowStartTime, LocalDateTime.now());

        double rate = ((windowSize.toMillis() - durationFromCurrWindow.toMillis()) / (double) windowSize.toMillis()) * prevRequests + currentRequests;
        LimitStatus limitStatus = new LimitStatus();

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
