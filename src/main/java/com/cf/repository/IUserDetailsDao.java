package com.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cf.model.UserDetails;

public interface IUserDetailsDao extends JpaRepository<UserDetails, Integer>
{

}
