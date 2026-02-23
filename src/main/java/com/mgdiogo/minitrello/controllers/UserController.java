package com.mgdiogo.minitrello.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.mgdiogo.minitrello.dtos.CreateUserDTO;
import com.mgdiogo.minitrello.dtos.GetUserDTO;
import com.mgdiogo.minitrello.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@GetMapping
	public List<GetUserDTO> getUsers() {
		return userService.findAllUsers();
	}

	@PostMapping
	public CreateUserDTO createUser(@Valid @RequestBody CreateUserDTO userDto) {
		return userService.createUser(userDto);
	}
}
