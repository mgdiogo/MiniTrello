package com.mgdiogo.minitrello.services;

import org.springframework.stereotype.Service;
import com.mgdiogo.minitrello.dtos.responses.TaskResponse;
import com.mgdiogo.minitrello.entities.TaskEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	public TaskResponse taskEntityToDTO(TaskEntity task) {
		return new TaskResponse(
            task.getTaskId(),
            task.getTitle(),
            task.getDescription(),
			task.getCreatedAt(),
            task.getStatus()
    	);
	}
}
