package com.totoro.AntiAbuse.abusing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totoro.AntiAbuse.Utils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@ToString
public class AbuseRequestDTO {
    private String pcId;
    private String fsId;
    private String domain;
    private String url;
    private String userAgent;
    private String remoteAddr;

    public static AbuseRequestDTO of(HttpServletRequest request){
        String pcId = null;
        String fsId = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            pcId = Utils.CookieUtils.getCookieValue(cookies,"pcid");
            fsId = Utils.CookieUtils.getCookieValue(cookies,"fsid");
        }
        return new AbuseRequestDTO(pcId, fsId, request.getRemoteAddr(), request.getRequestURI(), request.getHeader("User-Agent"), request.getServerName());
    }

    private AbuseRequestDTO(String pcId, String fsId, String remoteAddr, String url, String userAgent, String domain) {
        this.pcId = pcId;
        this.fsId = fsId;
        this.remoteAddr = remoteAddr;
        this.url = url;
        this.userAgent = userAgent;
        this.domain = domain;
    }

    public String generateKey() {
        String key = this.remoteAddr + "::" + this.url;
        if (this.pcId != null && !this.pcId.isEmpty()) {
            key += "::" + this.pcId;
        }
        return key;
    }


    @Override
    public String toString() {
        return "AbuseRequestDTO{" +
                "pcId='" + pcId + '\'' +
                ", fsId='" + fsId + '\'' +
                ", domain='" + domain + '\'' +
                ", url='" + url + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                '}';
    }
}
