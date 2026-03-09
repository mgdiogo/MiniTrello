package com.mgdiogo.minitrello.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.dtos.requests.CreateUserRequest;
import com.mgdiogo.minitrello.dtos.requests.LoginRequest;
import com.mgdiogo.minitrello.dtos.responses.LoginResponse;
import com.mgdiogo.minitrello.dtos.responses.UserResponse;
import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.enums.UserRole;
import com.mgdiogo.minitrello.exceptions.ConflictException;
import com.mgdiogo.minitrello.repositories.UserRepository;
import com.mgdiogo.minitrello.security.CustomUserDetails;
import com.mgdiogo.minitrello.utility.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserMapper userMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;

	public LoginResponse loginUser(LoginRequest request) {
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
		Authentication auth = this.authenticationManager.authenticate(usernamePassword);

		CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

		String token = null;

		return new LoginResponse(
			user.getUserId(),
			user.getEmail(),
			token
		);
	}

	public UserResponse createUser(CreateUserRequest userDTO) {
		try {
			String email = userDTO.getEmail().toLowerCase();
			
			UserEntity user = new UserEntity();
			user.setEmail(email);
			
			String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
			user.setPassword(hashedPassword);
			user.setRole(UserRole.USER);
			
			UserEntity createdUser = userRepository.save(user);
	
			return userMapper.toResponse(createdUser);
		} catch (DataIntegrityViolationException e) {
			throw new ConflictException("Email already registered");
		}
	}
}
