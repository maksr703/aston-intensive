package com.aston.userservice.controller;

import com.aston.userservice.dto.CreateUserRequest;
import com.aston.userservice.dto.UpdateUserRequest;
import com.aston.userservice.dto.UserResponse;
import com.aston.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Service", description = "Операции для работы с пользователями")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить всех пользователей")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CollectionModel<EntityModel<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        List<EntityModel<UserResponse>> userResources = users.stream()
                .map(user -> {
                    EntityModel<UserResponse> resource = EntityModel.of(user);
                    resource.add(linkTo(methodOn(UserController.class).getUserById(user.id())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserResponse>> collection = CollectionModel.of(userResources);
        collection.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<EntityModel<UserResponse>> getUserById(
            @PathVariable UUID id
    ) {
        UserResponse user = userService.getUserById(id);
        EntityModel<UserResponse> resource = EntityModel.of(user);
        resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    @Operation(
            summary = "Создать пользователя",
            description = "Создаёт нового пользователя. Возвращает данные пользователя с HATEOAS‑ссылками."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Пользователь успешно создан",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public ResponseEntity<EntityModel<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        UserResponse createdUser = userService.create(request);
        EntityModel<UserResponse> resource = EntityModel.of(createdUser);
        resource.add(linkTo(methodOn(UserController.class).getUserById(createdUser.id())).withSelfRel());

        URI location = URI.create("/api/v1/users/" + createdUser.id());
        return ResponseEntity
                .created(location)
                .body(resource);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет существующего пользователя. Возвращает данные пользователя с HATEOAS‑ссылками."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь успешно обновлён",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public ResponseEntity<EntityModel<UserResponse>> updateUser(
            @Parameter(
                    description = "ID пользователя для обновления",
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UserResponse updatedUser = userService.update(id, request);
        EntityModel<UserResponse> resource = EntityModel.of(updatedUser);
        resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя для удаления")
            @PathVariable UUID id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}