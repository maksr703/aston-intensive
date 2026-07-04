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

    private final String WELCOME = "Welcome";
    private final String ACC_CREATED = "Ваш аккаунт создан";
    private final String GOODBYE = "Goodbye";
    private final String ACC_DELETED = "Аккаунт удален";

    private static final String USER_CREATED_TOPIC = "user-created";
    private static final String USER_DELETED_TOPIC = "user-deleted";

    @KafkaListener(topics = USER_CREATED_TOPIC, groupId = "notification-group")
    public void handleUserCreated(SpecificRecord event) {

        UserCreatedEvent user = (UserCreatedEvent) event;

        emailService.sendEmail(
                user.getEmail(),
                WELCOME,
                ACC_CREATED
        );
    }

    @KafkaListener(topics = USER_DELETED_TOPIC, groupId = "notification-group")
    public void handleUserDeleted(SpecificRecord event) {

        UserDeletedEvent user = (UserDeletedEvent) event;

        emailService.sendEmail(
                user.getEmail(),
                GOODBYE,
                ACC_DELETED
        );
    }
}
