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
	@Size(max = 100, message = "Full name must be between 1 and 100 characters")
	private String fullName;

	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email address")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 64, message = "Password must be between 6 and 32 characters")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{6,}$",
		message = "Password must contain atleast one upper, lower, number and special character"
	)
	private String password;

	@NotBlank(message = "Confirm password is required")
	private String confirmPassword;

}
