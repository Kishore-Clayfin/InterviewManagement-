package com.cf.controller;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

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

import com.cf.model.Candidate;
import com.cf.model.User;
import com.cf.model.UserDetails;
import com.cf.service.ICandidateService;
import com.cf.service.IUserDetailsService;
import com.cf.service.IUserService;

@Controller
public class UserController {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private IUserDetailsService iUserDetailsService;

	@Autowired
	private ICandidateService iCandidateService;

	@GetMapping("/addUser")
	public ModelAndView addUser(HttpSession session, HttpServletResponse redirect) {

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
				e.printStackTrace();
			}
		}

		User errUser = (User) session.getAttribute("user");

		User user = new User();
		ModelAndView mav = new ModelAndView("userRegister");

		if (errUser != null) {

			mav.addObject("user", errUser);

		} else {
			mav.addObject("user", user);
		}

		return mav;
	}

	@PostMapping("/saveUser")
	public String saveUser(@Valid @ModelAttribute User user, BindingResult result, HttpSession session,
			HttpServletResponse redirect, RedirectAttributes attributes) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String mail = user.getEmail();

		String mob;
		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		if (result.hasErrors()) {
			return "userRegister";

		}

		if (iUserService.existsUserByEmail(mail)) {

			attributes.addAttribute("mailError", "Mail is already registerd please enter another mail!!!");
			session.setAttribute("user", user);

			return "redirect:/addUser";

		}

		mob = user.getUserDetails().getMobileNumber().toString();

		boolean mobv = Pattern.matches("^[9876]\\d{9}$", mob);
		if (!mobv == true) {
			attributes.addAttribute("numberError", "Please enter a valid mobile number");

			session.setAttribute("user", user);

			return "redirect:/addUser";
		}

		iUserService.saveUser(user);

		return "redirect:/viewUsers";
	}

	@GetMapping("/viewUsers")
	public ModelAndView getAllUsers(HttpSession session, HttpServletResponse redirect) {

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ModelAndView mav = new ModelAndView("userList");
		mav.addObject("user", iUserService.viewUserList());
		return mav;
	}

	@GetMapping("/showUpdateUser")
	public ModelAndView showUpdateUser(@RequestParam Integer userId, HttpSession session,
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
		ModelAndView mav = new ModelAndView("userRegister");
		User user = iUserService.updateUser(userId);
		mav.addObject("user", user);
		return mav;
	}

	@GetMapping("/deleteUser")
	public String deleteUser(@RequestParam Integer userId, HttpSession session, HttpServletResponse redirect) {

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		iUserService.deleteUser(userId);
		return "redirect:/viewUsers";
	}

}
