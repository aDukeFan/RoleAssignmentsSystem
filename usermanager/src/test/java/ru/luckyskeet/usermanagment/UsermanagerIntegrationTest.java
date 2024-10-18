package ru.luckyskeet.usermanagment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.luckyskeet.usermanagment.kafka.KafkaProducerService;
import ru.luckyskeet.usermanagment.model.User;
import ru.luckyskeet.usermanagment.model.UserDtoRequest;
import ru.luckyskeet.usermanagment.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsermanagerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private KafkaProducerService producerService;

    @Test
    void createUserTest() throws Exception {
        UserDtoRequest request = makeTestDtoRequest();
        postUser(request);

        User createdUser = userRepository.findByName(request.getName()).orElseThrow();
        assertNotNull(createdUser);
        assertEquals(request.getName(), createdUser.getName());
        assertEquals(request.getEmail(), createdUser.getEmail());

        verify(producerService, times(1))
                .sendUserCreatedEvent(createdUser.getId());
    }

    @Test
    void getUserByIdTest() throws Exception {
        UserDtoRequest request = makeTestDtoRequest();
        postUser(request);

        User createdUser = userRepository.findByName(request.getName()).orElseThrow();

        mockMvc.perform(get("/api/users/{id}", createdUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.name").value(createdUser.getName()));
    }

    @Test
    void getUsersTest() throws Exception {
        UserDtoRequest request = makeTestDtoRequest();
        postUser(request);
        mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    void updateUserByIdTest() throws Exception {
        UserDtoRequest request = makeTestDtoRequest();
        postUser(request);

        User createdUser = userRepository.findByName(request.getName()).orElseThrow();

        UserDtoRequest updateRequest = new UserDtoRequest()
                .setName("DukeFanUpdated")
                .setEmail("updatedEmail@outlook.com")
                .setPassword("newPassword");

        mockMvc.perform(put("/api/users/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateRequest))
                        .with(httpBasic(request.getName(), request.getPassword())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.name").value(updateRequest.getName()));
    }


    @Test
    void deleteUserByIdTest() throws Exception {
        UserDtoRequest request = makeTestDtoRequest();
        postUser(request);

        User createdUser = userRepository.findByName("DukeFan").orElseThrow();

        mockMvc.perform(delete("/api/users/{id}", createdUser.getId())
                        .with(httpBasic(request.getName(), request.getPassword())))
                .andExpect(status().isNoContent());

        Optional<User> deletedUser = userRepository.findByName(request.getName());
        assertThat(deletedUser).isEmpty();

        verify(producerService, times(1))
                .sendUserDeletedEvent(createdUser.getId());
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserDtoRequest makeTestDtoRequest() {
        return new UserDtoRequest()
                .setName("DukeFan")
                .setEmail("aDukeFan@outlook.com")
                .setPassword("password");
    }

    private void postUser(UserDtoRequest request) throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated());
    }
}
