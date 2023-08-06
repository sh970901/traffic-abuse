package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.abusing.domain.AbuseLimitDocument;
import com.totoro.AntiAbuse.abusing.domain.AbuseLogDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseLimitRepository;
import com.totoro.AntiAbuse.couchbase.repository.AbuseLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbuseLimitService implements CouchService<AbuseLimitDocument> {
    private final AbuseLimitRepository limitRepository;
    @Override
    public void addData(AbuseLimitDocument limit) {
        limitRepository.save(limit);
    }

    @Override
    public AbuseLimitDocument getData(String id) {
        return null;
    }

    @Override
    public AbuseLimitDocument saveForce(AbuseLimitDocument data) {
        return null;
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
