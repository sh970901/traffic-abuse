package com.totoro.AntiAbuse.tools.storage;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Limit {

    private long count;


    public void incrementKey(String key, LocalDateTime window){

    }
    public long[] get(String key, LocalDateTime previousWindow, LocalDateTime currentWindow){
        return null;
    }
}
