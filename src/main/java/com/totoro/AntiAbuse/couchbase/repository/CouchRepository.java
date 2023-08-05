package com.totoro.AntiAbuse.couchbase.repository;


import com.totoro.AntiAbuse.abusing.domain.LogDocument;
import org.springframework.data.repository.CrudRepository;

public interface CouchRepository extends CrudRepository<LogDocument, Long> {
}
