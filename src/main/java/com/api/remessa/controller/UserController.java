package com.api.remessa.controller;

import com.api.remessa.dto.request.CreateUserRequest;
import com.api.remessa.dto.response.UserResponse;
import com.api.remessa.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> geteUserList() {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

}
