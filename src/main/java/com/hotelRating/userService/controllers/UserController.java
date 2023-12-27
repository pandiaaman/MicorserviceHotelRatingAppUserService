package com.hotelRating.userService.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotelRating.userService.models.User;
import com.hotelRating.userService.servicesImpl.UserServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserServiceImpl userService;
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	
	//get Users
	@GetMapping("/")
	public ResponseEntity<List<User>> getAllUsers(){
		try {
			List<User> allUsers = this.userService.getAllUsers();
			if(allUsers != null) {
				return ResponseEntity.status(HttpStatus.OK).body(allUsers);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	//to track the times we retried to connect to a service below
	int retryCount = 1;
	
	//get Single user
	@GetMapping("/{userId}")
//	@CircuitBreaker(name="ratingHotelFallbackCircuitBreaker",fallbackMethod = "ratingHotelFallback") //checks if the rating or hotel service are down
//	@Retry(name="ratingHotelServiceRetry",fallbackMethod = "ratingHotelFallback")
	@RateLimiter(name="userRateLimiter", fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<User> getUserById(@PathVariable("userId") String userId){
		logger.info("fetching a single user by id : {}",userId);
		logger.info("retying : {}" + retryCount++);
//		try {
			User user = this.userService.getUserById(userId);
//			if(user!=null) {
				return ResponseEntity.status(HttpStatus.OK).body(user);
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/*
	 * creating the fallback method for handing the circuit breaker for service calling rating and hotel services
	*/
	public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){
		logger.info("fallback is executed because the service is down {}",ex.getMessage());
		User user = User.builder()
			.userId("1111")
			.userEmail("dummy@gmail.com")
			.userName("dummy")
			.userAbout("user is dummy because the service is down")
			.build();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
	}
	
	
	//create user
	@PostMapping(value="/")
	public ResponseEntity<User> addUser(@RequestBody User user){
		try {
			User useradded = this.userService.saveUser(user);
			if(useradded!=null) {
				return ResponseEntity.status(HttpStatus.OK).body(useradded);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
}
