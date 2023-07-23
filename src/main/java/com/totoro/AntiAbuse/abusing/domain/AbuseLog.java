package com.totoro.AntiAbuse.abusing.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class AbuseLog {
    @Id @GeneratedValue
    Long id;
}
