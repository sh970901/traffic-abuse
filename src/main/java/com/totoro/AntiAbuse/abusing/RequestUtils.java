package com.totoro.AntiAbuse.abusing;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class RequestUtils {
    public static final List<String> whiteUserAgent = Arrays.asList(
            "http://naver.me/spd",
            "http://www.google.com/bot.html"
    );

    public static final List<String> blackUserAgent = Arrays.asList(
            "scrapy", "Java", "python", "Apache-HttpClient",
            "The Knowledge AI", "Snoopy"
    );

    public static String getIpAddress(String temp) {
        int a = Integer.parseInt(temp.substring(0, 2), 36);
        int b = Integer.parseInt(temp.substring(2, 4), 36);
        int c = Integer.parseInt(temp.substring(4, 6), 36);
        int d = Integer.parseInt(temp.substring(6, 8), 36);
        return a + "." + b + "." + c + "." + d;
    }

    public static boolean isIpv4Net(String temp) {
        String remoteAddr = getIpAddress(temp);
        try {
            InetAddress inetAddress = InetAddress.getByName(remoteAddr);
            return inetAddress instanceof Inet4Address;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public static LocalDateTime timestampToTime(String temp) {
        long z = Long.parseLong(temp, 36);
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(z), ZoneId.systemDefault());
    }

    public static boolean isCorrectTimeWithinRange(LocalDateTime fsId1Time) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime ago = today.minusDays(1);
        LocalDateTime later = today.plusDays(1);
        return fsId1Time.isAfter(ago) && fsId1Time.isBefore(later);
    }

    public static boolean isCorrectTime(LocalDateTime fsIdTime, LocalDateTime fsId1Time) {
        return fsIdTime.isEqual(fsId1Time) || fsIdTime.isBefore(fsId1Time);
    }

    public static boolean isWhiteUserAgent(String userAgent) {
        return whiteUserAgent.stream().anyMatch(userAgent::contains);
    }

    public static boolean isBlackUserAgent(String userAgent) {
        return blackUserAgent.stream().anyMatch(agent -> userAgent.toLowerCase().contains(agent.toLowerCase()));
    }
}
