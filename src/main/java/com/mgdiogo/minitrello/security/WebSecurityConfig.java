package com.mgdiogo.minitrello.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	// Handles which requests are allowed in determined routes
	// All requests are allowed for now, to be implemented
	@Bean
	public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(form -> form.disable())
				.exceptionHandling(ex -> ex
					.authenticationEntryPoint((request, response, authException) -> {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						response.getWriter().write("Unauthorized");
					})
					.accessDeniedHandler((request, response, accessDeniedException) -> {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						response.getWriter().write("Forbidden");
					})
            	)
				.authorizeHttpRequests(auth -> auth
					.requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
					.requestMatchers("/error").permitAll()

					.requestMatchers("/users/**").hasRole("ADMIN")
					.requestMatchers("/test").authenticated()
					.anyRequest().permitAll()
				)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) { return config.getAuthenticationManager(); }

	// Password hashing algorithm
	@Bean
	public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
