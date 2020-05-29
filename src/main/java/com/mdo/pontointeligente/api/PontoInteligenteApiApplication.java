package com.mdo.pontointeligente.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PontoInteligenteApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PontoInteligenteApiApplication.class, args);
	}

}
