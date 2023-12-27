package com.hotelRating.userService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelRating.userService.models.User;

public interface UserRepository extends JpaRepository<User, String>{
/*
 * Crud Repository is the base interface and it acts as a marker interface. 
 * JPA also provides some extra methods related to JPA such as delete records 
 * in batch and flushing data directly to a database. It provides only CRUD 
 * functions like findOne, saves, etc. JPA repository also extends the 
 * PagingAndSorting repository.
 * 
 */
}
