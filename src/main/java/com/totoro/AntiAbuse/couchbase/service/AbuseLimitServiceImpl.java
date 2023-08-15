package com.totoro.AntiAbuse.couchbase.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseLimitDocument;
import com.totoro.AntiAbuse.couchbase.repository.AbuseLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbuseLimitServiceImpl implements AbuseLimitService {
    private final AbuseLimitRepository limitRepository;

    @Override
    public AbuseLimitDocument getData(String id) {
        return limitRepository.findById(id).orElse(null);
    }

    @Override
    public AbuseLimitDocument save(AbuseLimitDocument data) {
        return limitRepository.save(data);
    }


    @Override
    public void incKey(AbuseLimitDocument limit) {
        String id = limit.getId();
        AbuseLimitDocument existingLimit = limitRepository.findById(id).orElse(null);

        if (existingLimit == null) {
            // Document doesn't exist, create a new one with count = 1
            limit.setCount(1);
        } else {
            // Document exists, increment the count value by 1
            limit.setCount(existingLimit.getCount() + 1);
        }

        limitRepository.save(limit);
    }
}
