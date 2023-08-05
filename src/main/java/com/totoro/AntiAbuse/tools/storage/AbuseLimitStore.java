package com.totoro.AntiAbuse.tools.storage;

import java.time.LocalDateTime;

public interface AbuseLimitStore {
    void incrementKey(String message, LocalDateTime window) throws Exception;
    long[] get(String message, LocalDateTime previousWindow, LocalDateTime currentWindow) throws Exception;
}
