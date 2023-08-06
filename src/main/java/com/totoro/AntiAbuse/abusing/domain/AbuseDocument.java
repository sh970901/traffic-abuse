package com.totoro.AntiAbuse.abusing.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
@Document
public class AbuseDocument implements Serializable {
    private static final long serialVersionUID = 7330101427517450936L;

    @Id
    @GeneratedValue(strategy = GenerationStrategy.USE_ATTRIBUTES, delimiter = "#")
    private String id;

    @Field
    @IdAttribute(order=0)
    private String type;

    @Field
    @IdAttribute(order=1)
    private String type2;

}
