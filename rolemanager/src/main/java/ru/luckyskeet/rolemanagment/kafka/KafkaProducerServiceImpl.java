package ru.luckyskeet.rolemanagment.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "role-events";

    @Override
    public void sendRoleCreatedEvent(Long roleId) {
        kafkaTemplate.send(TOPIC, String.valueOf(roleId));
    }

    @Override
    public void sendRoleDeletedEvent(Long roleId) {
        kafkaTemplate.send(TOPIC, String.valueOf(roleId));
    }
}

