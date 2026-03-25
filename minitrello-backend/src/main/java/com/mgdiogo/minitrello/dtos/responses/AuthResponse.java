package com.mgdiogo.minitrello.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private Long userId;
	private String email;
    private String token;
    private String refreshToken;
}
