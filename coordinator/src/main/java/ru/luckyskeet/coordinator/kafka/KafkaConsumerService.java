package ru.luckyskeet.coordinator.kafka;

public interface KafkaConsumerService {

    void listenUserEvents(String message);

    void listenRoleEvents(String message);
}
