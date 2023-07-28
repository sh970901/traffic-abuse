package com.totoro.AntiAbuse.abusing.domain;

import com.couchbase.client.core.deps.com.google.gson.JsonObject;
import com.totoro.AntiAbuse.abusing.dto.AbuseRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Data
@Document
public class AbuseLog {
    @Id @GeneratedValue
    private Long id;
    @Field
    private String type;
    @Field
    private long count;
    @Field
    private String date;
    @Field
    private String mbId;
    @Field
    private String pcId;
    @Field
    private String fsId;
    @Field
    private String remoteAddr;
    @Field
    private String url;
    @Field
    private String userAgent;
    @Field
    private String domain;

    private String key;
    public AbuseLog(AbuseRequestDto req, String type) {
        createNewLog(req, type);
    }
    public AbuseLog() {

    }

    public AbuseLog(Long count) {

    }

    public String generateId() {
        if(this.key != null) {
            return key;
        }
        String key = date + "::" + type + "::" + remoteAddr + "::" + url;
        if (pcId != null && !pcId.isEmpty()) {
            key += "::" + pcId;
        }
        this.key = key;

        return key;
    }

    private static AbuseLog createNewLog(AbuseRequestDto req, String type) {
        AbuseLog log = new AbuseLog();
        log.setType(type);
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
