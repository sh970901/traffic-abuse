package com.totoro.AntiAbuse.rabbitmq.service;

import com.totoro.AntiAbuse.couchbase.domain.AbuseRuleDocument;
import com.totoro.AntiAbuse.couchbase.service.AbuseRuleService;
import com.totoro.AntiAbuse.rabbitmq.dto.RuleMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQServiceImpl implements RabbitMQService{

    private final AbuseRuleService abuseRuleService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receivedMessage(RuleMessage rule) {
        log.info("Received message: {}", rule.toString());
        AbuseRuleDocument[] rules = AbuseRuleDocument.convertMsgToDocument(rule);
        abuseRuleService.upsertRule(rules[0]);
        abuseRuleService.upsertRule(rules[1]);
    }
}
