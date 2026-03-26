package com.mgdiogo.minitrello.services;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.dtos.requests.CreateUserRequest;
import com.mgdiogo.minitrello.dtos.requests.LoginRequest;
import com.mgdiogo.minitrello.dtos.responses.AuthResponse;
import com.mgdiogo.minitrello.dtos.responses.UserResponse;
import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.enums.UserRole;
import com.mgdiogo.minitrello.exceptions.BadRequestException;
import com.mgdiogo.minitrello.exceptions.ConflictException;
import com.mgdiogo.minitrello.repositories.UserRepository;
import com.mgdiogo.minitrello.security.CustomUserDetails;
import com.mgdiogo.minitrello.security.JwtService;
import com.mgdiogo.minitrello.utility.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserMapper userMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;

	public AuthResponse loginUser(LoginRequest request) {
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase().trim(), request.getPassword());
		Authentication auth = this.authenticationManager.authenticate(usernamePassword);

		CustomUserDetails customUser = (CustomUserDetails) auth.getPrincipal();

		List<String> roles = customUser.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.toList();

		String token = jwtService.generateAccessToken(
			Map.of(
				"uid", customUser.getUserId(),
				"roles", roles,
				"type", "access"
			),
			customUser.getUsername()
		);
		String refreshToken = refreshTokenService.generateRefreshToken();

		UserEntity user = userRepository.findById(customUser.getUserId()).orElseThrow();
		refreshTokenService.storeRefreshToken(user, refreshToken);

		return new AuthResponse(
			customUser.getUserId(),
			customUser.getEmail(),
			token,
			refreshToken
		);
	}

	public UserResponse createUser(CreateUserRequest userDTO) {
		String email = userDTO.getEmail().toLowerCase().trim();
		
		if (userRepository.existsByEmail(email))
			throw new ConflictException("Email already registered", "email");

		UserEntity user = new UserEntity();
		user.setFullName(userDTO.getFullName());
		user.setEmail(email);

		if (!userDTO.getPassword().equals(userDTO.getConfirmPassword()))
			throw new BadRequestException("Passwords do not match", "password");
		
		String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
		user.setPassword(hashedPassword);
		user.setRole(UserRole.USER);
		
		UserEntity createdUser = userRepository.save(user);

		return userMapper.toResponse(createdUser);
	}
}
