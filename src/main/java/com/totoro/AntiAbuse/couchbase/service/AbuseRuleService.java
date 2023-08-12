package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseRuleDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AbuseRuleService implements CouchService<AbuseRuleDocument> {
    private final AbuseRuleRepository abuseRuleRepository;

    @Override
    public void addData(AbuseRuleDocument data) {
        abuseRuleRepository.save(data);
    }

    @Override
    public AbuseRuleDocument getData(String id) {
        return abuseRuleRepository.findById(id).orElse(null);
    }

    @Override
    public AbuseRuleDocument saveForce(AbuseRuleDocument data) {
        return null;
    }
}
