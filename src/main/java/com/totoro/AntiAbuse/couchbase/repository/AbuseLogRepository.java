package com.totoro.AntiAbuse.couchbase.repository;


import com.totoro.AntiAbuse.couchbase.domain.AbuseLogDocument;
import org.springframework.data.couchbase.repository.Query;

import java.util.Optional;


public interface AbuseLogRepository extends CouchRepository<AbuseLogDocument>{

    @Query("UPDATE abuse_log USE KEYS $1 SET count = count + $2 RETURNING count")
    Optional<Integer> incrementCountById(String id, int i);
}
