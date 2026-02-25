package com.mgdiogo.minitrello.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	// Handles which requests are allowed in determined routes
	// All requests are allowed for now, to be implemented
	@Bean
	public SecurityFilterChain applicationSecurity(HttpSecurity http) {
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(form -> form.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/auth/**").permitAll()
				.anyRequest().permitAll()
			);

		return http.build();
	}

	// Password hashing algorithm
	@Bean
	public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
