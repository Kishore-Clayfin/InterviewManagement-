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
import com.cf.model.User;
import com.cf.model.UserDetails;
import com.cf.service.ICandidateService;
import com.cf.service.IUserDetailsService;
import com.cf.service.IUserService;

@Controller
public class UserController 
{

	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private IUserDetailsService iUserDetailsService;
	
	@Autowired
	private ICandidateService iCandidateService;
	
	@GetMapping("/addUser")
	public ModelAndView addUser() {
		List<UserDetails> userDetails = iUserDetailsService.viewUserDetailsList();
		List<Candidate> candidate = iCandidateService.viewCandidateList();
		
		User user = new User();
		ModelAndView mav = new ModelAndView("userRegister");
		mav.addObject("user",user);
		//mav.addObject("details",userDetails);
		//mav.addObject("candidate",candidate);
		return mav;
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user) {
//		UserDetails userDetails=iUserDetailsService.findUserDetails(userDetailsId);
//		
//		log.info("Iddddd"+userDetailsId);
//		log.info("userDetails"+userDetails);
//		user.setUserDetails(userDetails);
		user.setEnabled(true);
		iUserService.saveUser(user);
		//UserDetails userDetails=user1.getUserDetails();
//		userId=user1.getUserId();
		return "redirect:/viewUsers";
	}
	
	@GetMapping("/viewUsers")
	public ModelAndView getAllUsers() {
		ModelAndView mav = new ModelAndView("userList");
		mav.addObject("user", iUserService.viewUserList());
		//mav.addObject("userDetails", iUserDetailsService.viewUserDetailsList());
		return mav;
	}
	
	@GetMapping("/showUpdateUser")
	public ModelAndView showUpdateUser(@RequestParam Integer userId) {
//		List<UserDetails> userDetails = iUserDetailsService.viewUserDetailsList();
//		List<Candidate> candidate = iCandidateService.viewCandidateList();
//		
		ModelAndView mav = new ModelAndView("userRegister");
		User user = iUserService.updateUser(userId);
		mav.addObject("user", user);
//		mav.addObject("userDetails",userDetails);
//		mav.addObject("candidate",candidate);
		return mav;
	}
	
	@GetMapping("/deleteUser")
	public String deleteUser(@RequestParam Integer userId) {
		iUserService.deleteUser(userId);
		return "redirect:/viewUsers";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Autowired
//	private IUserService iUserService;
//	
//	@Autowired
//	private IUserDetailsService iUserDetailsService;
//	
//	@Autowired
//	private ICandidateService iCandidateService;
//	
//	@GetMapping("/addUser")
//	public ModelAndView addUser() {
//		List<UserDetails> userDetails = iUserDetailsService.viewUserDetailsList();
//		List<Candidate> candidate = iCandidateService.viewCandidateList();
//		
//		User user = new User();
//		ModelAndView mav = new ModelAndView("userRegister");
//		mav.addObject("user",user);
//		mav.addObject("userDetails",userDetails);
//		mav.addObject("candidate",candidate);
//		return mav;
//	}
//	
//	@PostMapping("/saveUser")
//	public String saveUser(@ModelAttribute User user) {
//		iUserService.saveUser(user);
//		return "redirect:/viewUsers";
//	}
//	
//	@GetMapping("/viewUsers")
//	public ModelAndView getAllUsers() {
//		ModelAndView mav = new ModelAndView("userList");
//		mav.addObject("user", iUserService.viewUserList());
//		return mav;
//	}
//	
//	@GetMapping("/showUpdateUser")
//	public ModelAndView showUpdateUser(@RequestParam Integer userId) {
//		List<UserDetails> userDetails = iUserDetailsService.viewUserDetailsList();
//		List<Candidate> candidate = iCandidateService.viewCandidateList();
//		
//		ModelAndView mav = new ModelAndView("userRegister");
//		User user = iUserService.updateUser(userId);
//		mav.addObject("user", user);
//		mav.addObject("userDetails",userDetails);
//		mav.addObject("candidate",candidate);
//		return mav;
//	}
//	
//	@GetMapping("/deleteUser")
//	public String deleteUser(@RequestParam Integer userId) {
//		iUserService.deleteUser(userId);
//		return "redirect:/viewUsers";
//	}
}
