package com.cf.service;

import java.util.List;


import com.cf.model.DomainCategory;

public interface IDomainCategoryService 
{

	DomainCategory saveDomainCategory(DomainCategory domainCategory);
//	DomainCategory addDomainCategory( DomainCategory domainSubCategory);
//	DomainCategory updateDomainCategory(Integer  id);
//	void deleteDomainCategory(Integer id); 
//	List<DomainCategory> viewDomainCategoryList();

	List<DomainCategory> viewDomainCategoryList();

	DomainCategory updateDomainCategory(Integer domSubCatId);

	void deleteDomainCategory(Integer domSubCatId);
}
