package com.totoro.AntiAbuse.couchbase.service;

public interface CouchService<T> {

    T getData(String id);

    T save(T data);

}
