package com.mgdiogo.minitrello.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mgdiogo.minitrello.utility.ErrorResponseWriter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final ErrorResponseWriter errorResponseWriter;

	// Handles which requests are allowed in determined routes
	// All requests are allowed for now, to be implemented
	@Bean
	public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
		http
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(form -> form.disable())
				.exceptionHandling(ex -> ex
					.authenticationEntryPoint((request, response, authException) -> {
						errorResponseWriter.write(
							response,
							HttpStatus.UNAUTHORIZED.value(),
							"Unauthorized",
							"Authentication required",
							null
						);
					})
					.accessDeniedHandler((request, response, accessDeniedException) -> {
						errorResponseWriter.write(
							response,
							HttpStatus.FORBIDDEN.value(),
							"Forbidden",
							"Access denied",
							null
						);
					})
            	)
				.authorizeHttpRequests(auth -> auth
					.requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
					.requestMatchers("/error").permitAll()

					.requestMatchers("/users/**").hasRole("ADMIN")
					.anyRequest().authenticated()
				)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) { return config.getAuthenticationManager(); }

	// Password hashing algorithm
	@Bean
	public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
