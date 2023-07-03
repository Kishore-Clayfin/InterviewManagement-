package com.cf.service;

import java.util.List;

import com.cf.model.Candidate;
import com.cf.model.Schedule;

public interface ICandidateService {

	void saveCandidate(Candidate candidate);
	Candidate save(Candidate candidate);
	
	List<Candidate> viewCandidateList();

	Candidate updateCandidate(Integer candidateId);

	void deleteCandidate(Integer candidateId);
	
	Candidate updateCandidateStatus(Integer id,String status);

	Candidate findResumeCandidate(Integer candidateId);
	
	List<Candidate> bulkSaveCandidate(List<Candidate> candidateList);
	
	boolean existsCandidateByEmail(String email);
	
	Candidate findCandidateByEmail(String email);
	
	void deleteAllCandidates(List<Integer> candiIds);
	
	public String sendApplicationViaMail();
	
	public Candidate changeCandidateStatus(Candidate candidate,String status);
	
	public List<Candidate> filterByStatus(String status);
	
	public List<Candidate> changeCandidateListStatus(List<Candidate> listOfCandidate,String status);
}
