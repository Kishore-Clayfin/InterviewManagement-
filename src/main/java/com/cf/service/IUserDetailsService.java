package com.cf.service;

import java.util.List;

import com.cf.model.UserDetails;

public interface IUserDetailsService {

	void saveUserDetails(UserDetails userDetails);
//UserDetails findById(int id);
//UserDetails save(UserDetails userDetails);
//void deleteById(int id);
//List<UserDetails> findAll();

	List<UserDetails> viewUserDetailsList();

	UserDetails updateUserDetails(Integer userDetailsId);

	void deleteUserDetails(Integer userDetailsId);
	
}
