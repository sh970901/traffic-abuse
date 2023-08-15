package com.totoro.AntiAbuse.abusing.dto;

import com.totoro.AntiAbuse.couchbase.domain.AbuseLogDocument;
import com.totoro.AntiAbuse.utils.TotoroToStringStyle;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AbuseLogDto {
    private Long id;
    private String type;
    private long count;
    private String date;
    private String mbId;
    private String pcId;
    private String fsId;
    private String remoteAddr;
    private String url;
    private String userAgent;
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

    public static AbuseLogDto createNewLog(AbuseRequestDto req, String type) {
        return AbuseLogDto.builder()
                .type(type)
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .pcId(req.getPcId())
                .remoteAddr(req.getRemoteAddr())
                .url(req.getUrl())
                .userAgent(req.getUserAgent())
                .domain(req.getDomain())
                .key(req.getKey())
                .build();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, TotoroToStringStyle.simpleStyle());
    }

    public AbuseLogDto convertDocumentToDto(AbuseLogDocument logDocument){
        return AbuseLogDto.builder()
                .type(logDocument.getType())
                .count(logDocument.getCount())
                .date(logDocument.getDate())
                .mbId(logDocument.getMbId())
                .pcId(logDocument.getPcId())
                .fsId(logDocument.getFsId())
                .remoteAddr(logDocument.getRemoteAddr())
                .url(logDocument.getUrl())
                .userAgent(logDocument.getUserAgent())
                .domain(logDocument.getDomain())
                .build();
    }
}
