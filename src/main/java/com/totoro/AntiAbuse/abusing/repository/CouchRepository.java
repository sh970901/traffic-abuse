package com.totoro.AntiAbuse.abusing.repository;

import com.totoro.AntiAbuse.abusing.domain.AbuseLog;
import org.springframework.data.repository.CrudRepository;

public interface CouchRepository extends CrudRepository<AbuseLog, Long> {
}
