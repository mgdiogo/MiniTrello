package com.mgdiogo.minitrello.security;

import com.mgdiogo.minitrello.configs.AuthTokenProperties;

import java.io.IOException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mgdiogo.minitrello.utility.ErrorResponseWriter;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProperties authTokenProperties;
    private final JwtService jwtService;
    private final ErrorResponseWriter errorResponseWriter;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String token = extractAccessToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtService.isTokenValid(token)) {
            unauthorized(response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String email = jwtService.getUsername(token);
            final Long userId = jwtService.extractClaim(token, claims -> claims.get("uid", Long.class));
            List<?> roles = jwtService.extractClaim(token, claims -> claims.get("roles", List.class));
            List<GrantedAuthority> authorities = roles.stream()
                    .map(Object::toString)
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                    .toList();

            JwtPrincipal principal = new JwtPrincipal(userId, email);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (JwtException | IllegalArgumentException | ClassCastException e) {
            unauthorized(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null)
            return null;

        for (Cookie cookie : cookies) {
            if (authTokenProperties.getAccessCookieName().equals(cookie.getName()))
                return cookie.getValue();
        }

        return null;
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        errorResponseWriter.write(
                response,
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Invalid or expired token",
                null);
    }
}
