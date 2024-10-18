package ru.luckyskeet.rolemanagment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.luckyskeet.rolemanagment.documentation.RoleApiDoc;
import ru.luckyskeet.rolemanagment.model.RoleDtoRequest;
import ru.luckyskeet.rolemanagment.model.RoleDtoResponse;
import ru.luckyskeet.rolemanagment.model.RoleDtoToUpdate;
import ru.luckyskeet.rolemanagment.service.RoleService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/roles")
@AllArgsConstructor
@Validated
@Slf4j
public class RoleController implements RoleApiDoc {

    private final RoleService service;

    @GetMapping
    public List<RoleDtoResponse> getRoles(@RequestParam(defaultValue = "0", required = false)
                                          @Min(0) Integer from,
                                          @RequestParam(defaultValue = "10", required = false)
                                          @Min(0) @Max(50) Integer size) {
        log.info("GET /api/roles called with from={} and size={}", from, size);
        return service.getRoles(from, size);
    }

    @GetMapping("/{roleId}")
    public RoleDtoResponse getRoleById(@PathVariable @Min(1) Long roleId) {
        log.info("GET /api/roles/{} called", roleId);
        return service.getRoleById(roleId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDtoResponse createRole(@Valid @RequestBody RoleDtoRequest roleDtoRequest) {
        log.info("POST /api/roles called with {}", roleDtoRequest);
        return service.createRole(roleDtoRequest);
    }

    @PutMapping("/{roleId}")
    public RoleDtoResponse updateRoleById(@PathVariable @Min(1) Long roleId,
                                          @Valid @RequestBody RoleDtoToUpdate roleDtoToUpdate) {
        log.info("PUT /api/roles/{} called with {}", roleId, roleDtoToUpdate);
        return service.updateRoleById(roleId, roleDtoToUpdate);
    }

    @DeleteMapping("/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoleById(@PathVariable @Min(1) Long roleId) {
        log.info("DELETE /api/roles/{} called", roleId);
        service.deleteRoleById(roleId);
    }
}

