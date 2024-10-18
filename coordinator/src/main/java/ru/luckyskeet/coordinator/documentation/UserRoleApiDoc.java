package ru.luckyskeet.coordinator.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.PathVariable;
import ru.luckyskeet.coordinator.model.UserRole;

import java.util.List;

public interface UserRoleApiDoc {

    @Operation(summary = "Get roles of a user by userId",
            description = "Retrieve all role IDs assigned to a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid userId supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    List<Long> getUserRoles(@PathVariable @Min(1) Long userId);

    @Operation(summary = "Assign a role to a user",
            description = "Assign a specific role to a user by providing the userId and roleId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role assigned successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRole.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid userId or roleId supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User or role not found",
                    content = @Content)})
    UserRole setRoleToUserById(@PathVariable @Min(1) Long userId,
                               @PathVariable @Min(1) Long roleId);

    @Operation(summary = "Remove a role from a user",
            description = "Remove a specific role from a user by providing the userId and roleId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role removed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid userId or roleId supplied",
                    content = @Content)})
    void deleteRoleOfUserById(@PathVariable @Min(1) Long userId,
                              @PathVariable @Min(1) Long roleId);
}
