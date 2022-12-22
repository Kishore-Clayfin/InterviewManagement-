package com.cf.service;

import java.util.List;

import com.cf.model.UserDetails;

public interface IUserDetailsService {

	void saveUserDetails(UserDetails userDetails);

	List<UserDetails> viewUserDetailsList();

	UserDetails updateUserDetails(Integer userDetailsId);

	void deleteUserDetails(Integer userDetailsId);
	
}
