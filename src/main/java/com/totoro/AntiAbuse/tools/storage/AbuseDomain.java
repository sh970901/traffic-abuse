package com.totoro.AntiAbuse.tools.storage;

import java.util.List;

public class AbuseDomain {
    //TODO 도메인 별 다른 requestsLimit 적용
    private long requestsLimit;
    private String domain;
    private List<String> urls;
}
