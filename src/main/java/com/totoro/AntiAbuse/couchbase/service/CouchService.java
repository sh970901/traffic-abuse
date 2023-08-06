package com.totoro.AntiAbuse.couchbase.service;

public interface CouchService<T> {
    void addData(T data);

    T getData(String id);

    T saveForce(T data);
}
