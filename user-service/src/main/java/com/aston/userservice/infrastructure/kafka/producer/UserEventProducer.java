package com.aston.userservice.infrastructure.kafka.producer;

import com.aston.userservice.dto.UserCreatedEvent;
import com.aston.userservice.dto.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send("user.created", event.id().toString(), event);
    }

    public void sendUserDeleted(UserDeletedEvent event) {
        kafkaTemplate.send("user.deleted", event.id().toString(), event);
    }
}
