package com.totoro.AntiAbuse.couchbase.service;

import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.MutateInSpec;
import com.totoro.AntiAbuse.abusing.domain.AbuseLog;
import com.totoro.AntiAbuse.abusing.domain.LogDocument;
import com.totoro.AntiAbuse.couchbase.repository.CouchRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouchServiceImpl implements CouchService<LogDocument> {
    private final CouchRepository couchRepository;

    @Override public void addData(LogDocument log) {
        couchRepository.save(log);
    }
    //    public void addLog(AbuseLog log) {
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
