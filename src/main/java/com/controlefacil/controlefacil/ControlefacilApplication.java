package com.controlefacil.controlefacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ControlefacilApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlefacilApplication.class, args);
	}

}
