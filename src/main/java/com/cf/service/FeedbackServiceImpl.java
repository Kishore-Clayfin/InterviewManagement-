package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.model.Candidate;
import com.cf.model.Feedback;
import com.cf.repository.IFeedbackDao;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class FeedbackServiceImpl implements IFeedbackService {

	@Autowired
	private IFeedbackDao iFeedbackDao;
	
	@Override
	public void saveFeedback(Feedback feedback) {
		iFeedbackDao.save(feedback);
		log.info("Feedback added successfully");
	}

	@Override
	public Object viewFeedbackList() {
		log.info("find all Feedbacks from the database");
		return iFeedbackDao.findAll();
	}

	@Override
	public Feedback updateFeedback(Integer feedbackId) {
		log.info("Feedback with feedbackId "+feedbackId +" updated");
		return iFeedbackDao.findById(feedbackId).get();
	}

	@Override
	public void deleteFeedback(Integer feedbackId) {
		iFeedbackDao.deleteById(feedbackId);
		log.info("Feedback with feedbackId "+feedbackId +" deleted");
	}

	@Override
	public Feedback getDetailsById(Integer id) {
		Feedback feedback=iFeedbackDao.findById(id).orElseThrow();
		log.info("find Feedback with id: "+ id);
		return feedback;
	}

	@Override
	public List<Feedback> getAllFeedback() {
		log.info("find all Feedbacks from the database");
		return iFeedbackDao.findAll();
	}
	
	@Override
	public Feedback updateInterviewerFbStatus(Integer id,String status)
	{
		
		Feedback feedback= iFeedbackDao.findById(id).orElseThrow(null);
		feedback.setInterviewerFbStatus(status);
		
		Feedback f=iFeedbackDao.save(feedback);
		log.info("Updating InterviewerFbStatus with feedbackId:" + id);
		return f;
	}

	@Override
	public Feedback findByCandidate(Candidate candidate) {
		// TODO Auto-generated method stub
		Feedback feedback=iFeedbackDao.findByCandidate(candidate);
//		System.out.println("find by"+feedback);
		return feedback;
	}

	@Override
	public boolean existsFeedbackByCandidate(Candidate candidate) {
		// TODO Auto-generated method stub
		boolean bool=iFeedbackDao.existsFeedbackByCandidate(candidate);
//		System.out.println("does it exist"+bool);
		return bool;
	}


}
