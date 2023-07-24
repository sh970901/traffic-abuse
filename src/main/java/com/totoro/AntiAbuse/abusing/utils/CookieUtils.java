package com.totoro.AntiAbuse.abusing.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtils {
    public static String getCookieValue(Cookie[] cookies, String key){
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();

            if (key.equals(name) && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

}
