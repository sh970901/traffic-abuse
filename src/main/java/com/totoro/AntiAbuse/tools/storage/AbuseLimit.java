package com.totoro.AntiAbuse.tools.storage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbuseLimit {
    private long count;

    public AbuseLimit(Long count) {

    }
}
