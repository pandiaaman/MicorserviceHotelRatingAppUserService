package com.hotelRating.userService.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="micro_users_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@Column(name="user_id")
	private String userId;
	@Column(name="user_name",length=15)
	private String userName;
	@Column(name="user_email")
	private String userEmail;
	@Column(name="user_about")
	private String userAbout;
	
	@Transient //we will not store ratings in the user microservice database
	private List<Rating> ratings;
}
