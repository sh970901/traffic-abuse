package com.totoro.AntiAbuse.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutateInSpec;
import com.totoro.AntiAbuse.tools.storage.AbuseLimit;
import com.totoro.AntiAbuse.abusing.domain.AbuseLog;
import com.totoro.AntiAbuse.tools.storage.AbuseRule;
import com.totoro.AntiAbuse.tools.storage.Blacklist;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

//@Component
public class CouchbaseClient {

    private Cluster cluster;
    private Bucket limitBucket;
    private Bucket abuseBucket;
    private Bucket logBucket;

    public CouchbaseClient(Cluster cluster) {
        this.cluster = cluster;
        this.limitBucket = cluster.bucket("abuse-limit");
        this.abuseBucket = cluster.bucket("abuse");
        this.logBucket = cluster.bucket("abuse-log");
    }
    public CouchbaseClient(){}

    public boolean exist(String id) {
        return limitBucket.defaultCollection().exists(id).exists();
    }

    public boolean existLog(String id) {
        return logBucket.defaultCollection().exists(id).exists();
    }

    public boolean existAbuse(String id) {
        return abuseBucket.defaultCollection().exists(id).exists();
    }

    public void addFirstVisit(AbuseLog log) {
        String id = log.generateId();
        JsonObject jsonObject = JsonObject.fromJson(log.toJson());
        if (!exist(id)) {
            limitBucket.defaultCollection().insert(id, jsonObject);
        } else {
            Collection collection = limitBucket.defaultCollection();
            collection.mutateIn(id, Collections.singletonList(MutateInSpec.increment("count", 1)));
        }
    }

    public void addLog(AbuseLog log) {
        String id = log.generateId();
        JsonObject jsonObject = JsonObject.fromJson(log.toJson());
        if (!existLog(id)) {
            logBucket.defaultCollection().insert(id, jsonObject);
        } else {
            Collection collection = logBucket.defaultCollection();
            collection.mutateIn(id, Collections.singletonList(MutateInSpec.increment("count", 1)));
        }
    }

    public AbuseLimit getLimit(String id) {
        GetResult getResult = limitBucket.defaultCollection().get(id);
        JsonObject jsonObject = getResult.contentAsObject();
        return new AbuseLimit(jsonObject.getLong("count"));
    }

    public AbuseLog getFirstVisit(String id) {
        GetResult getResult = limitBucket.defaultCollection().get(id);
        JsonObject jsonObject = getResult.contentAsObject();
        return new AbuseLog(jsonObject.getLong("count"));
    }

    public void inc(String message, LocalDateTime window) {
        String id = documentId(message, window);
        if (!exist(id)) {
            JsonObject jsonObject = JsonObject.create().put("count", 1L);
            limitBucket.defaultCollection().insert(id, jsonObject);
        } else {
            Collection collection = limitBucket.defaultCollection();
            collection.mutateIn(id, Collections.singletonList(MutateInSpec.increment("count", 1)));
        }
    }

    public void addBlacklist(Blacklist blacklist) {
        String id = "blacklist";
        JsonObject jsonObject = JsonObject.fromJson(blacklist.toJson());
        if (!existAbuse(id)) {
            abuseBucket.defaultCollection().insert(id, jsonObject);
        } else {
            abuseBucket.defaultCollection().replace(id, jsonObject);
        }
    }

    public Blacklist getBlacklist() {
        GetResult getResult = abuseBucket.defaultCollection().get("blacklist");
        JsonObject jsonObject = getResult.contentAsObject();
        return Blacklist.fromJson(jsonObject.toString());
    }

    public void addRule(AbuseRule rule) {
        String id = "rule";
        JsonObject jsonObject = JsonObject.fromJson(rule.toJson());
        if (!existAbuse(id)) {
            abuseBucket.defaultCollection().insert(id, jsonObject);
        } else {
            abuseBucket.defaultCollection().replace(id, jsonObject);
        }
    }

    public AbuseRule getRule() {
        GetResult getResult = abuseBucket.defaultCollection().get("rule");
        JsonObject jsonObject = getResult.contentAsObject();
        return AbuseRule.fromJson(jsonObject.toString());
    }

    private String documentId(String message, LocalDateTime window) {
        Instant instant = window.toInstant(ZoneOffset.UTC);
        long epochMillis = instant.toEpochMilli();
        return String.format("%s::%d", message, epochMillis);
    }
}
