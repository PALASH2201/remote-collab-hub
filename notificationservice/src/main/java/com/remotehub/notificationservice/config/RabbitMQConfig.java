package com.remotehub.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange discussionExchange() {
        return new TopicExchange("discussion.exchange");
    }

    @Bean
    public Queue commentAddedQueue() {
        return new Queue("comment.added.queue", true); // durable = true
    }

    @Bean
    public Binding binding(Queue commentAddedQueue, TopicExchange discussionExchange) {
        return BindingBuilder.bind(commentAddedQueue)
                .to(discussionExchange)
                .with("comment.added");
    }
}

