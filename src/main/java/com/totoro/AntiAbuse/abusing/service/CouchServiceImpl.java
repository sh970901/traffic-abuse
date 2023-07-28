package com.totoro.AntiAbuse.abusing.service;

import com.totoro.AntiAbuse.abusing.repository.CouchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouchServiceImpl {
    private final CouchRepository couchRepository;


}
