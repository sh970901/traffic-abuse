package com.totoro.AntiAbuse.core;

import com.totoro.AntiAbuse.abusing.domain.AbuseLimitDocument;
import com.totoro.AntiAbuse.couchbase.service.CouchService;
import com.totoro.AntiAbuse.tools.storage.AbuseLimitStore;
import com.totoro.AntiAbuse.tools.storage.LimitStatus;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@Getter
public class RateLimiter {
    private int requestsLimit;
    private final Duration windowSize = Duration.ofMinutes(1);
    private Map<String, Integer> urls;

    @Autowired
    private CouchService<AbuseLimitDocument> abuseLimitService;

    public void incrementKey(String key) throws Exception {
        LocalDateTime currentWindow = truncateToMinutes(LocalDateTime.now());
        abuseLimitService.addData(new AbuseLimitDocument(key+"::"+currentWindow));
    }

    // ToDo: Exception 처리, 이전 요청이 없을 경우 모두 PASS 되는 걸 막아야함

    public LimitStatus check(String key) throws Exception {
        LocalDateTime currentWindowStartTime = truncateToMinutes(LocalDateTime.now()); // 현재 윈도우 시작 시간
        LocalDateTime prevWindowStartTime = currentWindowStartTime.minus(windowSize); //이전 윈도우 시작 시간
        LimitStatus limitStatus;

        //1분에 하나씩 limitDocument를 생성하고 count를 증가하는 방식
        long prevRequests, currentRequests;
        try {
//            long[] requests = dataStore.get(key, prevWindowStartTime, currentWindowStartTime);
            prevRequests = abuseLimitService.getData(key+"::"+prevWindowStartTime).getCount();
            currentRequests = abuseLimitService.getData(key+"::"+currentWindowStartTime).getCount();
        }
        catch (NullPointerException e){
            limitStatus = new LimitStatus();
            limitStatus.setLimited(false);
            return limitStatus;
        }
        catch (Exception e) {
            throw new Exception("Error while getting requests count from data store", e);
        }

        Duration durationFromCurrWindow = Duration.between(currentWindowStartTime, LocalDateTime.now());

        double rate = ((windowSize.toMillis() - durationFromCurrWindow.toMillis()) / (double) windowSize.toMillis()) * prevRequests + currentRequests;
        limitStatus = new LimitStatus();

        requestsLimit = 5;
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
