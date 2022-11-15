package com.cf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cf.model.Candidate;
import com.cf.model.Domain;
import com.cf.model.Feedback;
import com.cf.service.ICandidateService;
import com.cf.service.IDomainService;
import com.cf.service.IFeedbackService;


@Controller
public class FeedBackController {

	@Autowired
	private IFeedbackService iFeedbackService;
	
	@Autowired
	private IDomainService iDomainService;
	
	@Autowired
	private ICandidateService iCandidateService;
	
	@GetMapping("/addFeedback")
	public ModelAndView addFeedback() {
		List<Domain> domain = iDomainService.viewDomainList();
		List<Candidate> candidate = iCandidateService.viewCandidateList();
		
		Feedback feedback = new Feedback();
		ModelAndView mav = new ModelAndView("feedbackRegister");
		mav.addObject("feedback",feedback);
		mav.addObject("domain",domain);
		mav.addObject("candidate",candidate);
		return mav;
	}
	
	@PostMapping("/saveFeedback")
	public String saveFeedBack(@ModelAttribute Feedback feedback) {
		iFeedbackService.saveFeedback(feedback);
		return "redirect:/viewFeedbacks";
	}
	
	@GetMapping("/viewFeedbacks")
	public ModelAndView getAllFeedbacks() {
		ModelAndView mav = new ModelAndView("feedbackList");
		mav.addObject("feedback", iFeedbackService.viewFeedbackList());
		return mav;
	}
	
	@GetMapping("/showUpdateFeedback")
	public ModelAndView showUpdateFeedback(@RequestParam Integer feedbackId) {
		List<Domain> domain = iDomainService.viewDomainList();
		List<Candidate> candidate = iCandidateService.viewCandidateList();
		
		ModelAndView mav = new ModelAndView("feedbackRegister");
		Feedback feedback = iFeedbackService.updateFeedback(feedbackId);
		mav.addObject("feedback", feedback);
		mav.addObject("domain",domain);
		mav.addObject("candidate",candidate);
		return mav;
	}
	
	@GetMapping("/deleteFeedback")
	public String deleteFeedback(@RequestParam Integer feedbackId) {
		iFeedbackService.deleteFeedback(feedbackId);
		return "redirect:/viewFeedbacks";
	}
}
