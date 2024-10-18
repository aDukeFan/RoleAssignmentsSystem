package ru.luckyskeet.usermanagment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import ru.luckyskeet.usermanagment.kafka.KafkaProducerService;
import ru.luckyskeet.usermanagment.mapper.UserMapper;
import ru.luckyskeet.usermanagment.model.User;
import ru.luckyskeet.usermanagment.model.UserDtoRequest;
import ru.luckyskeet.usermanagment.model.UserDtoResponse;
import ru.luckyskeet.usermanagment.model.UserDtoToUpdate;
import ru.luckyskeet.usermanagment.repository.UserRepository;
import ru.luckyskeet.usermanagment.security.UserAccessLevel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImpTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private KafkaProducerService producerService;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private UserServiceImp userService;

    private User user;
    private UserDtoRequest userDtoRequest;
    private UserDtoResponse userDtoResponse;
    private UserDtoToUpdate userDtoToUpdate;

    @BeforeEach
    void setUp() {
        user = new User()
                .setId(2L)
                .setPassword("encodedPassword")
                .setAccessLevel(UserAccessLevel.USER);
        userDtoRequest = new UserDtoRequest()
                .setPassword("password");
        userDtoResponse = new UserDtoResponse()
                .setId(2L);
        userDtoToUpdate = new UserDtoToUpdate();
    }

    @Test
    void createUserTest() {
        when(mapper.toSave(userDtoRequest)).thenReturn(user);
        when(encoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(repository.save(user)).thenReturn(user);
        when(mapper.toSend(user)).thenReturn(userDtoResponse);

        UserDtoResponse result = userService.createUser(userDtoRequest);

        assertNotNull(result);
        assertEquals(2L, result.getId());

        verify(mapper).toSave(userDtoRequest);
        verify(encoder).encode(user.getPassword());
        verify(repository).save(user);
        verify(producerService).sendUserCreatedEvent(user.getId());
        verify(mapper).toSend(user);
    }


    @Test
    void updateUserByIdTest() {
        when(repository.findById(2L)).thenReturn(Optional.of(user));
        when(mapper.toUpdateUser(userDtoToUpdate, user)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toSend(user)).thenReturn(userDtoResponse);

        UserDtoResponse result = userService.updateUserById(2L, userDtoToUpdate);
        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(repository).findById(2L);
        verify(repository).save(user);
    }


    @Test
    void deleteUserByIdTest() {
        userService.deleteUserById(2L);
        verify(repository).deleteById(2L);
        verify(producerService).sendUserDeletedEvent(2L);
    }


    @Test
    void getUserByIdTest() {
        when(repository.findById(2L)).thenReturn(Optional.of(user));
        when(mapper.toSend(user)).thenReturn(userDtoResponse);

        UserDtoResponse result = userService.getUserById(2L);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(repository).findById(2L);
    }


    @Test
    void getUsersTest() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(repository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(mapper.toSend(user)).thenReturn(userDtoResponse);

        List<UserDtoResponse> result = userService.getUsers(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findAll(PageRequest.of(0, 10));
    }

}