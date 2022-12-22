package com.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cf.model.DomainCategory;

public interface IDomainCategoryDao  extends JpaRepository<DomainCategory, Integer>
{

}
