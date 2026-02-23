package com.mgdiogo.minitrello.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateTaskDTO {
	private Long taskId;

	@NotBlank(message = "Title is required")
	@Size(max = 24, message = "Title should not have more than 24 characters")
	private String title;

	@Size(max = 100, message = "Description should not have more than 100 characters")
	private String description;

	private boolean completed;
}
