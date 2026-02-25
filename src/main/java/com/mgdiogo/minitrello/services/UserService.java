package com.mgdiogo.minitrello.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.dtos.responses.TaskResponse;
import com.mgdiogo.minitrello.dtos.requests.CreateUserRequest;
import com.mgdiogo.minitrello.dtos.responses.UserResponse;
import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.exceptions.ConflictException;
import com.mgdiogo.minitrello.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

	private final TaskService taskService;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public List<UserResponse> findAllUsers() {
		List<UserEntity> users = userRepository.findAll();

		return users.stream().map(this::userEntityToDTO).toList();
	}

	public UserResponse createUser(CreateUserRequest userDTO) {
		try {
			String email = userDTO.getEmail().toLowerCase();
			
			UserEntity user = new UserEntity();
			user.setEmail(email);
			
			String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
			user.setPassword(hashedPassword);
			
			UserEntity createdUser = userRepository.save(user);
	
			return userEntityToDTO(createdUser);
		} catch (DataIntegrityViolationException e) {
			throw new ConflictException("Email already registered");
		}
	}

	private UserResponse userEntityToDTO(UserEntity user) {
		List<TaskResponse> tasks = user.getTasks() == null ? new ArrayList<>() : user.getTasks()
			.stream()
			.map(taskService::taskEntityToDTO)
			.toList();

		return new UserResponse(user.getUserId(), user.getEmail(), tasks);
	}
}
