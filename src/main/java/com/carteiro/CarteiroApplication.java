package com.carteiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@ImportResource("classpath:spring-integration-context.xml")
public class CarteiroApplication {

	public static void main(String[] args) { SpringApplication.run(CarteiroApplication.class, args); }

}
