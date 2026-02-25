package com.mgdiogo.minitrello.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskResponse {
	private Long taskId;
	private String title;
	private String description;
	private LocalDateTime createdAt;
	private boolean completed;
}
