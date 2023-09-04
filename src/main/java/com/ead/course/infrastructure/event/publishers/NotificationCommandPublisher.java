package com.ead.course.infrastructure.event.publishers;

import com.ead.course.infrastructure.event.dtos.NotificationCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${ead.broker.exchange.notificationCommandExchange}")
    private String notificationCommandExchange;


    @Value("${ead.broker.key.notificationCommandKey}")
    private String notificationCommandKey;

    public void publichNotificationCommand(NotificationCommandDto commandDto) {
        rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey, commandDto);
    }

}
