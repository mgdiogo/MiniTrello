package com.mgdiogo.minitrello.security;

public record JwtPrincipal(
    Long userId,
    String email
) {}
