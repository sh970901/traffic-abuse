package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseLimitDocument;


public interface AbuseLimitService extends CouchService<AbuseLimitDocument> {
    void incKey(AbuseLimitDocument limit);
}

