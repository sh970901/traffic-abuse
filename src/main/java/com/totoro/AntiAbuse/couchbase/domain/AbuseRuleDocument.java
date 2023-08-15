package com.totoro.AntiAbuse.couchbase.domain;

import com.totoro.AntiAbuse.rabbitmq.dto.RuleMessage;
import com.totoro.AntiAbuse.tools.storage.Blacklist;
import com.totoro.AntiAbuse.tools.storage.Rule;
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
public class AbuseRuleDocument implements Serializable {
    private static final long serialVersionUID = 7330101427517450936L;

    @Id
    @GeneratedValue(strategy = GenerationStrategy.USE_ATTRIBUTES)
    private String id;

    @Field
    @IdAttribute(order=0)
    private String type;

    @Field
    private Rule rule;

    @Field
    private Blacklist blacklist;

    public static AbuseRuleDocument[] convertMsgToDocument(RuleMessage msg){
        AbuseRuleDocument rule = AbuseRuleDocument.builder()
                .type("rule")
                .rule(msg.getRule())
                .build();

        AbuseRuleDocument blacklist = AbuseRuleDocument.builder()
                .type("blacklist")
                .blacklist(msg.getBlacklist())
                .build();

        return new AbuseRuleDocument[]{rule, blacklist};
    }

}
