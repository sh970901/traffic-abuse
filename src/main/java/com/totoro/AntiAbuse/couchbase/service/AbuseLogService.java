package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseLogDocument;

public interface AbuseLogService extends CouchService<AbuseLogDocument> {
    void saveCount(AbuseLogDocument log);
}
