package com.totoro.AntiAbuse.tools.storage;

import com.totoro.AntiAbuse.abusing.domain.AbuseLimitDocument;
import com.totoro.AntiAbuse.couchbase.service.CouchService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
