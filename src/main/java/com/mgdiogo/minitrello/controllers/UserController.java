package com.mgdiogo.minitrello.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.mgdiogo.minitrello.dtos.requests.CreateUserRequest;
import com.mgdiogo.minitrello.dtos.responses.UserResponse;
import com.mgdiogo.minitrello.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	// Checks all users information for development purposes
	@GetMapping
	public List<UserResponse> getUsers() {
		return userService.findAllUsers();
	}

	// Handles user registration
	@PostMapping
	public UserResponse createUser(@Valid @RequestBody CreateUserRequest userDto) {
		return userService.createUser(userDto);
	}
}
