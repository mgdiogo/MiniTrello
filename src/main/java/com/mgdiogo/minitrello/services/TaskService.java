package com.mgdiogo.minitrello.services;

import org.springframework.stereotype.Service;
import com.mgdiogo.minitrello.dtos.CreateTaskDTO;
import com.mgdiogo.minitrello.entities.TaskEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	public CreateTaskDTO taskEntityToDTO(TaskEntity task) {
		return new CreateTaskDTO(
            task.getTaskId(),
            task.getTitle(),
            task.getDescription(),
            task.isCompleted()
    	);
	}
}
