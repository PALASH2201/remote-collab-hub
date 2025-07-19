package com.remotehub.userservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.invite-exchange}")
    private String inviteExchange;

    @Value("${app.rabbitmq.invite-queue}")
    private String inviteQueue;

    @Value("${app.rabbitmq.invite-routing-key}")
    private String inviteRoutingKey;

    @Bean(name = "inviteQueue")
    public Queue inviteQueue() {
        return new Queue(inviteQueue, true);
    }

    @Bean(name = "inviteExchange")
    public DirectExchange inviteExchange() {
        return new DirectExchange(inviteExchange);
    }

    @Bean
    public Binding inviteBinding(@Qualifier("inviteQueue") Queue queue,
                                 @Qualifier("inviteExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(inviteRoutingKey);
    }

}
