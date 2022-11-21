package com.cf.service;

import java.util.List;


import com.cf.model.Candidate;
import com.cf.model.Feedback;

public interface IFeedbackService {

	void saveFeedback(Feedback feedback);
//	Feedback findById(int id);
//	Feedback save(Feedback feedback);
//	void deleteById(int id);
//	List<Feedback> findAll();

	Object viewFeedbackList();

	Feedback updateFeedback(Integer feedbackId);

	void deleteFeedback(Integer feedbackId);
	
	
Feedback getDetailsById(Integer id);
	
	List<Feedback> getAllFeedback();
	
}
