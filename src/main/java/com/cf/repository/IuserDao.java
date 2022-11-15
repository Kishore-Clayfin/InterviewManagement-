package com.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cf.model.User;

public interface IuserDao extends JpaRepository<User, Integer>
{
	
	boolean existsUserByEmailAndPassword(String name,String password);
	User findByEmail(String name);
}
