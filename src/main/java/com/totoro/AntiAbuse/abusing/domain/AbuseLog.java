package com.totoro.AntiAbuse.abusing.domain;

import com.couchbase.client.core.deps.com.google.gson.JsonObject;
import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class AbuseLog {
    @Id @GeneratedValue
    private Long id;

    private String type;
    private long count;
    private String date;
    private String mbId;
    private String pcId;
    private String fsId;
    private String fsId1;
    private String remoteAddr;
    private String url;
    private String userAgent;
    private String domain;
    public AbuseLog(AbuseRequestDTO req, String userAgent) {
        createNewLog(req, userAgent);
    }
    public AbuseLog() {

    }

    public AbuseLog(Long count) {

    }

    public String generateId() {
        String key = date + "::" + type + "::" + remoteAddr + "::" + url;
        if (pcId != null && !pcId.isEmpty()) {
            key += "::" + pcId;
        }
        return key;
    }

    private static AbuseLog createNewLog(AbuseRequestDTO req, String t) {
        AbuseLog log = new AbuseLog();
        log.setType(t);
        log.setCount(1);
        log.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        log.setPcId(req.getPcId());
        log.setFsId(req.getFsId());
        log.setRemoteAddr(req.getRemoteAddr());
        log.setUrl(req.getUrl());
        log.setUserAgent(req.getUserAgent());
        log.setDomain(req.getDomain());

        return log;
    }

    public String toJson() {
        return String.valueOf(new JsonObject());
    }
}
