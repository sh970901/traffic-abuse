package com.totoro.AntiAbuse.abusing.domain;

import com.totoro.AntiAbuse.abusing.dto.AbuseLogDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

@Builder
@Data
@AllArgsConstructor
@Document
public class LogDocument {
    @Id
//    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private String type;
    @Field
    private long count;
    @Field
    @CreatedDate
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

    public static LogDocument convertDtoToDocument(AbuseLogDto logDto){
        return LogDocument.builder()
                .id(logDto.generateId())
                .type(logDto.getType())
                .count(logDto.getCount())
                .date(logDto.getDate())
                .mbId(logDto.getMbId())
                .pcId(logDto.getPcId())
                .fsId(logDto.getFsId())
                .remoteAddr(logDto.getRemoteAddr())
                .url(logDto.getUrl())
                .userAgent(logDto.getUserAgent())
                .domain(logDto.getDomain())
                .key(logDto.getKey())
                .build();
    }
}
