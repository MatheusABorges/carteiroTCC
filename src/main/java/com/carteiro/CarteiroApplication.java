package com.carteiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ImportResource({"classpath:udp-inbound-config.xml","classpath:udp-inbound-config.xml"})
public class CarteiroApplication {

	public static void main(String[] args) { SpringApplication.run(CarteiroApplication.class, args); }

}
