package com.cf.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cf.model.User;
import com.cf.model.UserDetails;
import com.cf.service.IUserDetailsService;


@Controller
public class UserDetailsController {

	@Autowired
	private IUserDetailsService iUserDetailsService;
	
	@GetMapping("/addUserDetails")
	public ModelAndView addUserDetails(HttpSession session,HttpServletResponse redirect) {
		User checkUser=(User)session.getAttribute("loginDetails");
		if(!checkUser.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		UserDetails userDetails = new UserDetails();
		ModelAndView mav = new ModelAndView("userDetailsRegister");
		mav.addObject("userDetails",userDetails);
		return mav;
	}
	
	@PostMapping("/saveUserDetails")
	public String saveUserDetails(@Valid @ModelAttribute UserDetails userDetails,BindingResult result,HttpSession session,HttpServletResponse redirect) 
	{
		User checkUser=(User)session.getAttribute("loginDetails");
		if(!checkUser.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(result.hasErrors())
		{
			return "userDetailsRegister";
		}
		iUserDetailsService.saveUserDetails(userDetails);
		return "redirect:/viewUserDetails";
	}
	
	@GetMapping("/viewUserDetails")
	public ModelAndView getAllUserDetails(HttpSession session,HttpServletResponse redirect) {
		User checkUser=(User)session.getAttribute("loginDetails");
		if(!checkUser.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ModelAndView mav = new ModelAndView("userDetailsList");
		mav.addObject("userDetails", iUserDetailsService.viewUserDetailsList());
		return mav;
	}
	
	@GetMapping("/showUpdateUserDetails")
	public ModelAndView showUpdateUserDetails(@RequestParam Integer userDetailsId,HttpSession session,HttpServletResponse redirect) {
		User checkUser=(User)session.getAttribute("loginDetails");
		if(!checkUser.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ModelAndView mav = new ModelAndView("userDetailsRegister");
		UserDetails userDetails = iUserDetailsService.updateUserDetails(userDetailsId);
		mav.addObject("userDetails", userDetails);
		return mav;
	}
	
	@GetMapping("/deleteUserDetails")
	public String deleteUserDetails(@RequestParam Integer userDetailsId,HttpSession session,HttpServletResponse redirect) {
		User checkUser=(User)session.getAttribute("loginDetails");
		if(!checkUser.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		iUserDetailsService.deleteUserDetails(userDetailsId);
		return "redirect:/viewUserDetails";
	}
}
