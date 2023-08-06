package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.abusing.domain.AbuseLogDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AbuseLogService implements CouchService<AbuseLogDocument> {
    private final AbuseLogRepository logRepository;
    @Override
    public void addData(AbuseLogDocument log) {
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

    public AbuseLogDocument saveForce(AbuseLogDocument logDocument){
        return logRepository.save(logDocument);
    }
//        public void addLog(AbuseLog log) {
//        String id = log.generateId();
//        JsonObject jsonObject = JsonObject.fromJson(log.toJson());
//        if (!existLog(id)) {
//            logBucket.defaultCollection().insert(id, jsonObject);
//        } else {
//            Collection collection = logBucket.defaultCollection();
//            collection.mutateIn(id, Collections.singletonList(MutateInSpec.increment("count", 1)));
//        }
//    }

}
