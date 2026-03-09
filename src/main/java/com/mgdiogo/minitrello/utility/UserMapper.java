package com.mgdiogo.minitrello.utility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mgdiogo.minitrello.dtos.responses.TaskResponse;
import com.mgdiogo.minitrello.dtos.responses.UserResponse;
import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.services.TaskService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
	private final TaskService taskService;
	
	public UserResponse toResponse(UserEntity user) {
		List<TaskResponse> tasks = user.getTasks() == null ? new ArrayList<>() : user.getTasks()
			.stream()
			.map(taskService::taskEntityToDTO)
			.toList();

		return new UserResponse(user.getUserId(), user.getEmail(), tasks);
	}
}
