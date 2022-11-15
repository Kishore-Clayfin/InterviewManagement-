package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.model.User;
import com.cf.model.UserDetails;

import com.cf.repository.IUserDetailsDao;

@Service
public class UserDetailsServiceImpl implements IUserDetailsService {
	@Autowired
	private IUserDetailsDao iUserDetailsDao;

	@Override
	public void saveUserDetails(UserDetails userDetails) {
		iUserDetailsDao.save(userDetails);

	}

	@Override
	public List<UserDetails> viewUserDetailsList() {
		
		return iUserDetailsDao.findAll();
	}

	@Override
	public UserDetails updateUserDetails(Integer userDetailsId) {
		
		return iUserDetailsDao.findById(userDetailsId).get();
	}

	@Override
	public void deleteUserDetails(Integer userDetailsId) {
		iUserDetailsDao.deleteById(userDetailsId);

	}

}
