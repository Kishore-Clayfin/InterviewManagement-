package com.cf.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.model.User;
import com.cf.repository.IuserDao;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IuserDao iUserDao;
    
	@Override
	public User findById(int id) {
		// TODO Auto-generated method stub
		
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
		
	}

	@Override
	public List<User> viewUserList() {
		return iUserDao.findAll();
	}

	@Override
	public User updateUser(Integer userId) {
		return iUserDao.findById(userId).get();
	}

	@Override
	public void deleteUser(Integer userId) {
		iUserDao.deleteById(userId);
		
	}


	@Override
	public User findUsername(String name) {
		// TODO Auto-generated method stub
		return iUserDao.findByEmail(name);
	}



	@Override
	public boolean existsUserByEmail(String email) {
		// TODO Auto-generated method stub
		return iUserDao.existsUserByEmail(email);
	}

//	@Override
//	public User login(String userName, String password) {
//		// TODO Auto-generated method stub
//		User user=iUserDao.findByUserNameAndPassword(userName, password);
//		int userId=user.getUserId();
//		if( userDao.existsById(userId))
//			return user;
//		else
//			return null;
//	}
	

}
