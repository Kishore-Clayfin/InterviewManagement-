package com.cf.service;

import java.util.List;

import com.cf.model.Domain;
import com.cf.model.UserDetails;

public interface IDomainService {

	void saveDomain(Domain domain);
//	Domain findById(int id);
//	Domain save(Domain domain);
//	void deleteById(int id);
//	List<Domain> findAll();

	List<Domain> viewDomainList();

	Domain updateDomain(Integer domainId);

	void deleteDomain(Integer domainId);
}
