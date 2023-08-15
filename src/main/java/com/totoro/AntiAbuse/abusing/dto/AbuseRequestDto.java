package com.totoro.AntiAbuse.abusing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totoro.AntiAbuse.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbuseRequestDto {
    private String pcId;
    private String domain;
    private String url;
    private String userAgent;
    private String remoteAddr;
    private String key;

    public static AbuseRequestDto of(HttpServletRequest request){
        String pcId = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            pcId = CookieUtils.getCookieValue(cookies,"pcid");
            pcId = (pcId == null) ? request.getSession().getId() : pcId;
        }
        return new AbuseRequestDto(pcId, request.getRemoteAddr(), request.getRequestURI(), request.getHeader("User-Agent"), request.getServerName());
    }

    private AbuseRequestDto(String pcId, String remoteAddr, String url, String userAgent, String domain) {
        this.pcId = pcId;
        this.remoteAddr = remoteAddr;
        this.url = url;
        this.userAgent = userAgent;
        this.domain = domain;
    }

    public String generateKey() {
        if(this.key != null) return this.key;

        String key = this.remoteAddr + "::" + this.url;
        if (this.pcId != null && !this.pcId.isEmpty()) {
            key += "::" + this.pcId;
        }
        this.key = key;

        return key;
    }


    @Override
    public String toString() {
        return "AbuseRequestDTO{" +
                "pcId='" + pcId + '\'' +
                ", domain='" + domain + '\'' +
                ", url='" + url + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                '}';
    }
}
