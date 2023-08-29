package com.example.firesystemcs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.firesystemcs.dao")
@SpringBootApplication
public class FireSystemCsApplication {
	public static void main(String[] args) {
		SpringApplication.run(FireSystemCsApplication.class, args);
	}

}
