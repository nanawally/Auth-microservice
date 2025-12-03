package com.nanawally.Auth_microservice.rabbitmq;

import com.nanawally.Auth_microservice.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EmailMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRegistrationEmailMessage(String username) {
        String message = "New user registered: " + username;

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                message
        );

    }
}
