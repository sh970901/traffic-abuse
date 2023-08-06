package com.totoro.AntiAbuse.tools.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;

import java.time.LocalDateTime;

@Getter
@Setter
public class AbuseLimit {

    private long count;


    public void incrementKey(String key, LocalDateTime window){

    }
    public long[] get(String key, LocalDateTime previousWindow, LocalDateTime currentWindow){
        return null;
    }
}
