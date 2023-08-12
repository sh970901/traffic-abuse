package com.totoro.AntiAbuse.couchbase.domain;

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
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@Document
public class AbuseLogDocument implements Serializable {
    private static final long serialVersionUID = 7330101127517450930L;

    @Id
    @GeneratedValue(strategy = GenerationStrategy.USE_ATTRIBUTES, delimiter = "#")
    private String id;

    @Field
    @CreatedDate
    @IdAttribute(order=0)
    private String date;

    @Field
    @IdAttribute(order=1)
    private String type;

    @Field
    private long count;

    @Field
    @IdAttribute(order=1)
    private String mbId;

    @Field
    @IdAttribute(order=4)

    private String pcId;

    @Field
    private String fsId;

    @Field
    @IdAttribute(order=2)
    private String remoteAddr;

    @Field
    @IdAttribute(order=3)
    private String url;

    @Field
    private String userAgent;

    @Field
    private String domain;


    public String generateId() {
        String key = date + "#" + type + "#" + remoteAddr + "#" + url;
        if (pcId != null && !pcId.isEmpty()) {
            key += "::" + pcId;
        }
        return key;
    }

    public static AbuseLogDocument convertDtoToDocument(AbuseLogDto logDto){
        return AbuseLogDocument.builder()
                .type(logDto.getType())
                .date(logDto.getDate())
                .mbId(logDto.getMbId())
                .pcId(logDto.getPcId())
                .fsId(logDto.getFsId())
                .remoteAddr(logDto.getRemoteAddr())
                .url(logDto.getUrl())
                .userAgent(logDto.getUserAgent())
                .domain(logDto.getDomain())
                .build();
    }
}
