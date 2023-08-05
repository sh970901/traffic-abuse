package com.totoro.AntiAbuse.abusing.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class AbuseLogDto {
    private String id;
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime date;
}
