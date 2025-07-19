package com.remotehub.userservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class InviteProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.invite-exchange}")
    private String inviteExchange;

    @Value("${app.rabbitmq.invite-routing-key}")
    private String inviteRoutingKey;

    public InviteProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendInviteMessage(UUID inviteId) {
        rabbitTemplate.convertAndSend(inviteExchange, inviteRoutingKey, inviteId.toString());
    }
}
