package com.totoro.AntiAbuse.couchbase.repository;


import com.totoro.AntiAbuse.abusing.domain.LogDocument;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CouchRepository extends CrudRepository<LogDocument, Long> {
    Optional<LogDocument> findById(String id);
}
