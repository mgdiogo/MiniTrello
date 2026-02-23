package com.mgdiogo.minitrello.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserDTO {
	private Long userId;
	private String email;
	private List<CreateTaskDTO> tasks;
}
