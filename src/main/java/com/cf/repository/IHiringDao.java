package com.cf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cf.model.Hiring;

@Repository
@Transactional
public interface IHiringDao extends JpaRepository<Hiring, Integer>{
	@Query("Select h from Hiring h")
List<Hiring> findAllHiring();
}
