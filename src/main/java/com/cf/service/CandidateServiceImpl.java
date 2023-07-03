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
	@Autowired 
	private EmailService email;
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
		if(candidate.getExperience()==0) {
			candidate.setMaxRound(2);
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
			if(candi.getCreatedAt().equals(thirtyDay)&&(candi.getStatus().contains("Rejected"))) {
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
	
public String sendApplicationViaMail() {
	String text="<a href=\"http://localhost:9091/candidateForm\">Open Form</a>";
	String to="shrikiranyogi@gmail.com";
	 System.out.println(to);
		email.sendSimpleMessage(to, "Interview", text);
//		email.sendSimpleMessage(schedule.getUser().getEmail(), "Meeting", s.getMeetingLink());
	return null;
	}

public String uiShowStatus(String status) {
	String uiStatus="";
	System.out.println("status before : "+status);
	 if(status.endsWith("Absent")) {
		uiStatus="Absent";
	}
	else if(status.contains("Waiting")) {
		uiStatus="Hold";
	}
	else if(status.contains("Disconnected")) {
		uiStatus="Disconnected";
	}
//	if(status!=null) {
//		String firstWord="";
//		String lastWord="";
//		if(status.startsWith("Technical")) {
//			firstWord="1st"+" "+"Level"+" "+
//		}else if(status.startsWith("Second")) {
//			
//		}else if(status.startsWith("Third")) {
//			
//		}else if(status.startsWith("Fourth")) {
//			
//		}else if(status.startsWith("HR")) {
//			
//		}
//	}
	if(!(status.contains("Absent")&&status.contains("Waiting")&&status.contains("Disconnected"))) {
	switch(status) {
	case "ResumeShortlisted":
		uiStatus="Resume Shortlisted";
		break;
	case "TechnicalScheduled":
		uiStatus="1st Level Scheduled";
		break;
	case "SecondTechnicalScheduled":
		uiStatus="2nd Level Scheduled";
		break;
	case "ThirdTechnicalScheduled":
		uiStatus="3rd Level Scheduled";
		break;
	case "FourthTechnicalScheduled":
		uiStatus="4th Level Scheduled";
		break;	
	case "HRRoundScheduled":
		uiStatus="HR Round Scheduled";
		break;
	case "TechnicalCompleted":
		uiStatus="1st Level Completed";
		break;
	case "SecondTechnicalCompleted":
		uiStatus="2nd Level Completed";
		break;
	case "ThirdTechnicalCompleted":
		uiStatus="3rd Level Scheduled";
		break;
	case "FourthTechnicalCompleted":
		uiStatus="4th Level Completed";
		break;	
	case "HRRoundCompleted":
		uiStatus="HR Round Completed";
		break;
	case "FirstTechnicalSelected":
		uiStatus="1st Level Selected";
		break;
	case "SecondTechnicalSelected":
		uiStatus="2nd Level Selected";
		break;
	case "ThirdTechnicalSelected":
		uiStatus="3rd Level Selected";
		break;
	case "FourthTechnicalSelected":
		uiStatus="4th Level Selected";
		break;	
	case "HRRoundSelected":
		uiStatus="HR Round Selected";
		break;	
	case "TechnicalRejected":
		uiStatus="1st Level Rejected";
		break;
	case "SecondTechnicalRejected":
		uiStatus="2nd Level Rejected";
		break;	
	case "ThirdTechnicalRejected":
		uiStatus="3rd Level Rejected";
		break;
	case "FourthTechnicalRejected":
		uiStatus="4th Level Rejected";
		break;	
	case "HRRoundRejected":
		uiStatus="HR Round Rejected";
		break;
	case "Offered":
		uiStatus="Offered";
		break;
	case "Joined":
		uiStatus="Joined";
		break;
	case "OfferDeclined":
		uiStatus="Offered Declined";
		break;
	}
	}
	
	System.err.println("changed status"+uiStatus);
	return uiStatus;
}

@Override
public Candidate changeCandidateStatus(Candidate candidate,String status) {
	int currentRound=candidate.getCurrentRound();
	if(status.equalsIgnoreCase("FirstRound")) {
		candidate.setStatus("TechnicalScheduled");
		currentRound++;
	}else if(status.equalsIgnoreCase("nextTechnicalRound")) {
		if(candidate.getStatus().equalsIgnoreCase("TechnicalCompleted")) {
			candidate.setStatus("SecondTechnicalScheduled");
		}
		if(candidate.getStatus().equalsIgnoreCase("FirstTechnicalSelected")) {
			candidate.setStatus("SecondTechnicalScheduled");
		}
		else if(candidate.getStatus().equalsIgnoreCase("SecondTechnicalSelected")) {
			candidate.setStatus("ThirdTechnicalScheduled");
		}
		else if(candidate.getStatus().equalsIgnoreCase("ThirdTechnicalSelected")) {
			candidate.setStatus("FourthTechnicalScheduled");
		}
		currentRound++;
		}else if(status.equalsIgnoreCase("hrRound")) {
			candidate.setStatus("HRRoundScheduled");
			currentRound++;
		}else if(candidate.getStatus().endsWith("Disconnected")) {
			String status3=candidate.getStatus().replace("Disconnected", "Scheduled");
			candidate.setStatus(status3);
		}
		else if(candidate.getStatus().endsWith("Absent")) {
			System.out.println("when absent : "+candidate.getStatus());
			String status3=candidate.getStatus().replace("Absent", "Scheduled");
			System.err.println("after changing to scheduled: "+status3);
			candidate.setStatus(status3);
		}
	candidate.setCurrentRound(currentRound);
	return candidate;
}
@Override
public List<Candidate> filterByStatus(String status){
	List<Candidate> candidatesFromDB=viewCandidateList();
	List<Candidate> candidatesFiltered=new ArrayList<>();
	for(Candidate candidate:candidatesFromDB) {
		if(candidate.getStatus()==status) {
			candidatesFiltered.add(candidate);
		}
	}
	return candidatesFiltered;
}

@Override
public List<Candidate> changeCandidateListStatus(List<Candidate> listOfCandidate, String status) {
	List<Candidate> candidateListAfterUpdate=new ArrayList<>();
	for(Candidate candidate:listOfCandidate) {
		candidate=changeCandidateStatus(candidate, status);
		candidateListAfterUpdate.add(candidate);
	}
	return candidateListAfterUpdate;
}

}
