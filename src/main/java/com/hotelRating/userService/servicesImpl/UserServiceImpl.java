package com.hotelRating.userService.servicesImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hotelRating.userService.customExceptions.ResourceNotFoundException;
import com.hotelRating.userService.external.feignClientSercvices.HotelFiegnService;
import com.hotelRating.userService.models.Hotel;
import com.hotelRating.userService.models.Rating;
import com.hotelRating.userService.models.User;
import com.hotelRating.userService.repositories.UserRepository;
import com.hotelRating.userService.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RestTemplate restTemplate; //to autowire, we should have a bean defined for this RestTemplate
	
	@Autowired
	private HotelFiegnService hotelFeignService; //used to replace restTemplate for API calls
	
	@Override
	public User saveUser(User user) {
		
		String randomuid = UUID.randomUUID().toString();
//		Random rand = new Random();
//		int substrVal = 10 + rand.nextInt(10); //limiting the length of uid created to 20 and min to 10
//		randomuid = randomuid.substring(substrVal);
		user.setUserId(randomuid);
		
		User useradded = this.repository.save(user);
		return useradded;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = this.repository.findAll();
		
		//fetching ratings from rating service for each user when all users are called
		for(User user : users) {
			Rating[] userRatingFromUserService = this.restTemplate.getForObject("http://RATING-SERVICE/ratingservice/ratings/byuser/"+user.getUserId(), Rating[].class);
			user.setRatings(Arrays.asList(userRatingFromUserService));
		
			//we need to set hotel for each rating
			for(Rating incomingRate : userRatingFromUserService) {
				//api call to get the hotel detail
				///http://localhost:8082/hotelservice/hotels/496b1c4c-cdd8-453a-9a8b-8b810a9860e1
				ResponseEntity<Hotel> hotel = this.restTemplate.getForEntity("http://HOTEL-SERVICE/hotelservice/hotels/"+incomingRate.getHotelId(), Hotel.class);
				
				//set hotel to rating
				incomingRate.setHotel(hotel.getBody());
			}
			
		}
		
		
		return users;
	}

	@Override
	public User getUserById(String userId) {
		
		User userFetched = this.repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given" + userId + " can not be found on server"));
		
		//fetching ratings related to this user from the rating service
		//in ratings service, we have http://localhost:8083/ratingservice/ratings/byuser/{userId} : giving us all ratings by given user
		/*
		 * there are multiple ways to call HTTP API from within the microservice, but there are pros and cons of all
		 * using RestTemplate
		 */
		
		Rating[] userRatingsFromUserService = this.restTemplate.getForObject("http://RATING-SERVICE/ratingservice/ratings/byuser/"+userFetched.getUserId(), Rating[].class);
		
		logger.info("logged userRatingsFromUserService : ".toUpperCase() + " {} ", Arrays.asList(userRatingsFromUserService));
		
		//we need to set hotel for each rating
		for(Rating incomingRate : userRatingsFromUserService) {
		//->api call to get the hotel detail
			///http://localhost:8082/hotelservice/hotels/496b1c4c-cdd8-453a-9a8b-8b810a9860e1
			
			//->calling using restTemplate
			//ResponseEntity<Hotel> hotel = this.restTemplate.getForEntity("http://HOTEL-SERVICE/hotelservice/hotels/"+incomingRate.getHotelId(), Hotel.class);
			
			//->calling using FeignClient
			Hotel hotel = this.hotelFeignService.getHotel(incomingRate.getHotelId());
			
		//set hotel to rating
			//incomingRate.setHotel(hotel.getBody());
			incomingRate.setHotel(hotel);
		}
		
		userFetched.setRatings(Arrays.asList(userRatingsFromUserService));
		
		return userFetched;
	}

	@Override
	public boolean deleteUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
