package ru.luckyskeet.usermanagment.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.luckyskeet.usermanagment.model.UserDtoRequest;
import ru.luckyskeet.usermanagment.model.UserDtoResponse;
import ru.luckyskeet.usermanagment.model.UserDtoToUpdate;

import java.util.List;


public interface UserApiDoc {

    @Operation(summary = "Get a paginated list of users",
            description = "Retrieve users with pagination using 'from' and 'size' parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content)})
    List<UserDtoResponse> getUsers(@RequestParam(defaultValue = "0", required = false) @Min(0) Integer from,
                                   @RequestParam(defaultValue = "10", required = false) @Min(0) @Max(50) Integer size);

    @Operation(summary = "Get user by ID",
            description = "Retrieve detailed information of a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    UserDtoResponse getUserById(@PathVariable @Min(1) Long userId);

    @Operation(summary = "Create a new user",
            description = "Create a new user by providing user details in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided",
                    content = @Content)})
    UserDtoResponse createUser(@Valid @RequestBody UserDtoRequest userDtoRequest);

    @Operation(summary = "Update a user by ID",
            description = "Update the details of a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    UserDtoResponse updateUser(@PathVariable @Min(1) Long userId,
                               @Valid @RequestBody UserDtoToUpdate userDtoToUpdate);

    @Operation(summary = "Delete a user by ID",
            description = "Delete a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully",
                    content = @Content)})
    void deleteUserById(@PathVariable @Min(1) Long userId);
}

