package com.cf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.cf.model.Domain;
import com.cf.model.DomainCategory;
import com.cf.model.User;
import com.cf.service.IDomainCategoryService;
import com.cf.service.IDomainService;

@Controller
public class DomainController 
{

	@Autowired
	private IDomainService iDomainService;
	
	@Autowired
	private IDomainCategoryService iDomainCategoryService;
	
	@GetMapping("/addDomain")
	public ModelAndView addDomain(HttpSession session,HttpServletResponse redirect) {
		
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
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<DomainCategory> domainCategory = iDomainCategoryService.viewDomainCategoryList();
		
		Domain domain = new Domain();
		ModelAndView mav = new ModelAndView("domainRegister");
		mav.addObject("domain",domain);
		mav.addObject("domainCategory",domainCategory);
		return mav;
	}
	
	@PostMapping("/saveDomain")
	public String saveDomain(@Valid @ModelAttribute Domain domain,BindingResult result, @RequestParam String category,HttpSession session,HttpServletResponse redirect) 
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
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<DomainCategory> arr=new ArrayList<DomainCategory>();
		category= category.toUpperCase();
		String[]  s=category.split(",");
		List list= Arrays.asList(s);
//		System.out.println(list);
		domain.setDomainCategory(list);
		for(Object str:list) {
			DomainCategory domCat=new DomainCategory();
			domCat.setDomSubCatName((String)str);
			DomainCategory dom= iDomainCategoryService.saveDomainCategory(domCat);
			arr.add( dom);
		}
		domain.setDomainCategory(arr);
		System.out.println(arr);
		
		

		if(result.hasErrors()) 
		{
			return "domainRegister";
			
		}
		
		
		iDomainService.saveDomain(domain);
		return "redirect:/viewDomains";
	}
	
	@GetMapping("/viewDomains")
	public ModelAndView getAllDomains(HttpSession session,HttpServletResponse redirect) {
		
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
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ModelAndView mav = new ModelAndView("domainList");
		mav.addObject("domain", iDomainService.viewDomainList());
		return mav;
	}
	
	@GetMapping("/showUpdateDomain")
	public ModelAndView showUpdateDomain(@RequestParam Integer domainId,HttpSession session,HttpServletResponse redirect) {
		
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
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<DomainCategory> domainCategory = iDomainCategoryService.viewDomainCategoryList();
		
		ModelAndView mav = new ModelAndView("domainRegister");
		Domain domain = iDomainService.updateDomain(domainId);
		mav.addObject("domain", domain);
		mav.addObject("domainCategory",domainCategory);
		return mav;
	}
	
	@GetMapping("/deleteDomain")
	public String deleteDomain(@RequestParam Integer domainId,HttpSession session,HttpServletResponse redirect) {
		
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
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		iDomainService.deleteDomain(domainId);
		return "redirect:/viewDomains";
	}
}
