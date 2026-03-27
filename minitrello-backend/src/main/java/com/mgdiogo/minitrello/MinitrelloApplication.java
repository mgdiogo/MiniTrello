package com.mgdiogo.minitrello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mgdiogo.minitrello.configs.AuthTokenProperties;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(AuthTokenProperties.class)
public class MinitrelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinitrelloApplication.class, args);
	}

}
