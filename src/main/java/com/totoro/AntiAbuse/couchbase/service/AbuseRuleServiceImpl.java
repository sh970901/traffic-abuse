package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseRuleDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbuseRuleServiceImpl implements AbuseRuleService {
    private final AbuseRuleRepository abuseRuleRepository;
    private final CouchbaseTemplate ruleTemplate;

    @Override
    public AbuseRuleDocument getData(String id) {
        return abuseRuleRepository.findById(id).orElse(null);
    }

    @Override
    public AbuseRuleDocument save(AbuseRuleDocument data) {
        return abuseRuleRepository.save(data);
    }

    @Override
    public void upsertRule(AbuseRuleDocument rule) {
        ruleTemplate.upsertById(AbuseRuleDocument.class).one(rule);
    }

}

