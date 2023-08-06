package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.abusing.domain.AbuseDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbuseService implements CouchService<AbuseDocument> {
    private final AbuseRepository abuseRepository;

    @Override
    public void addData(AbuseDocument abuse) {
        abuseRepository.save(abuse);
    }
}
