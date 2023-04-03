package com.cf.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
//@GetMapping("/clear")
//public void clear() {
//	
//}
//	@GetMapping({"/login","/" })
//	public String login(HttpSession session, HttpServletRequest request) {
//
////		User user = (User) session.getAttribute("loginDetails");
////		if (user == null) {
////			// comes here when session is invalid.
////			return "login";
////		} else {
////			String url = (String) session.getAttribute("url");
////			return url;
////		}
//		return "login";
//	}
	@GetMapping("/login")
	public void loginMethod() {
		
	}

//	@GetMapping("/securityHome")
//	public String redirectSpring() {
//		System.out.println("login success url");
//		
//		return "interviewerHome";
//	}
	@GetMapping("/home")
	public String home(HttpSession session) {
		User user = (User) session.getAttribute("loginDetails");
		System.out.println("Login User Object###################"+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//		if (user.getRole().equalsIgnoreCase("interviewer")) {
//			String currentUrl = "interviewerHome";
//			session.setAttribute("url", currentUrl);
//			return currentUrl;
//
//		} else if (user.getRole().equalsIgnoreCase("hrHead")) {
//			String currentUrl = "hrHead";
//			session.setAttribute("url", currentUrl);
//			return currentUrl;
//
//		} else {
//			String currentUrl = "hrHome";
//			session.setAttribute("url", currentUrl);
//			return currentUrl;
//
//		}
		return null;

	}

	@GetMapping("/")
	public String hrlogin(){//HttpSession session, @RequestParam("name") String name,
//			@RequestParam("password") String password) {
		
		System.err.println(iUserService.getAuthentication());
//		System.out.println("Login User Object###################"+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//		boolean login = iUserService.existsUserByUsernameAndPassword(name, password);
//		String role = null;
////
//		if (login == true) {
//			checkUser = iUserService.findUsername(name);
//
//			role = checkUser.getRole();
//			session.setAttribute("loginDetails", checkUser);
//			session.setAttribute("interviewer", role);
//		}
//
//		if ((login == true) && (role.equals("hr"))) {
//			log.info("Logged in as HR");
//			String currentUrl = "hrHome";
//			session.setAttribute("url", currentUrl);
////			return currentUrl;
//		} else if ((login == true) && (role.equals("interviewer"))) {
//			log.info("Logged in as INTERVIEWER");
//
//			String currentUrl = "interviewerHome";
//			session.setAttribute("url", currentUrl);
////			return currentUrl;
//
//		} else if ((login == true) && (role.equals("hrHead"))) {
//			log.info("Logged in as HRHEAD");
//
//			String currentUrl = "hrHead";
//			session.setAttribute("url", currentUrl);
////			return currentUrl;
//		} else {
//			log.info("Invalid CREDENTIALS");
//			String url = "redirect:/login2";
////			return url;
//		}
		
		return "hrHome";

	}

	@GetMapping("/login2")
	public ModelAndView login2() {
		ModelAndView mv = new ModelAndView("login");
		mv.addObject("message1", "Invalid Credentials!!!! Please Enter correct mail-id and passward");

		return mv;

	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		System.out.println("entered logout");
		checkUser = null;
		session.invalidate();
		System.out.println("before invalidate");
//		SecurityContextHolder.clearContext();
//		System.out.println("after invalidate###################"+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return "redirect:/login";
	}

	@RequestMapping("/error")
	public String error() {

		return "redirect:/login";
	}
}
