package ru.luckyskeet.usermanagment.kafka;

public interface KafkaProducerService {

    void sendUserCreatedEvent(Long userId);

    void sendUserDeletedEvent(Long userId);
}
