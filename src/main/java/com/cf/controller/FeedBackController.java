package com.cf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cf.FeedbackConstants;
import com.cf.PdfUtilHelper;
import com.cf.model.Candidate;
import com.cf.model.Domain;
import com.cf.model.DomainCategory;
import com.cf.model.Feedback;
import com.cf.model.User;
import com.cf.repository.IDomainDao;
import com.cf.service.DomainServiceImpl;
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
	
	@Autowired
	private IDomainDao dom;

	@GetMapping("/commonInterviewerAndhrHead/addFeedback")
	public ModelAndView addFeedback(@RequestParam Integer candidateId, HttpSession session,
			HttpServletResponse redirect) {
		Domain d=dom.findByDomainName("HR Head Rating");
		List<DomainCategory> domCat=d.getDomainCategory();
		User user = (User) session.getAttribute("loginDetails");
		Candidate candidate = iCandidateService.updateCandidate(candidateId);
		List<DomainCategory> domainCategory = iCandidateService.updateCandidate(candidateId).getDomain()
				.getDomainCategory();
		Feedback feedback = new Feedback();
		ModelAndView mav = new ModelAndView("feedbackRegister");
		mav.addObject("feedback", feedback);
		mav.addObject("candidateId", candidateId);
		mav.addObject("candidate", candidate);
		for (DomainCategory dc : candidate.getDomain().getDomainCategory())
			System.out.println(dc.getDomSubCatName().length());
		mav.addObject("subCategory", domainCategory);
		mav.addObject("role","interviewer" /*user.getRole()*/);
		mav.addObject("hrRating", domCat);
		return mav;
	}

	@PostMapping("/commonInterviewerAndhrHead/saveFeedback")
	public String saveFeedBack(@Valid @ModelAttribute Feedback feedback, BindingResult result, Model model,
			@RequestParam Integer candidateId, HttpSession session, HttpServletResponse redirect) {
Candidate candidate1=iCandidateService.updateCandidate(candidateId);
System.out.println(feedback.getInterviewerFbStatus());
System.out.println(feedback.getHrFbStatus());
		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!(checkUser.getRole().equals("interviewer") || checkUser.getRole().equals("hrHead"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	//	System.out.println(feedback.getDomainRatings());

		ObjectMapper mapper = new ObjectMapper();
		String json = feedback.getDomainRatings();
		Map<String, Integer> map = null;
		try {

			// convert JSON string to Map
			map = mapper.readValue(json, Map.class);

			// it works
			// Map<String, String> map = mapper.readValue(json, new
			// TypeReference<Map<String, String>>() {});

		} catch (IOException e) {
			e.printStackTrace();
		}

		Candidate candidate = new Candidate();
		candidate.setCandidateId(candidateId);
		String GENERAL_MSG = "";
		List<Candidate> candidates = null;
		try {

			if (result.hasErrors()) {
				return "feedbackRegister";
			}
			System.out.println("check if candidate1 already exists"+iFeedbackService.existsFeedbackByCandidate(candidate1));
			if(iFeedbackService.existsFeedbackByCandidate(candidate1)) {
				System.out.println("existFeedbackBy Candidate");
				String interviewRound="";
				Feedback feedback1=iFeedbackService.findByCandidate(candidate1);
				System.out.println(candidate1);
				System.out.println(feedback1);
				feedback1.setCandidate(candidate);
				Map map1=feedback1.getSubDomRatings();
//				map1.putAll(map);
//				feedback1.setSubDomRatings(map1);
				System.out.println(feedback.getInterviewerFbStatus());
				if(feedback.getInterviewerFbStatus()!=null) 
				{
				feedback1.setInterviewerFbStatus(feedback.getInterviewerFbStatus());
				Map<String,Integer> feedbackModelAndView=feedback.getSubDomRatings();
				Map<String,Integer> feedbackExisting=feedback1.getSubDomRatings();
				System.out.println("inside if"+feedback.getInterviewerFbStatus());
				System.out.println(feedback1);
				if(candidate1.getStatus().equalsIgnoreCase("SecondTechnicalCompleted")) {
					String interviewerStatus="First-Interviewer: "+feedback1.getInterviewerFbStatus()+" & "+"Second-Interviewer: "+feedback.getInterviewerFbStatus();
					feedback1.setInterviewerFbStatus(interviewerStatus);
					System.out.println("inside SecondTechnicalCompleted if"+feedback.getHrFbStatus());
					String SecondInterviewer=feedback1.getFeed_back()+" & "+"Second-Interviewer: "+feedback.getFeed_back();
					feedback1.setFeed_back(SecondInterviewer);
					System.out.println(feedback1.getFeed_back());
					interviewRound="Second";
					Map<String,Integer> map2=new HashMap<>();
					System.out.println("Map from ui"+map);
					for(Map.Entry<String,Integer> firstMap:map.entrySet()) {
//						String jsonKey=firstMap.getKey();
						String updatedKey="Second-Round "+firstMap.getKey();
//						map.put(updatedKey,map.remove(jsonKey));
						map2.put(updatedKey, firstMap.getValue());
						System.out.println(updatedKey);
						System.out.println(firstMap.getValue());
					}
					System.out.println("existing feedback from database"+feedbackExisting);
					System.out.println("Map after iteration Map2"+map2);
					feedbackExisting.putAll(map2);
					System.out.println("Map afer Merging with map2 and feedbackExisting"+feedbackExisting);
					feedback1.setSubDomRatings(feedbackExisting);
					
				}
				else if(candidate1.getStatus().equalsIgnoreCase("ThirdTechnicalCompleted")) {
					String interviewerStatus=feedback1.getInterviewerFbStatus()+" & "+"Third-Interviewer: "+feedback.getInterviewerFbStatus();
					feedback1.setInterviewerFbStatus(interviewerStatus);
					System.out.println("inside ThirdTechnicalCompleted if"+feedback.getHrFbStatus());
					String SecondInterviewer=feedback1.getFeed_back()+" & "+"Third-Interviewer: "+feedback.getFeed_back();
					feedback1.setFeed_back(SecondInterviewer);
					System.out.println(feedback1.getFeed_back());
					interviewRound="Third";
					Map<String,Integer> map2=new HashMap<>();
					System.out.println("Map from ui"+map);
					for(Map.Entry<String,Integer> firstMap:map.entrySet()) {
//						String jsonKey=firstMap.getKey();
						String updatedKey="Third-Round "+firstMap.getKey();
//						map.put(updatedKey,map.remove(jsonKey));
						map2.put(updatedKey, firstMap.getValue());
						System.out.println(updatedKey);
						System.out.println(firstMap.getValue());
					}
					System.out.println("existing feedback from database"+feedbackExisting);
					System.out.println("Map after iteration Map2"+map2);
					feedbackExisting.putAll(map2);
					feedback1.setSubDomRatings(feedbackExisting);
				}
				else if(candidate1.getStatus().equalsIgnoreCase("FourthTechnicalCompleted")) {
					String interviewerStatus=feedback1.getInterviewerFbStatus()+" & "+"Fourth-Interviewer: "+feedback.getInterviewerFbStatus();
					feedback1.setInterviewerFbStatus(interviewerStatus);
					System.out.println("inside FourthTechnicalCompleted if"+feedback.getHrFbStatus());
					String SecondInterviewer=feedback1.getFeed_back()+" & "+"Fourth-Interviewer: "+feedback.getFeed_back();
					feedback1.setFeed_back(SecondInterviewer);
					System.out.println(feedback1.getFeed_back());
					interviewRound="Fourth";
					Map<String,Integer> map2=new HashMap<>();
					System.out.println("Map from ui"+map);
					for(Map.Entry<String,Integer> firstMap:map.entrySet()) {
//						String jsonKey=firstMap.getKey();
						String updatedKey="Fourth-Round "+firstMap.getKey();
//						map.put(updatedKey,map.remove(jsonKey));
						map2.put(updatedKey, firstMap.getValue());
						System.out.println(updatedKey);
						System.out.println(firstMap.getValue());
					}
					System.out.println("existing feedback from database"+feedbackExisting);
					System.out.println("Map after iteration Map2"+map2);
					feedbackExisting.putAll(map2);
					feedback1.setSubDomRatings(feedbackExisting);
				}
				}
				else if(feedback.getHrFbStatus()!=null)
				{
					feedback1.setHrFbStatus(feedback.getHrFbStatus());
					System.out.println("inside if"+feedback.getHrFbStatus());
					String hrReview=feedback1.getFeed_back()+" & "+"HrHead-Feedback: "+feedback.getFeed_back();
					feedback1.setFeed_back(hrReview);
					System.out.println(feedback1.getFeed_back());
				}
				Feedback feed=iFeedbackService.saveFeedback(feedback1);
				System.out.println(feed);
				System.out.println(feed.getDomainRatings());
				String status="";
				if(feedback.getInterviewerFbStatus().equalsIgnoreCase("rejected")) {
					status=interviewRound+"TechnicalRejected";
				}
				else if(feedback.getInterviewerFbStatus().equalsIgnoreCase("selected")) {
					status=interviewRound+"TechnicalSelected";
				}else if(feedback.getInterviewerFbStatus().equalsIgnoreCase("waiting")) {
					status=interviewRound+"TechnicalWaiting";
				}else if(feedback.getInterviewerFbStatus().equalsIgnoreCase("disconnected")) {
					status=interviewRound+"TechnicalDisconnected";
				}else if(feedback.getInterviewerFbStatus().equalsIgnoreCase("notattended")) {
					status=interviewRound+"TechnicalAbsent";
				}
				iCandidateService.updateCandidateStatus(candidateId, status);
				System.out.println("after saving"+feed.getFeed_back());
			
			}else {
				String interviewerFeed="First-Interviewer: "+feedback.getFeed_back();
				feedback.setFeed_back(interviewerFeed);
			feedback.setCandidate(candidate);
			Map<String,Integer> map2=new HashMap<>();
			for(Map.Entry<String,Integer> firstMap:map.entrySet()) {
//				String jsonKey=firstMap.getKey();
				String updatedKey="First-Round "+firstMap.getKey();
//				map.put(updatedKey,map.remove(jsonKey));
				map2.put(updatedKey, firstMap.getValue());
				System.out.println(updatedKey);
				System.out.println(firstMap.getValue());
			}
			System.out.println("Map after iteration"+map2);
			feedback.setSubDomRatings(map2);
			Feedback firstFeedback=iFeedbackService.saveFeedback(feedback);
			System.out.println("Map after saving"+firstFeedback.getDomainRatings());
			
			System.out.println("feedback afer saving"+firstFeedback);
			String status="";
			if(feedback.getInterviewerFbStatus().equalsIgnoreCase("rejected")) {
				status="FirstTechnicalRejected";
			}
			else if(feedback.getInterviewerFbStatus().equalsIgnoreCase("selected")) {
				status="FirstTechnicalSelected";
			}else if(feedback.getInterviewerFbStatus().equalsIgnoreCase("waiting")) {
				status="FirstTechnicalWaiting";
			}else if(feedback.getInterviewerFbStatus().equalsIgnoreCase("disconnected")) {
				status="FirstTechnicalDisconnected";
			}else if(feedback.getInterviewerFbStatus().equalsIgnoreCase("notattended")) {
				status="FirstTechnicalAbsent";
			}
			iCandidateService.updateCandidateStatus(candidateId, status);
			}
			GENERAL_MSG = FeedbackConstants.FEEDBACK_SUCCESS_MSG;
		} catch (Exception e) {
			candidates = iCandidateService.viewCandidateList();
			GENERAL_MSG = "error while adding " + e.getMessage();
			if (e instanceof DataIntegrityViolationException) {
				GENERAL_MSG = "data is already added for :: " + candidates.get(0).getCandidateName();
			}
		}
		model.addAttribute("FB_MSG", GENERAL_MSG);
		model.addAttribute("feedback", feedback);
		model.addAttribute("candidate", candidates);
		return "redirect:/giveFeedback";

	}
	@GetMapping("/common/viewFeedbacks")
	public ModelAndView getAllFeedbacks(HttpSession session, HttpServletResponse redirect) {
		List<String> ratings=new ArrayList<>();
//		List<Feedback> feedList=(List<Feedback>) iFeedbackService.viewFeedbackList();
//		for(Feedback feed:feedList) {
//			String mapString=null;
//		Map<String,Integer> mapFeedback=feed.getSubDomRatings();
//		for (Map.Entry<String,Integer> entry : mapFeedback.entrySet()) {
//			 mapString=entry.getKey()+" : "+entry.getValue()+"<br>";
//			
//		}
//		ratings.add(mapString);
//		}
		ModelAndView mav = new ModelAndView("feedbackList");
		mav.addObject("feedback", iFeedbackService.viewFeedbackList());
		mav.addObject("domainRatingsList", ratings);
		return mav;
	}

	@GetMapping("/commonInterviewerAndhrHead/showUpdateFeedback")
	public ModelAndView showUpdateFeedback(@RequestParam Integer feedbackId, HttpSession session,
			HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!(checkUser.getRole().equals("hr") || checkUser.getRole().equals("interviewer")
				|| checkUser.getRole().equals("hrHead"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Domain> domain = iDomainService.viewDomainList();
		List<Candidate> candidate = iCandidateService.viewCandidateList();

		ModelAndView mav = new ModelAndView("feedbackRegister");
		Feedback feedback = iFeedbackService.updateFeedback(feedbackId);
		mav.addObject("feedback", feedback);
		mav.addObject("domain", domain);
		mav.addObject("candidate", candidate);
		return mav;
	}

	@GetMapping("/deleteFeedback")
	public String deleteFeedback(@RequestParam Integer feedbackId, HttpSession session, HttpServletResponse redirect) {
		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!(checkUser.getRole().equals("hr") || checkUser.getRole().equals("interviewer")
				|| checkUser.getRole().equals("hrHead"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		iFeedbackService.deleteFeedback(feedbackId);
		return "redirect:/viewFeedbacks";
	}

	@GetMapping("/common/generatePdfFile")
	public void generatePdfFile(HttpServletResponse response, @RequestParam(value = "feedbackId") Integer feedbackId)
			throws IOException {
		if (LoginController.checkUser == null) {
			try {
				response.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Feedback feedback1 = iFeedbackService.getDetailsById(feedbackId);
		System.out.println(feedbackId);
		System.out.println(feedback1);
		PdfUtilHelper export = new PdfUtilHelper();
		export.export(response, feedback1);

	}

	@GetMapping("/common/generateAllPdfFile")
	public void generateAllPdfFile(HttpServletResponse response) throws IOException {
		if (LoginController.checkUser == null) {
			try {
				response.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Feedback> feedback2 = iFeedbackService.getAllFeedback();
		PdfUtilHelper export = new PdfUtilHelper();

		export.exportAll(response, feedback2);

	}

	@Transactional
	@GetMapping("/common/updateInterviewerFbStatus")
	public String updateInterviewerFbStatus(@RequestParam Integer feedbackId, @RequestParam String interviewerFbStatus,
			HttpSession session, HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!(checkUser.getRole().equals("hr") || checkUser.getRole().equals("hrHead")
				|| checkUser.getRole().equals("interviewer"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Feedback fb = iFeedbackService.updateInterviewerFbStatus(feedbackId, interviewerFbStatus);
		return "redirect:/viewFeedbacks";
	}

}
