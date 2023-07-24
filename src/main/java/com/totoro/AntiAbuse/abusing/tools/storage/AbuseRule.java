package com.totoro.AntiAbuse.abusing.tools.storage;

import com.couchbase.client.core.deps.com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AbuseRule {
    private long requestsLimit;
    private long firstVisitLimit;
    private List<AbuseDomain> domains;
    private List<String> keywords;
    private List<String> notKeywords;
    private List<String> whiteUserAgent;
    private List<String> blackUserAgent;


    public static AbuseRule fromJson(String toString) {
        return new AbuseRule();
    }

    public String  toJson() {
        Gson gson = new Gson();
        return "gg";
    }
}
