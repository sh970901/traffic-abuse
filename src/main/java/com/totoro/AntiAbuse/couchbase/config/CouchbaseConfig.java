package com.totoro.AntiAbuse.couchbase.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
//@EnableCouchbaseRepositories(basePackages={"com.totoro.AntiAbuse"})
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {
    @Override
    public String getConnectionString() {
        return "couchbase://127.0.0.1";
    }

    @Override
    public String getBucketName() {
        return "abuse_log";
    }

    @Override
    public String getUserName() {
        return "dev-admin";
    }

    @Override
    public String getPassword() {
        return "dev-admin";
    }
}
