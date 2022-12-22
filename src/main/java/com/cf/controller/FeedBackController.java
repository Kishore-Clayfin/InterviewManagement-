package com.cf.controller;

import java.io.IOException;
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
	public ModelAndView addFeedback(@RequestParam Integer candidateId, HttpSession session,
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
		if (!(checkUser.getRole().equals("interviewer") || checkUser.getRole().equals("hrHead"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
		mav.addObject("role", user.getRole());
		return mav;
	}

	@PostMapping("/saveFeedback")
	public String saveFeedBack(@Valid @ModelAttribute Feedback feedback, BindingResult result, Model model,
			@RequestParam Integer candidateId, HttpSession session, HttpServletResponse redirect) {

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

		System.out.println(feedback.getDomainRatings());

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
			feedback.setCandidate(candidate);
			feedback.setSubDomRatings(map);
			iFeedbackService.saveFeedback(feedback);
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
		return "redirect:/viewFeedbacks";

	}

	@GetMapping("/viewFeedbacks")
	public ModelAndView getAllFeedbacks(HttpSession session, HttpServletResponse redirect) {

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

		ModelAndView mav = new ModelAndView("feedbackList");
		mav.addObject("feedback", iFeedbackService.viewFeedbackList());
		return mav;
	}

	@GetMapping("/showUpdateFeedback")
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

	@GetMapping("/generatePdfFile")
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

	@GetMapping("/generateAllPdfFile")
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
	@GetMapping("/updateInterviewerFbStatus")
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
