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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	private final AuthTokenProperties authTokenProperties;

	// Handles user login and sets cookie headers
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
		AuthResponse data = authService.loginUser(loginRequest);

		ResponseCookie accessCookie = buildAccessCookie(data.getToken());
		ResponseCookie refreshCookie = buildRefreshCookie(data.getRefreshToken());

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

	// Validates the refresh token cookie and issues a new access/refresh token pair
	@PostMapping("/refresh")
	public ResponseEntity<Void> refreshUserToken(HttpServletRequest request) {
		String refreshToken = extractRefreshToken(request);
		AuthResponse data = authService.refreshToken(refreshToken);

		ResponseCookie accessCookie = buildAccessCookie(data.getToken());
		ResponseCookie refreshCookie = buildRefreshCookie(data.getRefreshToken());

		return ResponseEntity.noContent().headers(headers -> {
			headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
			headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
		}).build();
	}

	private String extractRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if (cookies == null)
			return null;

		for (Cookie cookie : cookies) {
			if (authTokenProperties.getRefreshCookieName().equals(cookie.getName()))
				return cookie.getValue();
		}

		return null;
	}

	private ResponseCookie buildAccessCookie(String token) {
		return ResponseCookie.from(
				authTokenProperties.getAccessCookieName(),
				token)
				.httpOnly(true)
				.secure(authTokenProperties.isCookieSecure())
				.sameSite(authTokenProperties.getCookieSameSite())
				.path("/")
				.maxAge(Duration.ofMinutes(authTokenProperties.getAccessTokenExpirationMinutes()))
				.build();
	}

	private ResponseCookie buildRefreshCookie(String token) {
		return ResponseCookie.from(
				authTokenProperties.getRefreshCookieName(),
				token)
				.httpOnly(true)
				.secure(authTokenProperties.isCookieSecure())
				.sameSite(authTokenProperties.getCookieSameSite())
				.path(authTokenProperties.getRefreshCookiePath())
				.maxAge(Duration.ofDays(authTokenProperties.getRefreshTokenExpirationDays()))
				.build();
	}
}
