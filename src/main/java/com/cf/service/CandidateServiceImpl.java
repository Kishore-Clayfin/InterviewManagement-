package com.cf.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cf.model.Candidate;
import com.cf.repository.ICandidateDao;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CandidateServiceImpl implements ICandidateService {

	@Autowired
	private ICandidateDao iCandidateDao;

	@Override
	public void saveCandidate(Candidate candidate) {

		if (!iCandidateDao.existsById(candidate.getCandidateId())) 
		{
			Candidate can=iCandidateDao.findById(candidate.getCandidateId()).orElseThrow(null);
			System.out.println(candidate.getCandidateId());
			if (can.getStatus() == null) 
			{
				System.err.println("-------Comming Inside---------");
				candidate.setStatus("ResumeShortlisted");
			}
		}
		else
		{
			Candidate can=iCandidateDao.findById(candidate.getCandidateId()).orElseThrow(null);
			candidate.setStatus(can.getStatus());
		}
		iCandidateDao.save(candidate);
		log.info("new candidate added successfully");
	}

	@Override
	public List<Candidate> viewCandidateList() {
		log.info("find all candidates from the database");
		return iCandidateDao.findAll();
	}

	@Override
	public void deleteCandidate(Integer candidateId) {
		iCandidateDao.deleteById(candidateId);
		log.info("candidate with candidateId "+candidateId +" deleted");
	}

	@Override
	public Candidate updateCandidate(Integer candidateId) {
		return iCandidateDao.findById(candidateId).get();
	}

	@Override
	public Candidate updateCandidateStatus(Integer id, String status) {

		Candidate candidate = iCandidateDao.findById(id).orElseThrow(null);
		candidate.setStatus(status);

		Candidate c = iCandidateDao.save(candidate);
		log.info("candidate status updated for candidateId: "+ id);
		return c;
	}

	@Override
	public Candidate findResumeCandidate(Integer candidateId) {
		log.info("find resume for candidate with candidatId: "+ candidateId);
		return iCandidateDao.findById(candidateId).get();
	}

	@Override
	@Transactional
	public List<Candidate> bulkSaveCandidate(List<Candidate> candidateList) {
		// TODO Auto-generated method stub
		List<Candidate> candList = iCandidateDao.saveAllAndFlush(candidateList);
		log.info("List of candidates added successfully");
		return candList;
	}

	@Override
	public Candidate save(Candidate candidate) {
		// TODO Auto-generated method stub
		candidate.setStatus("ResumeShortlisted");
		Candidate candid=iCandidateDao.save(candidate);
		return candid;
	}
}
