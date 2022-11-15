package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.model.DomainCategory;
import com.cf.repository.IDomainCategoryDao;

@Service
public class DomainCategoryServiceImpl implements IDomainCategoryService  
{
	@Autowired
	private IDomainCategoryDao iDomainCategoryDao;
	
	@Override
	public DomainCategory saveDomainCategory(DomainCategory domainCategory) {
		return iDomainCategoryDao.save(domainCategory);
		
	}

	@Override
	public List<DomainCategory> viewDomainCategoryList() {
		return iDomainCategoryDao.findAll();
	}

	@Override
	public DomainCategory updateDomainCategory(Integer domSubCatId) {
		
		return iDomainCategoryDao.findById(domSubCatId).get();
	}

	@Override
	public void deleteDomainCategory(Integer domSubCatId) {
		iDomainCategoryDao.deleteById(domSubCatId);
		
	}

}
