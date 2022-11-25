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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cf.model.DomainCategory;
import com.cf.model.User;
import com.cf.service.IDomainCategoryService;

@Controller
public class DomainCategoryController {

	@Autowired
	private IDomainCategoryService iDomainCategoryService;
	
	@GetMapping("/addDomainCategory")
	public ModelAndView addDomainCategory(HttpSession session,HttpServletResponse redirect)
	{
		
		if(LoginController.checkUser==null)
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		User user=(User)session.getAttribute("loginDetails");
		if(!user.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");//("http://localhost:9091/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DomainCategory domainCategory = new DomainCategory();
		ModelAndView mav = new ModelAndView("domainCategoryRegister");
		mav.addObject("domainCategory",domainCategory);
		return mav;
	}
	
	@PostMapping("/saveDomainCategory")
	public String saveDomainCategory(@Valid @ModelAttribute DomainCategory domainCategory,BindingResult result,HttpSession session,HttpServletResponse redirect ) 
	{
		
		if(LoginController.checkUser==null)
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		User user=(User)session.getAttribute("loginDetails");
		if(!user.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");//("http://localhost:9091/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		if(result.hasErrors())
		{
			return "domainCategoryRegister";
		}
		iDomainCategoryService.saveDomainCategory(domainCategory);
		return "redirect:/viewDomainCategories";
	}
	
	@GetMapping("/viewDomainCategories")
	public ModelAndView getAllDomainCategories(HttpSession session,HttpServletResponse redirect) {
		
		if(LoginController.checkUser==null)
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		User user=(User)session.getAttribute("loginDetails");
		if(!user.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");//("http://localhost:9091/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		ModelAndView mav = new ModelAndView("domainCategoryList");
		mav.addObject("domainCategory", iDomainCategoryService.viewDomainCategoryList());
		return mav;
	}
	
	@GetMapping("/showUpdateDomainCategory")
	public ModelAndView showUpdateDomainCategory(@RequestParam Integer domSubCatId,HttpSession session,HttpServletResponse redirect) {
		
		if(LoginController.checkUser==null)
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		User user=(User)session.getAttribute("loginDetails");
		if(!user.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");//("http://localhost:9091/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		ModelAndView mav = new ModelAndView("domainCategoryRegister");
		DomainCategory domainCategory = iDomainCategoryService.updateDomainCategory(domSubCatId);
		mav.addObject("domainCategory", domainCategory);
		return mav;
	}
	
	@GetMapping("/deleteDomainCategory")
	public String deleteDomainCategory(@RequestParam Integer domSubCatId,HttpSession session,HttpServletResponse redirect) {
		
		if(LoginController.checkUser==null)
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		User user=(User)session.getAttribute("loginDetails");
		if(!user.getRole().equals("hr"))
		{
			try {
				redirect.sendRedirect("/login");//("http://localhost:9091/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		iDomainCategoryService.deleteDomainCategory(domSubCatId);
		return "redirect:/viewDomainCategories";
	}
}
