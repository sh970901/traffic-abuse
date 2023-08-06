package com.totoro.AntiAbuse.couchbase.config;

import com.totoro.AntiAbuse.abusing.domain.AbuseDocument;
import com.totoro.AntiAbuse.abusing.domain.AbuseLogDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.SimpleCouchbaseClientFactory;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

@Configuration
@RequiredArgsConstructor
//@EnableCouchbaseRepositories(basePackages={"com.totoro.AntiAbuse"})
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {
    private final CouchClusterProperties couchClusterProperties;

    @Override
    public String getConnectionString() {
        return couchClusterProperties.getConnectionString();
    }

    @Override
    public String getUserName() {
        return couchClusterProperties.getUserName();
    }

    @Override
    public String getPassword() {
        return couchClusterProperties.getPassword();
    }

    @Override
    public String getBucketName() {
        return couchClusterProperties.getBucketAbuseLog().getName();
    }

    @Override
    protected void configureRepositoryOperationsMapping(RepositoryOperationsMapping baseMapping) {
        try {
            baseMapping
                    .mapEntity(AbuseLogDocument.class, logTemplate())
                    .mapEntity(AbuseDocument.class, abuseTemplate());
        } catch (Exception e) {
            throw e;
        }
    }

    @Primary
    @Bean("logClientFactory")
    public CouchbaseClientFactory logClientFactory(){
        return new SimpleCouchbaseClientFactory(getConnectionString(),authenticator(), couchClusterProperties.getBucketAbuseLog().getName());
    }

    @Bean("logTemplate")
    public CouchbaseTemplate logTemplate(){
        return new CouchbaseTemplate(logClientFactory(), new MappingCouchbaseConverter());
    }

    @Bean("abuseClientFactory")
    public CouchbaseClientFactory abuseClientFactory(){
        return new SimpleCouchbaseClientFactory(getConnectionString(),authenticator(), couchClusterProperties.getBucketAbuse().getName());
    }

    @Bean("abuseTemplate")
    public CouchbaseTemplate abuseTemplate(){
        return new CouchbaseTemplate(abuseClientFactory(), new MappingCouchbaseConverter());
    }

}