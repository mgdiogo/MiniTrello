package com.mgdiogo.minitrello.services;

import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.dtos.responses.UserResponse;
import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.repositories.UserRepository;
import com.mgdiogo.minitrello.utility.UserMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public List<UserResponse> findAllUsers() {
		List<UserEntity> users = userRepository.findAll();

		return users.stream().map(userMapper::toResponse).toList();
	}
}
