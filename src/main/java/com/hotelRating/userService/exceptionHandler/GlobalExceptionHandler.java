package com.hotelRating.userService.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hotelRating.userService.customExceptions.ResourceNotFoundException;
import com.hotelRating.userService.payload.ApiResponse;

//this will be global centralized exception handler of the project, if anywhere there is an exception, this will handle it

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
		
		//ApiResponse is a payload that goes in as a response from the exception handler
		String message =  ex.getMessage(); 
		ApiResponse response = ApiResponse.builder().message(message).success(true).status(HttpStatus.NOT_FOUND).build();
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

	}
}
