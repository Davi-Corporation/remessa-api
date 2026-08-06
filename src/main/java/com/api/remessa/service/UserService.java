package com.api.remessa.service;

import com.api.remessa.dto.request.CreateUserRequest;
import com.api.remessa.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public UserResponse create(CreateUserRequest request) {
        // Implement the logic to create a new user
        // For example, you can save the user to the database and return a UserResponse
        return null; // Replace with actual implementation
    }
}
