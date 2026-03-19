package com.mgdiogo.minitrello.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
	private Long userId;
	private String email;
	private List<TaskResponse> tasks;
}
