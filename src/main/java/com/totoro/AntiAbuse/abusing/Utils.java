package com.totoro.AntiAbuse.abusing;

import jakarta.servlet.http.Cookie;

public class Utils {
    public static class CookieUtils{
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
}
