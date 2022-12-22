package com.cf.service;

import java.util.List;


import com.cf.model.Candidate;
import com.cf.model.Feedback;
import com.cf.model.Schedule;

public interface IFeedbackService {

	void saveFeedback(Feedback feedback);

	Object viewFeedbackList();

	Feedback updateFeedback(Integer feedbackId);

	void deleteFeedback(Integer feedbackId);
	
	
Feedback getDetailsById(Integer id);
	
	List<Feedback> getAllFeedback();
	
	
	
	
	Feedback updateInterviewerFbStatus(Integer id,String status);
	
}
