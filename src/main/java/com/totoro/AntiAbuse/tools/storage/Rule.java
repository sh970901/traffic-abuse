package com.totoro.AntiAbuse.tools.storage;

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
    private int firstVisitLimit;
    private List<AbuseDomain> domains;
    private List<String> keywords;
    private List<String> notKeywords;
    private List<String> whiteUserAgent;
    private List<String> blackUserAgent;

    public Rule(int requestsLimit, List<String> whiteUserAgent, List<String> blackUserAgent) {
        this.requestsLimit = requestsLimit;
        this.domains = new ArrayList<>();
        this.keywords = new ArrayList<>();
        this.notKeywords = new ArrayList<>();
        this.whiteUserAgent = whiteUserAgent;
        this.blackUserAgent = blackUserAgent;
    }

//    public static Rule fromJson(String toString) {
//        return new Rule(5);
//    }

}
