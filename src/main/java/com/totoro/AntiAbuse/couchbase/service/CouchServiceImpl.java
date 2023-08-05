package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.abusing.domain.LogDocument;
import com.totoro.AntiAbuse.abusing.dto.AbuseLogDto;
import com.totoro.AntiAbuse.couchbase.repository.CouchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouchServiceImpl implements CouchService<LogDocument> {
    private final CouchRepository couchRepository;

    @Override
    public void addData(LogDocument log) {
        LogDocument logDocument = couchRepository.findById(log.generateId()).orElse(null);
        if( logDocument == null ){
            couchRepository.save(log);
        }
        else {
            logDocument.setCount(logDocument.getCount() + 1);
            couchRepository.save(logDocument);
        }
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
