package com.totoro.AntiAbuse.abusing.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class AbuseLogDTO {
    private String type;
    private int count;
    private String date;
    private String mbId;
    private String remoteAddr;
    private String url;
    private String userAgent;
    private String domain;

    public AbuseLogDTO(String type, String mbId, String remoteAddr, String url, String userAgent, String domain) {
        this.type = type;
        this.mbId = mbId;
        this.remoteAddr = remoteAddr;
        this.url = url;
        this.userAgent = userAgent;
        this.domain = domain;
        this.count = 1;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public String generateId(String pcId) {
        String key = this.date + "::" + this.type + "::" + this.remoteAddr + "::" + this.url;
        if (pcId != null && !pcId.isEmpty()) {
            key += "::" + pcId;
        }
        return key;
    }
}
