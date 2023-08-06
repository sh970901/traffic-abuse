package com.totoro.AntiAbuse.tools.storage;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Rule {
    private long requestsLimit;
    private long firstVisitLimit;
    private List<AbuseDomain> domains;
    private List<String> keywords;
    private List<String> notKeywords;
    private List<String> whiteUserAgent;
    private List<String> blackUserAgent;

    public Rule(){
        this.domains = new ArrayList<>();
        this.keywords = new ArrayList<>();
        this.notKeywords = new ArrayList<>();
        this.whiteUserAgent = new ArrayList<>();
        this.blackUserAgent = new ArrayList<>();
    }

    public static Rule fromJson(String toString) {
        return new Rule();
    }

}
