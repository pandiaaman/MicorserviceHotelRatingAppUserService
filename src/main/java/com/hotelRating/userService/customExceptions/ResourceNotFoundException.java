package com.hotelRating.userService.customExceptions;

public class ResourceNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String msg) {
		super(msg);
		System.out.println("resource not found on server exception");
	}

}
