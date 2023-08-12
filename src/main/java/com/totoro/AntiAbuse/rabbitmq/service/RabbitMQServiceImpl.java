package com.totoro.AntiAbuse.rabbitmq.service;

import com.totoro.AntiAbuse.rabbitmq.dto.MessageDto;
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
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    /**
     * Queue로 메시지를 발행
     *
     * @param messageDto 발행할 메시지의 DTO 객체
     */
    public void sendMessage(MessageDto messageDto) {
        log.info("message sent: {}", messageDto.toString());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, messageDto);
    }

    /**
     * Queue에서 메시지를 구독
     *
     * @param messageDto 구독한 메시지를 담고 있는 MessageDto 객체
     */
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receivedMessage(MessageDto messageDto) {
        log.info("Received message: {}", messageDto.toString());
    }
}
