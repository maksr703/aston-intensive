package com.aston.userservice.infrastructure.kafka.producer;



import com.aston.events.UserCreatedEvent;
import com.aston.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;

    public void sendUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send(
                "user.created",
                event.getId(),
                event
        );
    }

    public void sendUserDeleted(UserDeletedEvent event) {
        kafkaTemplate.send(
                "user.deleted",
                event.getId(),
                event
        );
    }
}
