package com.learnease.server.controller;

import com.learnease.server.model.User;
import com.learnease.server.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor  // Lombok injects final fields automatically
@Tag(name = "User APIs", description = "APIs to manage users for LearnEase")
public class HelloWorldController {

    private final UserRepository userRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World from LearnEase!";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "Hello World from LearnEase6!";
    }

    @Operation(
            summary = "Save a new user",
            description = "Adds a new user to the database using JPA repository"
    )
    @GetMapping("/save")
    public String saveUser() {
        User user = new User();
        user.setName("Swaraj");
        userRepository.save(user);
        return "User Saved Successfully!";
    }

    @ApiResponse(responseCode = "200", description = "List of all users")
    @GetMapping("/users")
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
