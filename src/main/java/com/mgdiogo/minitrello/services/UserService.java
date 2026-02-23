package com.mgdiogo.minitrello.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mgdiogo.minitrello.dtos.CreateTaskDTO;
import com.mgdiogo.minitrello.dtos.CreateUserDTO;
import com.mgdiogo.minitrello.dtos.GetUserDTO;
import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

	private final TaskService taskService;
	private final UserRepository userRepository;

	public List<GetUserDTO> findAllUsers() {
		List<UserEntity> users = userRepository.findAll();

		return users.stream().map(this::userEntityToDTO).toList();
	}

	public CreateUserDTO createUser(CreateUserDTO userDTO) {
		String email = userDTO.getEmail().toLowerCase();

		if (userRepository.existsByEmail(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
		}
	}

	private GetUserDTO userEntityToDTO(UserEntity user) {
		List<CreateTaskDTO> tasks = user.getTasks() == null ? new ArrayList<>() : user.getTasks()
			.stream()
			.map(taskService::taskEntityToDTO)
			.toList();

		return new GetUserDTO(user.getUserId(), user.getEmail(), tasks);
	}
}
