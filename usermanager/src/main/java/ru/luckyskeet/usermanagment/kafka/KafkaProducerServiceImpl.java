package ru.luckyskeet.usermanagment.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "user-events";

    @Override
    public void sendUserCreatedEvent(Long userId) {
        if (userId != null) {
            kafkaTemplate.send(TOPIC, String.valueOf(userId));
        }
    }

    @Override
    public void sendUserDeletedEvent(Long userId) {
        kafkaTemplate.send(TOPIC, String.valueOf(userId));
    }
}