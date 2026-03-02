package com.mgdiogo.minitrello.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.dtos.requests.LoginRequest;
import com.mgdiogo.minitrello.dtos.responses.LoginResponse;
import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.repositories.UserRepository;
import com.mgdiogo.minitrello.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;

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
