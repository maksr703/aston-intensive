package com.aston.userservice.service;

import com.aston.userservice.dto.CreateUserRequest;
import com.aston.userservice.dto.UpdateUserRequest;
import com.aston.userservice.dto.UserResponse;
import com.aston.userservice.exception.UserNotFoundException;
import com.aston.userservice.model.User;
import com.aston.userservice.repository.UserRepository;
import com.aston.userservice.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id));

        return UserMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponse create(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(
                    "User with email already exists"
            );
        }

        User user = new User();

        user.setEmail(request.email());
        user.setName(request.name());
        user.setAge(request.age());

        User userSaved = userRepository.save(user);

        return UserMapper.toResponse(userSaved);
    }

    public UserResponse update(
            UUID id,
            UpdateUserRequest request
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id));

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.age() != null) {
            user.setAge(request.age());
        }

        User updated = userRepository.save(user);

        return UserMapper.toResponse(updated);
    }

    public void delete(UUID id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }
}
