package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.model.DomainCategory;
import com.cf.repository.IDomainCategoryDao;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class DomainCategoryServiceImpl implements IDomainCategoryService  
{
	@Autowired
	private IDomainCategoryDao iDomainCategoryDao;
	
	@Override
	public DomainCategory saveDomainCategory(DomainCategory domainCategory) {
		log.info("new DomainCategory added successfully");
		return iDomainCategoryDao.save(domainCategory);
		
	}

	@Override
	public List<DomainCategory> viewDomainCategoryList() {
		log.info("find all DomainCategory from the database");
		return iDomainCategoryDao.findAll();
	}

	@Override
	public DomainCategory updateDomainCategory(Integer domSubCatId) {
		log.info("DomainCategory with domSubCatId "+domSubCatId +" updated");
		return iDomainCategoryDao.findById(domSubCatId).get();
	}

	@Override
	public void deleteDomainCategory(Integer domSubCatId) {
		iDomainCategoryDao.deleteById(domSubCatId);
		log.info("DomainCategory with domSubCatId "+domSubCatId +" deleted");
	}

}
