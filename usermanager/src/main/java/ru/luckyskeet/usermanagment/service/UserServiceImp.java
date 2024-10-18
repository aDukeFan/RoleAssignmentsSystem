package ru.luckyskeet.usermanagment.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.luckyskeet.usermanagment.exception.UserNotFoundException;
import ru.luckyskeet.usermanagment.kafka.KafkaProducerService;
import ru.luckyskeet.usermanagment.mapper.UserMapper;
import ru.luckyskeet.usermanagment.model.User;
import ru.luckyskeet.usermanagment.model.UserDtoRequest;
import ru.luckyskeet.usermanagment.model.UserDtoResponse;
import ru.luckyskeet.usermanagment.model.UserDtoToUpdate;
import ru.luckyskeet.usermanagment.repository.UserRepository;
import ru.luckyskeet.usermanagment.security.UserAccessLevel;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final KafkaProducerService producerService;
    private final PasswordEncoder encoder;

    @Override
    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {
        log.info("Creating user with details: {}", userDtoRequest);
        User user = mapper.toSave(userDtoRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAccessLevel(UserAccessLevel.USER);
        User savedUser = repository.save(user);
        log.info("User created with ID: {}", savedUser.getId());
        producerService.sendUserCreatedEvent(savedUser.getId());
        return mapper.toSend(savedUser);
    }

    @Override
    public UserDtoResponse updateUserById(Long userId, UserDtoToUpdate userDtoToUpdate) {
        log.info("Updating user with ID: {}", userId);
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User updatedUser = mapper.toUpdateUser(userDtoToUpdate, user);
        log.info("User with ID: {} updated", userId);
        return mapper.toSend(repository.save(updatedUser));
    }

    @Override
    public void deleteUserById(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        repository.deleteById(userId);
        producerService.sendUserDeletedEvent(userId);
        log.info("User with ID: {} deleted", userId);
    }

    @Override
    public UserDtoResponse getUserById(Long userId) {
        log.info("Retrieving user with ID: {}", userId);
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        log.info("User retrieved with ID: {}", userId);
        return mapper.toSend(user);
    }

    @Override
    public List<UserDtoResponse> getUsers(Integer from, Integer size) {
        log.info("Retrieving users from {} with size {}", from, size);
        List<UserDtoResponse> users = repository
                .findAll(PageRequest.of(from / size, size))
                .stream()
                .map(mapper::toSend)
                .toList();
        log.info("Retrieved {} users", users.size());
        return users;
    }
}