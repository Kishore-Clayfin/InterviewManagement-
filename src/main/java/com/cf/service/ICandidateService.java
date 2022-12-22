package com.cf.service;

import java.util.List;

import com.cf.model.Candidate;
import com.cf.model.Schedule;

public interface ICandidateService {

	void saveCandidate(Candidate candidate);

	List<Candidate> viewCandidateList();

	Candidate updateCandidate(Integer candidateId);

	void deleteCandidate(Integer candidateId);
	
	Candidate updateCandidateStatus(Integer id,String status);

	Candidate findResumeCandidate(Integer candidateId);
	
	List<Candidate> bulkSaveCandidate(List<Candidate> candidateList);
}
