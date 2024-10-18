package ru.luckyskeet.rolemanagment.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.luckyskeet.rolemanagment.exception.RoleNotFoundException;
import ru.luckyskeet.rolemanagment.kafka.KafkaProducerService;
import ru.luckyskeet.rolemanagment.mapper.RoleMapper;
import ru.luckyskeet.rolemanagment.model.Role;
import ru.luckyskeet.rolemanagment.model.RoleDtoRequest;
import ru.luckyskeet.rolemanagment.model.RoleDtoResponse;
import ru.luckyskeet.rolemanagment.model.RoleDtoToUpdate;
import ru.luckyskeet.rolemanagment.repository.RoleRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;
    private final KafkaProducerService producerService;

    @Override
    public List<RoleDtoResponse> getRoles(Integer from, Integer size) {
        log.info("Getting roles with pagination: from={}, size={}", from, size);
        return repository.findAll(PageRequest.of(from / size, size)).stream()
                .map(mapper::toSend)
                .toList();
    }

    @Override
    public RoleDtoResponse getRoleById(Long id) {
        log.info("Getting role by id: {}", id);
        Role role = repository.findById(id).orElseThrow(() -> {
            log.error("Role not found with id: {}", id);
            return new RoleNotFoundException(id);
        });
        return mapper.toSend(role);
    }

    @Override
    public RoleDtoResponse createRole(RoleDtoRequest roleDtoRequest) {
        log.info("Creating role with details: {}", roleDtoRequest);
        Role role = mapper.toSave(roleDtoRequest);
        Role savedRole = repository.save(role);
        producerService.sendRoleCreatedEvent(savedRole.getId());
        log.info("Role created with id: {}", savedRole.getId());
        return mapper.toSend(savedRole);
    }

    @Override
    public RoleDtoResponse updateRoleById(Long id, RoleDtoToUpdate roleDtoToUpdate) {
        log.info("Updating role with id: {} and details: {}", id, roleDtoToUpdate);
        Role role = repository.findById(id).orElseThrow(() -> {
            log.error("Role not found for update with id: {}", id);
            return new RoleNotFoundException(id);
        });
        Role updatedRole = mapper.toUpdateRole(roleDtoToUpdate, role);
        return mapper.toSend(repository.save(updatedRole));
    }

    @Override
    public void deleteRoleById(Long id) {
        log.info("Deleting role with id: {}", id);
        repository.deleteById(id);
        producerService.sendRoleDeletedEvent(id);
        log.info("Role deleted with id: {}", id);
    }
}

