package com.cf.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cf.model.Candidate;
import com.cf.repository.ICandidateDao;
import org.springframework.stereotype.Service;

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
				candidate.setStatus("INCOMPLETE");
			}
		}
		else
		{
			Candidate can=iCandidateDao.findById(candidate.getCandidateId()).orElseThrow(null);
			candidate.setStatus(can.getStatus());
		}
		iCandidateDao.save(candidate);

	}

	@Override
	public List<Candidate> viewCandidateList() {

		return iCandidateDao.findAll();
	}

	@Override
	public void deleteCandidate(Integer candidateId) {
		iCandidateDao.deleteById(candidateId);

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
		return c;
	}

	@Override
	public Candidate findResumeCandidate(Integer candidateId) {
		return iCandidateDao.findById(candidateId).get();
	}

	@Override
	@Transactional
	public List<Candidate> bulkSaveCandidate(List<Candidate> candidateList) {
		// TODO Auto-generated method stub
		List<Candidate> candList = iCandidateDao.saveAllAndFlush(candidateList);
		return candList;
	}
}
