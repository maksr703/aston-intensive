package com.aston.userservice.util;

import com.aston.userservice.dto.UserResponse;
import com.aston.userservice.model.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getAge()
        );
    }
}
