package com.totoro.AntiAbuse.tools.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Rule {
    private int requestsLimit;
    private long firstVisitLimit;
    private List<AbuseDomain> domains;
    private List<String> keywords;
    private List<String> notKeywords;
    private List<String> whiteUserAgent;
    private List<String> blackUserAgent;

    public Rule(int i) {
        this.requestsLimit = i;
        this.domains = new ArrayList<>();
        this.keywords = new ArrayList<>();
        this.notKeywords = new ArrayList<>();
        this.whiteUserAgent = new ArrayList<>();
        this.blackUserAgent = new ArrayList<>();
    }

    public static Rule fromJson(String toString) {
        return new Rule(5);
    }

}
