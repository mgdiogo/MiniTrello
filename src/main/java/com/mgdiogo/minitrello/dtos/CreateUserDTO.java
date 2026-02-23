package com.mgdiogo.minitrello.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDTO {

	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 32, message = "Password must have between 6 and 32 characters")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
		message = "Password must contain upper, lower, number and special character"
	)
	private String password;
}
