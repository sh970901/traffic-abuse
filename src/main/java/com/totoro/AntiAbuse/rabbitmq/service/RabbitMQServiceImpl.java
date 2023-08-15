package com.totoro.AntiAbuse.rabbitmq.service;

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
    //TODO 업데이트 된 Rule, BlackList를 실시간으로 받아 처리하기 위한 큐를 사용
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    /**
     * Queue로 메시지를 발행
     *
     * @param ruleDto 발행할 메시지의 DTO 객체
     */
//    public void sendMessage(RuleMessage ruleDto) {
//        log.info("message sent: {}", ruleDto.toString());
//        rabbitTemplate.convertAndSend(exchangeName, routingKey, ruleDto);
//    }

    /**
     * Queue에서 메시지를 구독
     *
     * @param ruleDto 구독한 메시지를 담고 있는 RuleMessage 객체
     */
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receivedMessage(RuleMessage ruleDto) {
        log.info("Received message: {}", ruleDto.toString());
    }
}
