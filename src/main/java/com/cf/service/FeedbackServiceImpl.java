package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.model.Feedback;
import com.cf.repository.IFeedbackDao;

@Service
public class FeedbackServiceImpl implements IFeedbackService {

	@Autowired
	private IFeedbackDao iFeedbackDao;
	
	@Override
	public void saveFeedback(Feedback feedback) {
		iFeedbackDao.save(feedback);
	}

	@Override
	public Object viewFeedbackList() {
		return iFeedbackDao.findAll();
	}

	@Override
	public Feedback updateFeedback(Integer feedbackId) {
		return iFeedbackDao.findById(feedbackId).get();
	}

	@Override
	public void deleteFeedback(Integer feedbackId) {
		iFeedbackDao.deleteById(feedbackId);
	}

}
