package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cf.model.Hiring;
import com.cf.repository.IHiringDao;

@Service
@Transactional
public class HiringServiceImpl implements IHiringService{

	@Autowired
	private IHiringDao iHiringDao;
	
	@Override
	public Hiring saveHiring(Hiring hiring) {
		
		return iHiringDao.save(hiring);
	}

	@Override
	public List<Hiring> viewHiringList() {
		
		return iHiringDao.findAllHiring();
	}

	@Override
	public Hiring updateHiring(Integer hiringId) {
		
		return iHiringDao.findById(hiringId).get();
	}

	@Override
	public void deleteHiring(Integer hiringId) {
		
		iHiringDao.deleteById(hiringId);
	}

}
