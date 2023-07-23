package com.totoro.AntiAbuse.abusing.domain;

import java.time.LocalDateTime;

public interface LimitStore {
    void incrementKey(String message, LocalDateTime window) throws Exception;
    long[] get(String message, LocalDateTime previousWindow, LocalDateTime currentWindow) throws Exception;
}
