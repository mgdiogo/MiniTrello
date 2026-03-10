package com.mgdiogo.minitrello.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = userRepository.findOneByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

		return new CustomUserDetails(
			user.getUserId(),
			user.getEmail(),
			user.getPassword(),
			user.getRole()
		);
	}
}
