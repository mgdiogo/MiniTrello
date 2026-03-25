package com.mgdiogo.minitrello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.mgdiogo.minitrello.configs.AuthTokenProperties;

@SpringBootApplication
@EnableConfigurationProperties(AuthTokenProperties.class)
public class MinitrelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinitrelloApplication.class, args);
	}

}
