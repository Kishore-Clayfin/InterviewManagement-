package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cf.model.Domain;
import com.cf.repository.IDomainDao;
import org.springframework.stereotype.Service;
@Service
public class DomainServiceImpl implements IDomainService {
	
	@Autowired
	private IDomainDao iDomainDao;
	
	@Override
	public void saveDomain(Domain domain) {
		iDomainDao.save(domain);
	}

	@Override
	public List<Domain> viewDomainList() {
		return iDomainDao.findAll();
	}

	@Override
	public Domain updateDomain(Integer domainId) {
		
		return iDomainDao.findById(domainId).get();
	}

	@Override
	public void deleteDomain(Integer domainId) {
		iDomainDao.deleteById(domainId);
		
	}

}
