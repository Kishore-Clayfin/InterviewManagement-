package com.cf.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cf.FeedbackConstants;
import com.cf.model.Candidate;
import com.cf.model.Domain;
import com.cf.model.DomainCategory;
import com.cf.model.Feedback;
import com.cf.model.User;
import com.cf.service.ICandidateService;
import com.cf.service.IDomainService;
import com.cf.service.IFeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class FeedBackController {

	@Autowired
	private IFeedbackService iFeedbackService;
	
	@Autowired
	private IDomainService iDomainService;
	
	@Autowired
	private ICandidateService iCandidateService;
	
	@GetMapping("/addFeedback")
	public ModelAndView addFeedback(@RequestParam Integer candidateId ,HttpSession session) 
	{
		
		User user= (User) session.getAttribute("loginDetails");
//		List<Domain> domain = iDomainService.viewDomainList();
//		List<Candidate> candidate = iCandidateService.viewCandidateList();
		Candidate candidate= iCandidateService.updateCandidate(candidateId);
		List<DomainCategory> domainCategory=iCandidateService.updateCandidate(candidateId).getDomain().getDomainCategory();
//		System.out.println(domainCategory);
		Feedback feedback = new Feedback();
		ModelAndView mav = new ModelAndView("feedbackRegister");
		mav.addObject("feedback",feedback);
//		mav.addObject("domain",domain);
		mav.addObject("candidateId",candidateId);
		mav.addObject("candidate",candidate);
		mav.addObject("subCategory", domainCategory);
		mav.addObject("role",user.getRole());
		return mav;
	}
	
	@PostMapping("/saveFeedback")
	public String saveFeedBack(@ModelAttribute Feedback feedback, Model model,@RequestParam Integer candidateId) 
	{
//		Domain domain = feedback.getCandidate().getDomain();
//		List<DomainCategory> domainCategory = domain.getDomainCategory();
//		
//		for(DomainCategory dc: domainCategory) {
//			String domSubCat = dc.getDomSubCatName();
//		}
//		
		
		System.out.println(feedback.getDomainRatings());
		
		ObjectMapper mapper = new ObjectMapper();
        String json = feedback.getDomainRatings();
        Map<String, Integer> map=null;
        try 
        {

            // convert JSON string to Map
            map = mapper.readValue(json, Map.class);

			// it works
            //Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
		
		
		
		
		
		
		Candidate candidate=new Candidate();
		candidate.setCandidateId(candidateId);
		String GENERAL_MSG="";
		List<Candidate> candidates=null;
		try{
			
			feedback.setCandidate(candidate);
			feedback.setSubDomRatings(map);
			iFeedbackService.saveFeedback(feedback);
			GENERAL_MSG=FeedbackConstants.FEEDBACK_SUCCESS_MSG;
		}
		catch(Exception e)
		{
			//System.out.println("in catch");
			candidates=iCandidateService.viewCandidateList();
			//System.out.println(e);
			GENERAL_MSG="error while adding "+e.getMessage();
			if(e instanceof DataIntegrityViolationException){
			//	System.out.println("in if");
				GENERAL_MSG="data is already added for :: "+candidates.get(0).getCandidateName();
			}
		}
		model.addAttribute("FB_MSG",GENERAL_MSG);
		model.addAttribute("feedback",feedback);
		model.addAttribute("candidate",candidates);
		return "redirect:/viewFeedbacks";
		
		
//		iFeedbackService.saveFeedback(feedback);
//		return "redirect:/viewFeedbacks";
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
