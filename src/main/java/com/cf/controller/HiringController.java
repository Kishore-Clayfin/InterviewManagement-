package com.cf.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
import com.cf.model.Hiring;
import com.cf.model.User;
import com.cf.model.UserDetails;
import com.cf.repository.IHiringDao;
import com.cf.service.IDomainService;
import com.cf.service.IHiringService;
import com.cf.service.IUserService;

@Controller
public class HiringController {

	@Autowired
	private IHiringService iHiringService;

	@Autowired
	private IDomainService iDomainService;

	@Autowired
	private IUserService iUserService;

	@Autowired
	private IHiringDao iHiringDao;

	@GetMapping("/addHiring")
	public ModelAndView addHiring(HttpSession session, HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Domain> domain = iDomainService.viewDomainList();

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<User> user = iUserService.viewUserList();

		List<User> list = user.stream().filter(c -> c.getRole().equalsIgnoreCase("hr")).collect(Collectors.toList());

		Hiring hiring = new Hiring();

		ModelAndView mav = new ModelAndView("hiringRegister");
		mav.addObject("hiring", hiring);
		mav.addObject("domain", domain);
		mav.addObject("user", list);
		return mav;
	}

	@PostMapping("/saveHiring")
	public String saveHiring(@ModelAttribute Hiring hiring, HttpSession session, HttpServletResponse redirect) {

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

		iHiringService.saveHiring(hiring);
		return "redirect:/viewHirings";
	}

	@GetMapping("/viewHirings")
	public ModelAndView getAllHirings(HttpSession session, HttpServletResponse redirect) {

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

		System.out.println("entered view List");
		ModelAndView mav = new ModelAndView("hiringList");
		List<Hiring> hiring2 = iHiringService.viewHiringList();
		System.out.println(hiring2);
		System.out.println(iHiringService.viewHiringList());
		mav.addObject("hiring", hiring2);// iHiringService.viewHiringList()
		return mav;
	}

	@GetMapping("/showUpdateHiring")
	public ModelAndView showUpdateHiring(@RequestParam Integer hiringId, HttpSession session,
			HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Domain> domain = iDomainService.viewDomainList();
		List<User> user = iUserService.viewUserList();

		List<User> list = user.stream().filter(c -> c.getRole().equalsIgnoreCase("hr")).collect(Collectors.toList());

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		ModelAndView mav = new ModelAndView("hiringRegister");
		Hiring hiring = iHiringService.updateHiring(hiringId);
		mav.addObject("hiring", hiring);
		mav.addObject("domain", domain);
		mav.addObject("user", list);
		return mav;
	}

	@GetMapping("/deleteHiring")
	public String deleteHiring(@RequestParam Integer hiringId, HttpSession session, HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User user = (User) session.getAttribute("loginDetails");
		if (!user.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		iHiringService.deleteHiring(hiringId);
		return "redirect:/viewHirings";
	}
}
