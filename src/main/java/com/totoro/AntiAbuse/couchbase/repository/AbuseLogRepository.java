package com.totoro.AntiAbuse.couchbase.repository;


import com.totoro.AntiAbuse.abusing.domain.AbuseLogDocument;
import org.springframework.data.repository.CrudRepository;


public interface AbuseLogRepository extends CouchRepository<AbuseLogDocument>{
}
