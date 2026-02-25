package com.mgdiogo.minitrello.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.dtos.requests.LoginRequest;
import com.mgdiogo.minitrello.dtos.responses.LoginResponse;
import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.exceptions.BadRequestException;
import com.mgdiogo.minitrello.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public LoginResponse loginUser(LoginRequest userDTO) {
		UserEntity user = userRepository.findOneByEmail(userDTO.getEmail());
		if (user == null)
			throw new BadRequestException("User does not exist");

		if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword()))
			throw new BadRequestException("Invalid password");

		return loginToDTO(user);
	}

	private LoginResponse loginToDTO(UserEntity user) {
		return new LoginResponse(user.getUserId(), user.getEmail());
	}

}
