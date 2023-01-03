package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cf.model.Domain;
import com.cf.repository.IDomainDao;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
@Log4j2
@Service
public class DomainServiceImpl implements IDomainService {
	
	@Autowired
	private IDomainDao iDomainDao;
	
	@Override
	public void saveDomain(Domain domain) {
		log.info("new Domain added successfully");
		iDomainDao.save(domain);
	}

	@Override
	public List<Domain> viewDomainList() {
		log.info("find all Domains from the database");
		return iDomainDao.findAll();
	}

	@Override
	public Domain updateDomain(Integer domainId) {
		log.info("Domain with domainId "+domainId +" updated");
		return iDomainDao.findById(domainId).get();
	}

	@Override
	public void deleteDomain(Integer domainId) {
		iDomainDao.deleteById(domainId);
		log.info("Domain with domainId "+domainId +" deleted");
	}

}
