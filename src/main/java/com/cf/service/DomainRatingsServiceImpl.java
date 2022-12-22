//package com.cf.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.cf.model.DomainRatings;
//import com.cf.repository.IDomainRatingsDao;
//
//@Service
//public class DomainRatingsServiceImpl implements IDomainRatingsService{
//
//	private IDomainRatingsDao iDomainRatingsDao;
//	@Override
//	public void saveDomainRatings(DomainRatings domainRatings) {
//		iDomainRatingsDao.save(domainRatings);
//		
//	}
//
//	@Override
//	public List<DomainRatings> viewDomainRatingsList() {
//		
//		return iDomainRatingsDao.findAll();
//	}
//
//	@Override
//	public DomainRatings updateDomainRatings(Integer ratingId) {
//		
//		return iDomainRatingsDao.findById(ratingId).get();
//	}
//
//	@Override
//	public void deleteDomainRatings(Integer ratingId) {
//		iDomainRatingsDao.deleteById(ratingId);
//		
//	}
//
//}
