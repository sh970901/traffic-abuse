package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseRuleDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbuseRuleService implements CouchService<AbuseRuleDocument> {
    private final AbuseRuleRepository abuseRuleRepository;

    @Override
    public void addData(AbuseRuleDocument abuse) {
        abuseRuleRepository.save(abuse);
    }

    @Override
    public AbuseRuleDocument getData(String id) {
        return null;
    }

    @Override
    public AbuseRuleDocument saveForce(AbuseRuleDocument data) {
        return null;
    }
}
