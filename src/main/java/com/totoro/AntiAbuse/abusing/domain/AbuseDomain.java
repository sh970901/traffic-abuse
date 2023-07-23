package com.totoro.AntiAbuse.abusing.domain;

import java.util.List;

public class AbuseDomain {
    private long requestsLimit;
    private String domain;
    private List<String> urls;
}
