package com.cloudSerenityHotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cloudSerenityHotel"})
public class SpringBootHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootHotelApplication.class, args);
	}

}
