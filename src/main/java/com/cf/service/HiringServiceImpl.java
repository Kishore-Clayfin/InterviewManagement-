package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cf.model.Hiring;
import com.cf.repository.IHiringDao;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
@Transactional
public class HiringServiceImpl implements IHiringService{

	@Autowired
	private IHiringDao iHiringDao;
	
	@Override
	public Hiring saveHiring(Hiring hiring) {
		log.info("Hiring record added successfully");
		return iHiringDao.save(hiring);
	}

	@Override
	public List<Hiring> viewHiringList() {
		log.info("find all Hirings from the database");
		return iHiringDao.findAllHiring();
	}

	@Override
	public Hiring updateHiring(Integer hiringId) {
		log.info("Hiring with hiringId "+hiringId +" updated");
		return iHiringDao.findById(hiringId).get();
	}

	@Override
	public void deleteHiring(Integer hiringId) {
		
		iHiringDao.deleteById(hiringId);
		log.info("Hiring with hiringId "+hiringId +" deleted");
	}

}
