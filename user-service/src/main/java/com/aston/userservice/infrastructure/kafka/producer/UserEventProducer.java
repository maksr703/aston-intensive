package com.aston.userservice.infrastructure.kafka.producer;



import com.aston.events.UserCreatedEvent;
import com.aston.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private static final String USER_CREATED_TOPIC = "user-created";
    private static final String USER_DELETED_TOPIC = "user-deleted";

    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;

    public <T extends SpecificRecord> CompletableFuture<Void> sendEvent(String topic, String key, T event) {
        if (key == null || event == null) {
            System.err.println("Key or event is null for topic: " + topic);
            return CompletableFuture.completedFuture(null);
        }

        return kafkaTemplate.send(topic, key, event)
                .thenRun(() -> {
                    System.out.println("Event sent successfully to topic: " + topic);
                })
                .exceptionally(ex -> {
                    System.err.println("Failed to send event to topic " + topic + ": " + ex.getMessage());
                    return null;
                });
    }

    public CompletableFuture<Void> sendUserCreated(UserCreatedEvent event) {
        return sendEvent(USER_CREATED_TOPIC, event.getId(), event);
    }

    public CompletableFuture<Void> sendUserDeleted(UserDeletedEvent event) {
        return sendEvent(USER_DELETED_TOPIC, event.getId(), event);
    }
}
