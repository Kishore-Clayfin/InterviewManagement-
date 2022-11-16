package com.cf.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cf.model.User;
import com.cf.service.IUserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class LoginController {

	@Autowired
	private IUserService iUserService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/home")
	public String home(HttpSession session)
	{
		User user=(User) session.getAttribute("loginDetails");
		
		if(user.getRole().equalsIgnoreCase("interviewer"))
		{
			return "interviewerHome";

		}
		else if(user.getRole().equalsIgnoreCase("hrHead"))
		{
			return "hrHead";

		}
		else
		{
			return "hrHome";

		}
		
	}
	@GetMapping("/homePage")
	public String hrlogin(HttpSession session,@RequestParam("name") String name , @RequestParam("password") String password) 
	{
//		User u= iUserService.findUsername(name);
////		System.out.println(u);
		log.info("send from login page"  +name);
		log.info("send from login page"  +password);
		
		   boolean login=iUserService.existsUserByUsernameAndPassword(name, password);
		   String role=null;
		   
		   if(login==true)
			{
			   User user= iUserService.findUsername(name);

			   
//			    System.out.println(session.getAttribute("details"));
			    role=user.getRole();
			    session.setAttribute("loginDetails", user);
			    session.setAttribute("interviewer", role);
//			    System.out.println(session.getAttribute("details"));
		    }
		  
		   if((login==true)&&(role.equals("hr"))) {
				log.info("going inside the hr home page");
				return"hrHome";
			}
			else if((login==true)&&(role.equals("interviewer")))
			{
				log.info("going inside the interviewer home page");

				return"interviewerHome";
			}
			else if((login==true)&&(role.equals("hrHead")))
			{
				log.info("going inside the hr home page");

				return"hrHead";
			}
			else 
			{
				log.info("Invalid useremail and password");
				
				return "redirect:/login2";	
			}
			
			
			
			}

	@GetMapping("/login2")
	public ModelAndView login2() {
		ModelAndView mv = new ModelAndView("login");
		mv.addObject("message1", "Invalid Credentials!!!! Please Enter correct mail and passward");

		return mv;

	}
	
	@GetMapping("/logout")
	public String logout( HttpSession session) 
	{
		session.invalidate();
		return "redirect:/login";
	}
}
