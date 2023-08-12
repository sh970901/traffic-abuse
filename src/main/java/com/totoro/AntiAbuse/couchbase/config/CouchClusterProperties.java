package com.totoro.AntiAbuse.couchbase.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "couchbase")
public class CouchClusterProperties {
    private String connectionString;
    private String userName;
    private String password;

    @NestedConfigurationProperty
    private CouchBucketProperties bucketAbuseLog;

    @NestedConfigurationProperty
    private CouchBucketProperties bucketAbuseRule;

    @NestedConfigurationProperty
    private CouchBucketProperties bucketAbuseLimit;
}
