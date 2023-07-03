package com.totoro.AntiAbuse.abusing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbuseResponseDTO {
    private boolean block;
    private String blockTime;
    private String remainLimit;
    private String message;

    private AbuseResponseDTO(Boolean block){
        this.block = block;
    }

    // 요청을 어뷰징으로 판단
    public static AbuseResponseDTO abuse(){
        return new AbuseResponseDTO(true);
    }
    
    //요청을 정상으로 판단
    public static AbuseResponseDTO nonAbuse(){
        return new AbuseResponseDTO(false);
    }
}
