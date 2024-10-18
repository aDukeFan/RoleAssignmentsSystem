package ru.luckyskeet.rolemanagment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.luckyskeet.rolemanagment.exception.RoleNotFoundException;
import ru.luckyskeet.rolemanagment.kafka.KafkaProducerService;
import ru.luckyskeet.rolemanagment.mapper.RoleMapper;
import ru.luckyskeet.rolemanagment.model.Role;
import ru.luckyskeet.rolemanagment.model.RoleDtoRequest;
import ru.luckyskeet.rolemanagment.model.RoleDtoResponse;
import ru.luckyskeet.rolemanagment.model.RoleDtoToUpdate;
import ru.luckyskeet.rolemanagment.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RoleServiceImplTest {

    @Mock
    private RoleRepository repository;
    @Mock
    private RoleMapper mapper;
    @Mock
    private KafkaProducerService producerService;
    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private RoleDtoRequest roleDtoRequest;
    private RoleDtoResponse roleDtoResponse;
    private RoleDtoToUpdate roleDtoToUpdate;

    @BeforeEach
    void setUp() {
        role = new Role()
                .setId(1L)
                .setName("Test Role")
                .setDescription("For tests");
        roleDtoRequest = new RoleDtoRequest()
                .setName("Test Role")
                .setDescription("For tests");
        roleDtoResponse = new RoleDtoResponse()
                .setId(1L)
                .setName("Test Role")
                .setDescription("For tests");
        roleDtoToUpdate = new RoleDtoToUpdate()
                .setName("Updated Role")
                .setDescription("Updated description");
    }

    @Test
    void createRoleTest() {
        when(mapper.toSave(roleDtoRequest)).thenReturn(role);
        when(repository.save(role)).thenReturn(role);
        when(mapper.toSend(role)).thenReturn(roleDtoResponse);

        RoleDtoResponse result = roleService.createRole(roleDtoRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Role", result.getName());

        verify(mapper).toSave(roleDtoRequest);
        verify(repository).save(role);
        verify(producerService).sendRoleCreatedEvent(role.getId());
        verify(mapper).toSend(role);
    }

    @Test
    void updateRoleByIdTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(mapper.toUpdateRole(roleDtoToUpdate, role)).thenReturn(role);
        when(repository.save(role)).thenReturn(role);
        when(mapper.toSend(role)).thenReturn(roleDtoResponse);

        RoleDtoResponse result = roleService.updateRoleById(1L, roleDtoToUpdate);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Role", result.getName());

        verify(repository).findById(1L);
        verify(repository).save(role);
        verify(mapper).toUpdateRole(roleDtoToUpdate, role);
    }

    @Test
    void deleteRoleByIdTest() {
        roleService.deleteRoleById(1L);

        verify(repository).deleteById(1L);
        verify(producerService).sendRoleDeletedEvent(1L);
    }

    @Test
    void getRoleByIdTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(mapper.toSend(role)).thenReturn(roleDtoResponse);

        RoleDtoResponse result = roleService.getRoleById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(repository).findById(1L);
        verify(mapper).toSend(role);
    }

    @Test
    void getRolesTest() {
        Page<Role> page = new PageImpl<>(List.of(role));
        when(repository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(mapper.toSend(role)).thenReturn(roleDtoResponse);

        List<RoleDtoResponse> result = roleService.getRoles(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findAll(PageRequest.of(0, 10));
        verify(mapper).toSend(role);
    }

    @Test
    void getRoleById_shouldThrowExceptionWhenNotFound() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.getRoleById(id))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining(String.valueOf(id));

        verify(repository).findById(id);
    }
}
