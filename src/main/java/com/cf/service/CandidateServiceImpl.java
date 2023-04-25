package com.cf.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.cf.model.Candidate;
import com.cf.model.Feedback;
import com.cf.model.History;
import com.cf.model.Schedule;
import com.cf.repository.ICandidateDao;
import com.cf.repository.IFeedbackDao;
import com.cf.repository.IScheduleDao;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CandidateServiceImpl implements ICandidateService {

	@Autowired
	private ICandidateDao iCandidateDao;
	@Autowired
	private IFeedbackDao iFeedbackDao;
	@Autowired
	private IHistoryService historyService;
	@Autowired
	private IScheduleService scheduleService;
	@Autowired
	private IScheduleDao scheduleDao;
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
		Candidate candi=iCandidateDao.findById(candidateId).orElseThrow();
		
		History history=new History();
		history.setAlternateEmail(candi.getAlternateEmail());
		history.setAlternateMobileNumber(candi.getAlternateMobileNumber());
		history.setCandidateId(candidateId);
		history.setCandidateName(candi.getCandidateName());
		history.setCgpa(candi.getCgpa());
		history.setCurrentCtc(candi.getCurrentCtc());
		history.setHighQualification(candi.getHighQualification());
		history.setEmail(candi.getEmail());
		history.setMobileNumber(candi.getMobileNumber());
		history.setResume(candi.getResume());
		history.setResumeName(candi.getResumeName());
		history.setRoleAppliedFor(candi.getRoleAppliedFor());		
		history.setUserName(candi.getUser().getUsername());
		history.setExpectedCtc(candi.getExpectedCtc());
		history.setExperience(candi.getExperience());
		history.setStatus(candi.getStatus());		                                                        
		if(iFeedbackDao.existsFeedbackByCandidate(candi)) {
		Feedback feedback=iFeedbackDao.findByCandidate(candi);
		history.setDomainRatings(feedback.getDomainRatings());
		System.out.println("Feedback domain Ratings"+feedback.getDomainRatings());
		history.setFeed_back(feedback.getFeed_back());
		history.setFeedbackId(feedback.getFeedbackId());		
		history.setHrFbStatus(feedback.getHrFbStatus());
		history.setRating(feedback.getRating());
		history.setInterviewerFbStatus(feedback.getInterviewerFbStatus());
		iFeedbackDao.deleteById(feedback.getFeedbackId());
		}
		List<Candidate> candiList=new ArrayList();
		candiList.add(candi);
		if(scheduleService.existsScheduleByCandidate(candiList))
		{
			//Schedule schedule=scheduleService.findByCandidate(candiList);
			//scheduleService.deleteSchedule(schedule.getScheduleId());
			List<Schedule> listOfSchedule=scheduleDao.findByCandidate(candi);
			scheduleDao.deleteAll(listOfSchedule);
		}
		History historyAfterSave=historyService.saveHistory(history);
		if(historyAfterSave!=null)
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

	@Override
	public boolean existsCandidateByEmail(String email) {
		// TODO Auto-generated method stub
		boolean ifExist=iCandidateDao.existsCandidateByEmail(email);
		return ifExist;
	}

	@Override
	public Candidate findCandidateByEmail(String email) {
		// TODO Auto-generated method stub
		Candidate candi=iCandidateDao.findCandidateByEmail(email);
		return candi;
	}
	@Scheduled(cron = "0 45 10 * * ?")
	public void autoMoveCandidateToHistory() {
		System.out.println("entered @ Schedule cron deleted method");
		List<Candidate> candiList=iCandidateDao.findAll();
		List<Integer> candidateToDelete=new ArrayList<>();
		LocalDate today=LocalDate.now();
		LocalDate thirtyDay=today.minusDays(30);
		for(Candidate candi:candiList) {
			if(candi.getCreatedAt().equals(thirtyDay)) {
				candidateToDelete.add(candi.getCandidateId());
			}
//			deleteAllCandidates(candidateToDelete);
		}
	}
	@Override
	public void deleteAllCandidates(List<Integer> candiIds) {
		// TODO Auto-generated method stub
		List<Candidate> candiList=iCandidateDao.findAllById(candiIds);
		List<History> historyList=new ArrayList<>();
		for(Candidate candi:candiList) {
			History history=new History();
			history.setAlternateEmail(candi.getAlternateEmail());
			history.setAlternateMobileNumber(candi.getAlternateMobileNumber());
			history.setCandidateId(candi.getCandidateId());
			history.setCandidateName(candi.getCandidateName());
			history.setCgpa(candi.getCgpa());
			history.setCurrentCtc(candi.getCurrentCtc());
			history.setHighQualification(candi.getHighQualification());
			history.setEmail(candi.getEmail());
			history.setMobileNumber(candi.getMobileNumber());
			history.setResume(candi.getResume());
			history.setResumeName(candi.getResumeName());
			history.setRoleAppliedFor(candi.getRoleAppliedFor());		
			history.setUserName(candi.getUser().getUsername());
			history.setDomainName(candi.getDomain().getDomainName());
			history.setExpectedCtc(candi.getExpectedCtc());
			history.setExperience(candi.getExperience());
			history.setStatus(candi.getStatus());		                                                        
			if(iFeedbackDao.existsFeedbackByCandidate(candi)) {
			Feedback feedback=iFeedbackDao.findByCandidate(candi);
			history.setDomainRatings(feedback.getDomainRatings());
			System.out.println("Feedback domain Ratings"+feedback.getDomainRatings());
			history.setRating(feedback.getRating());
			history.setFeed_back(feedback.getFeed_back());
			history.setFeedbackId(feedback.getFeedbackId());		
			history.setHrFbStatus(feedback.getHrFbStatus());
			history.setInterviewerFbStatus(feedback.getInterviewerFbStatus());
			iFeedbackDao.deleteById(feedback.getFeedbackId());
			}
			List<Candidate> candiList1=new ArrayList();
			candiList1.add(candi);
			if(scheduleService.existsScheduleByCandidate(candiList1))
			{
				//Schedule schedule=scheduleService.findByCandidate(candiList);
				//scheduleService.deleteSchedule(schedule.getScheduleId());
				List<Schedule> listOfSchedule=scheduleDao.findByCandidate(candi);
				scheduleDao.deleteAll(listOfSchedule);
			}
			History historyAfterSave=historyService.saveHistory(history);
			historyList.add(historyAfterSave);
		}
		if(historyList.size()==candiList.size())
		iCandidateDao.deleteAllByIdInBatch(candiIds);
	}
}
