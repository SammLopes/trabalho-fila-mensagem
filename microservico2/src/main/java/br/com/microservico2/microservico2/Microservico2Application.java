package br.com.microservico2.microservico2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Microservico2Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservico2Application.class, args);
	}

}
