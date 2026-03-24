package com.mgdiogo.minitrello.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CreateUserRequest {
	@NotBlank(message = "Full name is required")
	@Size(min = 1, max = 100, message = "Full name must have between 1 and 100 chracters")
	private String fullName;

	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 32, message = "Password must have between 6 and 32 characters")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{6,}$",
		message = "Password must contain upper, lower, number and special character"
	)
	private String password;

	@NotBlank(message = "Confirm password is required")
	private String confirmPassword;

}
