package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseRuleDocument;

public interface AbuseRuleService extends CouchService<AbuseRuleDocument>{

    void upsertRule(AbuseRuleDocument rule);

}
