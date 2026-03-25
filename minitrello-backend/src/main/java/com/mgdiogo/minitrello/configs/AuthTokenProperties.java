package com.mgdiogo.minitrello.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth")
public class AuthTokenProperties {
    private String secretKey;
    private long accessTokenExpirationMinutes;
    private long refreshTokenExpirationDays;
    private String issuer;
}
