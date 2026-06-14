package com.aston.notificationservice.infrastructure.kafka.consumer;

import com.aston.notificationservice.dto.UserCreatedEvent;
import com.aston.notificationservice.dto.UserDeletedEvent;
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
        emailService.sendEmail(event.email(), "Welcome", "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");
    }

    @KafkaListener(topics = "user.deleted", groupId = "notification-group")
    public void handleUserDeleted(UserDeletedEvent event) {
        emailService.sendEmail(event.email(), "Adios", "Здравствуйте! Ваш аккаунт был удалён.");
    }
}
