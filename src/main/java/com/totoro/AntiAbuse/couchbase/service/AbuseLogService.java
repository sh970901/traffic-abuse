package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.abusing.domain.AbuseLogDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbuseLogService implements CouchService<AbuseLogDocument> {
    private final AbuseLogRepository logRepository;
    @Override
    public void addData(AbuseLogDocument log) {
        AbuseLogDocument logDocument = logRepository.findById(log.generateId()).orElse(null);
        if( logDocument == null ){
            logRepository.save(log);
        }
        else {
            logDocument.setCount(logDocument.getCount() + 1);
            logRepository.save(logDocument);
        }
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
