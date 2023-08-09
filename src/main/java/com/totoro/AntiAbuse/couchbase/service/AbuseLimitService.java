package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.abusing.domain.AbuseLimitDocument;
import com.totoro.AntiAbuse.abusing.domain.AbuseLogDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbuseLimitService implements CouchService<AbuseLimitDocument> {
    private final AbuseLimitRepository limitRepository;
    @Override
    public void addData(AbuseLimitDocument limit) {
        String id = limit.getId();
        AbuseLimitDocument existingLimit = limitRepository.findById(id).orElse(null);

        if (existingLimit == null) {
            // Document doesn't exist, create a new one with count = 1
            limit.setCount(1);
        } else {
            // Document exists, increment the count value by 1
            limit.setCount(existingLimit.getCount() + 1);
        }

        limitRepository.save(limit);
    }

    @Override
    public AbuseLimitDocument getData(String id) {
        return null;
    }

    @Override
    public AbuseLimitDocument saveForce(AbuseLimitDocument data) {
        return limitRepository.save(data);
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
