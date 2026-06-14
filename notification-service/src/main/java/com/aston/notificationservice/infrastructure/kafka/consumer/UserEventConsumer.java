package com.aston.notificationservice.infrastructure.kafka.consumer;

import com.aston.events.UserCreatedEvent;
import com.aston.events.UserDeletedEvent;
import com.aston.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "user.created", groupId = "notification-group")
    public void handleUserCreated(UserCreatedEvent event) {
        emailService.sendEmail(event.getEmail(), "Welcome", "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");
    }

    @KafkaListener(topics = "user.deleted", groupId = "notification-group")
    public void handleUserDeleted(UserDeletedEvent event) {
        emailService.sendEmail(event.getEmail(), "Adios", "Здравствуйте! Ваш аккаунт был удалён.");
    }
}
