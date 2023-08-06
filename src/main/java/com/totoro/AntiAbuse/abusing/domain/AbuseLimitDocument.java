package com.totoro.AntiAbuse.abusing.domain;

import com.totoro.AntiAbuse.tools.storage.AbuseLimit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class AbuseLimitDocument implements Serializable {
    private static final long serialVersionUID = 7330101127517450935L;

    @Id
    @GeneratedValue(strategy = GenerationStrategy.USE_ATTRIBUTES, delimiter = "#")
    private String id;

    @Field
    @IdAttribute(order=0)
    private String remoteAddr;

    @Field
    @IdAttribute(order=1)
    private String url;

    @Field
    @IdAttribute(order=2)
    private String pcId;

    @Field
    @CreatedDate
    @IdAttribute(order=3)
    private String date;

    @Field
    private AbuseLimit limit;

}
