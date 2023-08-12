package com.totoro.AntiAbuse.tools.storage;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class LimitStatus {
    private boolean isLimited;
    private Duration limitDuration;
    private double currentRate;
    private long currentRemainRequests;
}
