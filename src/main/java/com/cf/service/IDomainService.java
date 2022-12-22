package com.cf.service;

import java.util.List;

import com.cf.model.Domain;
import com.cf.model.UserDetails;

public interface IDomainService {

	void saveDomain(Domain domain);

	List<Domain> viewDomainList();

	Domain updateDomain(Integer domainId);

	void deleteDomain(Integer domainId);
}
