package com.cf.controller;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cf.model.Candidate;
import com.cf.model.Domain;
import com.cf.model.User;
import com.cf.repository.ICandidateDao;
import com.cf.service.ICandidateService;
import com.cf.service.IDomainService;

@Controller
public class CandidateController {
	@Autowired
	private ICandidateService iCandidateService;

	@Autowired
	private IDomainService iDomainService;

	@Autowired
	private ICandidateDao iCandidateDao;
	@GetMapping("/addCandidate")
	public ModelAndView addCandidate(HttpSession session, HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Domain> domain = iDomainService.viewDomainList();

		Candidate candidate = new Candidate();
		ModelAndView mav = new ModelAndView("candidateRegister");
		mav.addObject("candidate", candidate);
		mav.addObject("domain", domain);
		return mav;
	}

	@PostMapping("/saveCandidate")
	public String saveCandidate(@Valid @ModelAttribute Candidate candidate, BindingResult result,
			@RequestParam("file") MultipartFile file, Model mav, HttpSession session, RedirectAttributes attributes,
			HttpServletResponse redirect) throws IOException {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String mob;
		Long nullcheck;
		User obj = (User) session.getAttribute("loginDetails");
		User user = new User();

		List<Domain> domain = iDomainService.viewDomainList();

		mav.addAttribute("domain", domain);

		boolean b = result.hasErrors();
		System.err.println(b);

		String name = file.getOriginalFilename();

		System.err.println("hiiiiiii" + name);

		nullcheck = candidate.getMobileNumber();

		if (result.hasErrors()) {
			return "candidateRegister";

		} else if (nullcheck == null) {
			attributes.addAttribute("numberNull", "Please enter a  mobile number");
			return "redirect:/addCandidate";
		}

		mob = candidate.getMobileNumber().toString();

		boolean mobv = Pattern.matches("^[9876]\\d{9}$", mob);

		if (!mobv == true) {
			attributes.addAttribute("numberError", "Please enter a valid mobile number");
			return "redirect:/addCandidate";
		}

		if (!file.getContentType().endsWith("pdf")) {
			attributes.addAttribute("fileError", "Only pdf format resume is allowed");
			return "redirect:/addCandidate";

		}
		if ((candidate.getExperience() == null)) {
			candidate.setExpectedCtc(0.0f);
			candidate.setCurrentCtc(0.0f);

		}
		candidate.setResume(file.getBytes());
		user.setUserId(obj.getUserId());
		candidate.setUser(user);
		iCandidateService.saveCandidate(candidate);

		return "redirect:/viewCandidates";
	}

	
	@GetMapping("/viewCandidates")
	public ModelAndView getAllCandidates(HttpSession session, HttpServletResponse redirect) {
		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if ((!checkUser.getRole().equals("hr"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ModelAndView mav = new ModelAndView("candidateList");
		mav.addObject("candidate", iCandidateService.viewCandidateList());
		return mav;
	}

	@GetMapping("/showUpdateCandidate")
	public ModelAndView showUpdateCandidate(@RequestParam Integer candidateId, HttpSession session,
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
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Domain> domain = iDomainService.viewDomainList();

		ModelAndView mav = new ModelAndView("candidateRegister");
		Candidate candidate = iCandidateService.updateCandidate(candidateId);
		mav.addObject("candidate", candidate);
		mav.addObject("domain", domain);
		return mav;
	}
	@GetMapping("/updateExpectedCtc")
	public ModelAndView updateExpectedCtc(@RequestParam Integer candidateId, HttpSession session,
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
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Domain> domain = iDomainService.viewDomainList();

		ModelAndView mav = new ModelAndView("candidateUpdateCtc");
		Candidate candidate = iCandidateService.updateCandidate(candidateId);
		mav.addObject("candidate", candidate);
		mav.addObject("domain", domain);
		return mav;
	}
	
	@PostMapping("/saveExpectedCtc")
	public String saveExpectedCtc(@ModelAttribute Candidate candidate, HttpSession session, RedirectAttributes attributes,
			HttpServletResponse redirect) throws IOException {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("candidate from model and view"+candidate.getExpectedCtc());
      Candidate candidate1=iCandidateService.updateCandidate(candidate.getCandidateId());
      System.out.println("candidate from jpa repo"+candidate1);
      candidate1.setExpectedCtc(candidate.getExpectedCtc());
      System.out.println("candidate after setting expected ctc"+candidate1.getExpectedCtc());
    Candidate dummy= iCandidateDao.save(candidate1);
    System.out.println("updated candidate"+dummy);
		return "redirect:/showInterviewCompleted";
	}

	@Transactional
	@GetMapping("/updateStatus")
	public String updateCandidateStatus(@RequestParam Integer candidateId, @RequestParam String status,
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

		Candidate candi = iCandidateService.updateCandidateStatus(candidateId, status);
		return "redirect:/viewschedules";
	}

	@GetMapping("/deleteCandidate")
	public String deleteCandidate(@RequestParam Integer candidateId, HttpSession session,
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
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		iCandidateService.deleteCandidate(candidateId);
		return "redirect:/viewCandidates";
	}

	@GetMapping("/downloadFile")
	public void downloadFile(@RequestParam Integer candidateId, Model model, HttpSession session,
			HttpServletResponse response) throws IOException {

		if (LoginController.checkUser == null) {
			try {
				response.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				response.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Candidate candidate1 = iCandidateService.findResumeCandidate(candidateId);
		if (candidate1 != null) {

			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename = " + candidate1.getCandidateName() + ".pdf";
			response.setHeader(headerKey, headerValue);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(candidate1.getResume());
			outputStream.close();
		}
	}

}