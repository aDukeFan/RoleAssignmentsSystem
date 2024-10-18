package ru.luckyskeet.rolemanagment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.luckyskeet.rolemanagment.kafka.KafkaProducerService;
import ru.luckyskeet.rolemanagment.model.Role;
import ru.luckyskeet.rolemanagment.model.RoleDtoRequest;
import ru.luckyskeet.rolemanagment.model.RoleDtoToUpdate;
import ru.luckyskeet.rolemanagment.repository.RoleRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
class RolemanagerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository repository;
    @MockBean
    private KafkaProducerService producerService;

    @Test
    void createRoleTest() throws Exception {
        RoleDtoRequest roleDtoRequest = makeTestDtoRequest();
        postRole(roleDtoRequest);

        Role role = repository.findById(1L).orElseThrow();
        verify(producerService, times(1)).sendRoleCreatedEvent(role.getId());
    }

    @Test
    void getRoleByIdTest() throws Exception {
        RoleDtoRequest request = makeTestDtoRequest();
        postRole(request);

        mockMvc.perform(get("/api/roles/{roleId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test role"));
    }


    @Test
    void getRolesTest() throws Exception {
        mockMvc.perform(get("/api/roles")
                        .param("from", "0")
                        .param("size", "10")
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    void updateRoleByIdTest() throws Exception {
        RoleDtoRequest request = makeTestDtoRequest();
        postRole(request);

        RoleDtoToUpdate roleDtoToUpdate = new RoleDtoToUpdate()
                .setName("Updated Test Role")
                .setDescription("Yeah, it's true");

        mockMvc.perform(put("/api/roles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roleDtoToUpdate))
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Test Role"))
                .andExpect(jsonPath("$.description").value("Yeah, it's true"));
    }

    @Test
    void deleteRoleByIdTest() throws Exception {
        RoleDtoRequest request = makeTestDtoRequest();
        postRole(request);

        mockMvc.perform(delete("/api/roles/{id}", 1L)
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isNoContent());

        Optional<Role> deletedRole = repository.findById(1L);

        assertThat(deletedRole).isEmpty();

        verify(producerService, times(1))
                .sendRoleDeletedEvent(1L);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RoleDtoRequest makeTestDtoRequest() {
        return new RoleDtoRequest()
                .setName("Test role")
                .setDescription("role just for test, u know");
    }

    private void postRole(RoleDtoRequest request) throws Exception {
        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test role"));
    }
}