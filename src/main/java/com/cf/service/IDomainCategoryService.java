package com.cf.service;

import java.util.List;


import com.cf.model.DomainCategory;

public interface IDomainCategoryService 
{

	DomainCategory saveDomainCategory(DomainCategory domainCategory);

	List<DomainCategory> viewDomainCategoryList();

	DomainCategory updateDomainCategory(Integer domSubCatId);

	void deleteDomainCategory(Integer domSubCatId);
}
