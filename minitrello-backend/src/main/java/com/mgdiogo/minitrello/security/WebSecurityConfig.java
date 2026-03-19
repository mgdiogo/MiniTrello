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

import com.mgdiogo.minitrello.utility.ErrorMessage;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final ErrorMessage errorMessage;

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
						String body = errorMessage.setBody(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Authentication required");
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						response.setContentType("application/json");
						response.getWriter().write(body);
					})
					.accessDeniedHandler((request, response, accessDeniedException) -> {
						String body = errorMessage.setBody(HttpServletResponse.SC_FORBIDDEN, "Forbidden", "Access denied");
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						response.setContentType("application/json");
						response.getWriter().write(body);
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
