package com.hotelRating.userService.models;

import lombok.Data;

@Data
public class Rating {

	private String ratingId;
	private String userId;
	private String hotelId;
	private int rating;
	private String comment;
	private Hotel hotel;
}
