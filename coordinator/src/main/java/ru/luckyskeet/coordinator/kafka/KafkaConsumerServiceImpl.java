package ru.luckyskeet.coordinator.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.luckyskeet.coordinator.model.RoleId;
import ru.luckyskeet.coordinator.model.UserId;
import ru.luckyskeet.coordinator.repository.RoleIdRepository;
import ru.luckyskeet.coordinator.repository.UserIdRepository;

@Service
@AllArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private final UserIdRepository userIdRepository;
    private final RoleIdRepository roleIdRepository;

    @Override
    @KafkaListener(topics = "user-events", groupId = "coordinator-group")
    public void listenUserEvents(String message) {
        Long userId = Long.valueOf(message);
        userIdRepository.save(new UserId().setId(userId));
    }

    @Override
    @KafkaListener(topics = "role-events", groupId = "coordinator-group")
    public void listenRoleEvents(String message) {
        Long roleId = Long.valueOf(message);
        roleIdRepository.save(new RoleId().setId(roleId));
    }
}
