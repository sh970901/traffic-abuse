package com.totoro.AntiAbuse.rabbitmq.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {
    private String queueName;
    private String exchangeName;
    private String routingKey;
}
