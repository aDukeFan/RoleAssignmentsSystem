package ru.luckyskeet.rolemanagment.service;

import ru.luckyskeet.rolemanagment.model.RoleDtoRequest;
import ru.luckyskeet.rolemanagment.model.RoleDtoResponse;
import ru.luckyskeet.rolemanagment.model.RoleDtoToUpdate;

import java.util.List;

public interface RoleService {

    List<RoleDtoResponse> getRoles(Integer from, Integer size);

    RoleDtoResponse getRoleById(Long userId);

    RoleDtoResponse createRole(RoleDtoRequest roleDtoRequest);

    RoleDtoResponse updateRoleById(Long userId, RoleDtoToUpdate roleDtoToUpdate);

    void deleteRoleById(Long userId);
}
