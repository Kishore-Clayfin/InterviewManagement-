package com.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cf.model.Domain;

public interface IDomainDao extends JpaRepository<Domain, Integer>
{

}
