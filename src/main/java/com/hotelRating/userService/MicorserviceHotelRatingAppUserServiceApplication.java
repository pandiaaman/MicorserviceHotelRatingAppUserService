package com.hotelRating.userService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableEurekaClient has been depricated and we can only import it in pom.xml and it can be used automatically
@SpringBootApplication
@EnableFeignClients
public class MicorserviceHotelRatingAppUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicorserviceHotelRatingAppUserServiceApplication.class, args);
		System.out.println("\t running... ");
		System.out.println("***************************");
	}

}
