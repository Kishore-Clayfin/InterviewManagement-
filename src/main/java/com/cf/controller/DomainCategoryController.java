package com.cf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cf.model.DomainCategory;
import com.cf.service.IDomainCategoryService;

@Controller
public class DomainCategoryController {

	@Autowired
	private IDomainCategoryService iDomainCategoryService;
	
	@GetMapping("/addDomainCategory")
	public ModelAndView addDomainCategory() {
		DomainCategory domainCategory = new DomainCategory();
		ModelAndView mav = new ModelAndView("domainCategoryRegister");
		mav.addObject("domainCategory",domainCategory);
		return mav;
	}
	
	@PostMapping("/saveDomainCategory")
	public String saveDomainCategory(@ModelAttribute DomainCategory domainCategory) {
		iDomainCategoryService.saveDomainCategory(domainCategory);
		return "redirect:/viewDomainCategories";
	}
	
	@GetMapping("/viewDomainCategories")
	public ModelAndView getAllDomainCategories() {
		ModelAndView mav = new ModelAndView("domainCategoryList");
		mav.addObject("domainCategory", iDomainCategoryService.viewDomainCategoryList());
		return mav;
	}
	
	@GetMapping("/showUpdateDomainCategory")
	public ModelAndView showUpdateDomainCategory(@RequestParam Integer domSubCatId) {
		ModelAndView mav = new ModelAndView("domainCategoryRegister");
		DomainCategory domainCategory = iDomainCategoryService.updateDomainCategory(domSubCatId);
		mav.addObject("domainCategory", domainCategory);
		return mav;
	}
	
	@GetMapping("/deleteDomainCategory")
	public String deleteDomainCategory(@RequestParam Integer domSubCatId) {
		iDomainCategoryService.deleteDomainCategory(domSubCatId);
		return "redirect:/viewDomainCategories";
	}
}
