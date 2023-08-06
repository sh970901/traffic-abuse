package com.totoro.AntiAbuse.couchbase.repository;


import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CouchRepository <T> extends CrudRepository<T, Long> {
    Optional<T> findById(String id);
}
