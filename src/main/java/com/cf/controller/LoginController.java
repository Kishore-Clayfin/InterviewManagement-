package com.cf.controller;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cf.model.User;
import com.cf.service.IUserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class LoginController implements ErrorController {

	@Autowired
	private IUserService iUserService;

	public static User checkUser = null;

	@GetMapping({ "/login", "/" })
	public String login(HttpSession session, HttpServletRequest request) {

		User user = (User) session.getAttribute("loginDetails");
		if (user == null) {
			// comes here when session is invalid.
			return "login";
		} else {
			String url = (String) session.getAttribute("url");
			return url;
		}

	}

	@GetMapping("/home")
	public String home(HttpSession session) {
		User user = (User) session.getAttribute("loginDetails");

		if (user.getRole().equalsIgnoreCase("interviewer")) {
			String currentUrl = "interviewerHome";
			session.setAttribute("url", currentUrl);
			return currentUrl;

		} else if (user.getRole().equalsIgnoreCase("hrHead")) {
			String currentUrl = "hrHead";
			session.setAttribute("url", currentUrl);
			return currentUrl;

		} else {
			String currentUrl = "hrHome";
			session.setAttribute("url", currentUrl);
			return currentUrl;

		}

	}

	@GetMapping("/homePage")
	public String hrlogin(HttpSession session, @RequestParam("name") String name,
			@RequestParam("password") String password) {

		boolean login = iUserService.existsUserByUsernameAndPassword(name, password);
		String role = null;

		if (login == true) {
			checkUser = iUserService.findUsername(name);

			role = checkUser.getRole();
			session.setAttribute("loginDetails", checkUser);
			session.setAttribute("interviewer", role);
		}

		if ((login == true) && (role.equals("hr"))) {
			log.info("Logged in as HR");
			String currentUrl = "hrHome";
			session.setAttribute("url", currentUrl);
			return currentUrl;
		} else if ((login == true) && (role.equals("interviewer"))) {
			log.info("Logged in as INTERVIEWER");

			String currentUrl = "interviewerHome";
			session.setAttribute("url", currentUrl);
			return currentUrl;

		} else if ((login == true) && (role.equals("hrHead"))) {
			log.info("Logged in as HRHEAD");

			String currentUrl = "hrHead";
			session.setAttribute("url", currentUrl);
			return currentUrl;
		} else {
			log.info("Invalid CREDENTIALS");
			String url = "redirect:/login2";
			return url;
		}

	}

	@GetMapping("/login2")
	public ModelAndView login2() {
		ModelAndView mv = new ModelAndView("login");
		mv.addObject("message1", "Invalid Credentials!!!! Please Enter correct mail-id and passward");

		return mv;

	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

	@RequestMapping("/error")
	public String error() {

		return "redirect:/login";
	}
}
