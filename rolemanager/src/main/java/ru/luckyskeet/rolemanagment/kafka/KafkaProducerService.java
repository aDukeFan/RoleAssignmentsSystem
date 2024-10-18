package ru.luckyskeet.rolemanagment.kafka;

public interface KafkaProducerService {

    void sendRoleCreatedEvent(Long roleId);

    void sendRoleDeletedEvent(Long roleId);
}
