package com.absencia.diginamic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.absencia.diginamic" })
public class DiginamicApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiginamicApplication.class, args);
	}

}
