package com.totoro.AntiAbuse.abusing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableCouchbaseRepositories(basePackages={"com.totoro.AntiAbuse"})
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {
    @Override
    public String getConnectionString() {
        return "couchbase://dev-couchbase.elandmall.co.kr";
    }

    @Override
    public String getBucketName() {
        return "sample_bucket";
    }

    @Override
    public String getUserName() {
        return "dev_admin";
    }

    @Override
    public String getPassword() {
        return "dev_admin";
    }
}
