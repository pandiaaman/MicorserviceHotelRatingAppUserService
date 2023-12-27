package com.hotelRating.userService.external.feignClientSercvices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hotelRating.userService.models.Hotel;

@FeignClient(name="HOTEL-SERVICE")
public interface HotelFiegnService {

	@GetMapping(value="/hotelservice/hotels/{hotelId}")
	public Hotel getHotel(@PathVariable("hotelId") String hotelId);
}
