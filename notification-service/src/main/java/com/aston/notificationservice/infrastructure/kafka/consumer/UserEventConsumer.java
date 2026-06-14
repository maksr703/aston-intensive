package com.aston.notificationservice.infrastructure.kafka.consumer;

import com.aston.events.UserCreatedEvent;
import com.aston.events.UserDeletedEvent;
import com.aston.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "user.created", groupId = "notification-group")
    public void handleUserCreated(SpecificRecord event) {

        UserCreatedEvent user = (UserCreatedEvent) event;

        emailService.sendEmail(
                user.getEmail(),
                "Welcome",
                "Ваш аккаунт создан"
        );
    }

    @KafkaListener(topics = "user.deleted", groupId = "notification-group")
    public void handleUserDeleted(SpecificRecord event) {

        UserDeletedEvent user = (UserDeletedEvent) event;

        emailService.sendEmail(
                user.getEmail(),
                "Goodbye",
                "Аккаунт удалён"
        );
    }
}
