package ru.luckyskeet.coordinator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.luckyskeet.coordinator.model.RoleId;
import ru.luckyskeet.coordinator.model.UserId;
import ru.luckyskeet.coordinator.repository.RoleIdRepository;
import ru.luckyskeet.coordinator.repository.UserIdRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CoordinatorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleIdRepository roleIdRepository;
    @Autowired
    private UserIdRepository userIdRepository;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_ROLE_ID = 2L;

    @BeforeEach
    void setUp() {
        userIdRepository.save(new UserId().setId(TEST_USER_ID));
        roleIdRepository.save(new RoleId().setId(TEST_ROLE_ID));
    }

    @Test
    void getUserRolesTest() throws Exception {
        mockMvc.perform(get("/api/users/{userId}/roles", TEST_USER_ID)
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void setRoleToUserTest() throws Exception {
        mockMvc.perform(post("/api/users/{userId}/roles/{roleId}", TEST_USER_ID, TEST_ROLE_ID)
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteRoleFromUserTest() throws Exception {
        mockMvc.perform(delete("/api/users/{userId}/roles/{roleId}", TEST_USER_ID, TEST_ROLE_ID)
                        .with(httpBasic("admin", "admin")))
                .andExpect(status().isNoContent());
    }
}
