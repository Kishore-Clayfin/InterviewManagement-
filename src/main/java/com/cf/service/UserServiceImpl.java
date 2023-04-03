package com.cf.service;

import java.util.Collection;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cf.model.User;
import com.cf.repository.IuserDao;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IuserDao iUserDao;
    
	@Override
	public User findById(int id) {
		// TODO Auto-generated method stub
		log.info("find User from the database with id:" + id);
		return iUserDao.findById(id).orElseThrow(null);
	}

	

	@Override
	public User login(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsUserByUsernameAndPassword(String name, String password) {
		// TODO Auto-generated method stub
		
		return iUserDao.existsUserByEmailAndPassword(name, password);
	}

	@Override
	public void saveUser(User user) {
		iUserDao.save(user);
		log.info("new User added successfully");
	}

	@Override
	public List<User> viewUserList() {
		log.info("find all Users from the database");
		return iUserDao.findAll();
	}

	@Override
	public User updateUser(Integer userId) {
		log.info("User with userId: "+userId +" updated");
		return iUserDao.findById(userId).get();
	}

	@Override
	public void deleteUser(Integer userId) {
		iUserDao.deleteById(userId);
		log.info("User with userId "+userId +" deleted");
	}


	@Override
	public User findUsername(String name) {
		log.info("find User from the database with name:" + name);
		return iUserDao.findByEmail(name);
	}



	@Override
	public boolean existsUserByEmail(String email) {
		
		return iUserDao.existsUserByEmail(email);
	}



	@Override
	public String getAuthentication() {
		
		String role=null;
		org.springframework.security.core.Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		Collection<?> c= auth.getAuthorities();
		for (Object object : c) 
		{
			role=object.toString();
			
		}
		return role;
	}

}
