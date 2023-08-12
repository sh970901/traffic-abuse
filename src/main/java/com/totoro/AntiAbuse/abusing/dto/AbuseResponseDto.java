package com.totoro.AntiAbuse.abusing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(builderMethodName = "from")
public class AbuseResponseDto {
    private boolean block;
    private String blockTime;

    private String message;

    private double currentRate;
    private long currentRemainRequests;

        // 요청을 어뷰징으로 판단
    public static AbuseResponseDto abuse(String blockTime, String message){
        return AbuseResponseDto.from().block(true).blockTime(blockTime).message(message).build();
    }

    //요청을 정상으로 판단
    public static AbuseResponseDto nonAbuse(String blockTime, String message, double currentRate, long currentRemainRequests){
        return AbuseResponseDto.from().block(false).blockTime(blockTime).message(message).currentRate(currentRate).currentRemainRequests(currentRemainRequests).build();
    }

}
