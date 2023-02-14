package com.harsha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class BooksKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksKafkaApplication.class, args);

	}



}
