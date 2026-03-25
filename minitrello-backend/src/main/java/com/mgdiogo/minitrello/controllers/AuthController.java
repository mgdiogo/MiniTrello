package com.mgdiogo.minitrello.controllers;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgdiogo.minitrello.configs.AuthTokenProperties;
import com.mgdiogo.minitrello.dtos.requests.CreateUserRequest;
import com.mgdiogo.minitrello.dtos.requests.LoginRequest;
import com.mgdiogo.minitrello.dtos.responses.AuthResponse;
import com.mgdiogo.minitrello.dtos.responses.LoginResponse;
import com.mgdiogo.minitrello.dtos.responses.UserResponse;
import com.mgdiogo.minitrello.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	private final AuthTokenProperties authTokenProperties;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
		AuthResponse data = authService.loginUser(loginRequest);

		ResponseCookie accessCookie = ResponseCookie.from(
				authTokenProperties.getAccessCookieName(),
				data.getToken())
				.httpOnly(true)
				.secure(authTokenProperties.isCookieSecure())
				.sameSite(authTokenProperties.getCookieSameSite())
				.path("/")
				.maxAge(Duration.ofMinutes(authTokenProperties.getAccessTokenExpirationMinutes()))
				.build();

		ResponseCookie refreshCookie = ResponseCookie.from(
				authTokenProperties.getRefreshCookieName(),
				data.getRefreshToken())
				.httpOnly(true)
				.secure(authTokenProperties.isCookieSecure())
				.sameSite(authTokenProperties.getCookieSameSite())
				.path(authTokenProperties.getRefreshCookiePath())
				.maxAge(Duration.ofDays(authTokenProperties.getRefreshTokenExpirationDays()))
				.build();

		LoginResponse response = new LoginResponse(data.getUserId(), data.getEmail());

		return ResponseEntity.ok()
				.headers(headers -> {
					headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
					headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
				})
				.body(response);
	}

	// Handles user registration
	@PostMapping("/register")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest userDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.createUser(userDto));
	}
}
