package ru.luckyskeet.rolemanagment.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.luckyskeet.rolemanagment.model.RoleDtoRequest;
import ru.luckyskeet.rolemanagment.model.RoleDtoResponse;
import ru.luckyskeet.rolemanagment.model.RoleDtoToUpdate;

import java.util.List;


public interface RoleApiDoc {

    @Operation(summary = "Get a paginated list of roles",
            description = "Retrieve roles with pagination using 'from' and 'size' parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content)})
    @GetMapping
    List<RoleDtoResponse> getRoles(@RequestParam(defaultValue = "0", required = false)
                                   @Min(0) Integer from,
                                   @RequestParam(defaultValue = "10", required = false)
                                   @Min(0) @Max(50) Integer size);

    @Operation(summary = "Get role by ID",
            description = "Retrieve detailed information of a role by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content)})
    RoleDtoResponse getRoleById(@PathVariable @Min(1) Long userId);

    @Operation(summary = "Create a new role",
            description = "Create a new role by providing role details in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid role data provided",
                    content = @Content)})
    RoleDtoResponse createRole(@Valid @RequestBody RoleDtoRequest roleDtoRequest);

    @Operation(summary = "Update a role by ID",
            description = "Update the details of a role by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid role data provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content)})
    RoleDtoResponse updateRoleById(@PathVariable @Min(1) Long userId,
                                   @Valid @RequestBody RoleDtoToUpdate roleDtoToUpdate);

    @Operation(summary = "Delete a role by ID",
            description = "Delete a role by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully",
                    content = @Content)})
    void deleteRoleById(@PathVariable @Min(1) Long userId);
}

