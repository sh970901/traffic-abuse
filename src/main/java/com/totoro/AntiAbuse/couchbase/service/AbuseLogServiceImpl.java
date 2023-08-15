package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseLogDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbuseLogServiceImpl implements AbuseLogService {
    private final AbuseLogRepository logRepository;
    @Override
    public void saveCount(AbuseLogDocument log) {
        String id = log.generateId();
        AbuseLogDocument existingLog = logRepository.findById(id).orElse(null);

        if (existingLog == null) {
            // Document doesn't exist, create a new one with count = 1
            log.setCount(1);
        } else {
            // Document exists, increment the count value by 1
            log.setCount(existingLog.getCount() + 1);
        }

        logRepository.save(log);
    }


    public AbuseLogDocument getData(String id){
        return logRepository.findById(id).orElse(null);
    }

    @Override
    public AbuseLogDocument save(AbuseLogDocument logDocument){
        return logRepository.save(logDocument);
    }


}
