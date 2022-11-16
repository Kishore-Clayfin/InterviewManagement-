package com.cf.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.cf.model.Candidate;
import com.cf.model.Domain;
import com.cf.model.User;
import com.cf.service.ICandidateService;
import com.cf.service.IDomainService;

@Controller
public class CandidateController
{ 
	@Autowired
	private ICandidateService iCandidateService;
	
	@Autowired
	private IDomainService iDomainService;
	
	@GetMapping("/addCandidate")
	public ModelAndView addCandidate() {
		List<Domain> domain = iDomainService.viewDomainList();
		
		Candidate candidate = new Candidate();
		ModelAndView mav = new ModelAndView("candidateRegister");
		mav.addObject("candidate",candidate);
		mav.addObject("domain",domain);
		return mav;
	}
//	@PostMapping("/saveCandidate")
//	public String saveCandidate(@ModelAttribute Candidate candidate,BindingResult result,Model mav,HttpSession session) 
//	{
//		User obj=(User) session.getAttribute("loginDetails");
//		User user=new User();
////		System.out.println(obj.getUserId());
//		List<Domain> domain = iDomainService.viewDomainList();
//		
//		mav.addAttribute("domain",domain);
//		
//		if(result.hasErrors()) 
//		{
//			return "candidateRegister";
//			
//		}
//		user.setUserId(obj.getUserId());
//		candidate.setUser(user);
//		iCandidateService.saveCandidate(candidate);
//		return "redirect:/viewCandidates";
//	}
	
	@PostMapping("/saveCandidate")
	public String saveCandidate(@Valid @ModelAttribute Candidate candidate,BindingResult result,@RequestParam("file") MultipartFile file,Model mav,HttpSession session) throws IOException 
	{
		
		User obj=(User) session.getAttribute("loginDetails");
		User user=new User();

		List<Domain> domain = iDomainService.viewDomainList();
		
		mav.addAttribute("domain",domain);
		
		boolean b=result.hasErrors();
		System.err.println(b);
		
		String name=file.getOriginalFilename();
		
		System.err.println("hiiiiiii"+name);
 
    
		
		if(result.hasErrors()) 
		{
			return "candidateRegister";
			
		}
		candidate.setResume(file.getBytes());
		user.setUserId(obj.getUserId());
		candidate.setUser(user);
		iCandidateService.saveCandidate(candidate);
		

		
		return "redirect:/viewCandidates";
	}
	@GetMapping("/viewCandidates")
	public ModelAndView getAllCandidates(HttpSession session) 
	{
//		System.out.println(session.getAttribute("details"));
//		System.out.println(user);
		ModelAndView mav = new ModelAndView("candidateList");
		mav.addObject("candidate", iCandidateService.viewCandidateList());
		return mav;
	}
	
	@GetMapping("/showUpdateCandidate")
	public ModelAndView showUpdateCandidate(@RequestParam Integer candidateId) {
		List<Domain> domain = iDomainService.viewDomainList();
		
		ModelAndView mav = new ModelAndView("candidateRegister");
		Candidate candidate = iCandidateService.updateCandidate(candidateId);
		mav.addObject("candidate", candidate);
		mav.addObject("domain",domain);
		return mav;
	}
	
	@GetMapping("/updateStatus")
	public String updateCandidateStatus(@RequestParam Integer candidateId ,@RequestParam String status)
	{
		
		Candidate candi= iCandidateService.updateCandidateStatus(candidateId,status);
		return "redirect:/viewschedules";
	}
	
	@GetMapping("/deleteCandidate")
	public String deleteCandidate(@RequestParam Integer candidateId) {
		iCandidateService.deleteCandidate(candidateId);
		return "redirect:/viewCandidates";
	}
	
	@GetMapping("/downloadFile")
	 public void downloadFile(@RequestParam Integer candidateId , Model model, HttpServletResponse response) throws IOException {
			Candidate  candidate1= iCandidateService.findResumeCandidate(candidateId);
	  if(candidate1!=null) {

	  
	  
	   response.setContentType("application/octet-stream");
	   String headerKey = "Content-Disposition";
	   String headerValue = "attachment; filename = "+candidate1.getCandidateName()+".docx";
	   response.setHeader(headerKey, headerValue);
	   ServletOutputStream outputStream = response.getOutputStream();
	   outputStream.write(candidate1.getResume());
	   outputStream.close();
	  }
	 }
	
}