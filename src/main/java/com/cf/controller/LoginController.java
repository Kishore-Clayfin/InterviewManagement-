package com.cf.controller;

import javax.servlet.http.HttpServletRequest;
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

//	@GetMapping({"/login","/"})
//	public String login() {
//		return "login";
//	}
//	@GetMapping({"/login","/"})
//	public String login(HttpSession session) {
//		//HttpSession session = request.getSession();
//		String url1;
//		url1=(String)session.getAttribute("url");
//		//session.isNew()||
//		if (url1==null) {
//
//		return "login";
//		} else {
//		
//		System.out.println(url1);
//		return url1;
//		}
//		
//	}
	
	@GetMapping({"/login","/"})
	public String login(HttpSession session,HttpServletRequest request) {
		//HttpSession session = request.getSession();
		HttpSession session1 = request.getSession(false);
		if (session1 == null || !request.isRequestedSessionIdValid()) {
		    //comes here when session is invalid. 
			return "login";
		} else {
			String url =(String)session.getAttribute("url");
			System.out.println(url);
		    return url;
		}
		
	}
	@GetMapping("/home")
	public String home(HttpSession session)
	{
		User user=(User) session.getAttribute("loginDetails");
		
		if(user.getRole().equalsIgnoreCase("interviewer"))
		{
			String currentUrl="interviewerHome";
			session.setAttribute("url", currentUrl);
			return currentUrl;

		}
		else if(user.getRole().equalsIgnoreCase("hrHead"))
		{
			String currentUrl="hrHead";
			session.setAttribute("url", currentUrl);
			return currentUrl;

		}
		else
		{
			String currentUrl="hrHome";
			session.setAttribute("url", currentUrl);
			return currentUrl;

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
				String currentUrl="hrHome";
				session.setAttribute("url", currentUrl);
				return currentUrl;
			}
			else if((login==true)&&(role.equals("interviewer")))
			{
				log.info("going inside the interviewer home page");
  
				String currentUrl="interviewerHome";
				session.setAttribute("url", currentUrl);
				return currentUrl;
			
			}
			else if((login==true)&&(role.equals("hrHead")))
			{
				log.info("going inside the hr home page");

				String currentUrl="hrHead";
				session.setAttribute("url", currentUrl);
				return currentUrl;
			}
			else 
			{
				log.info("Invalid useremail and password");
				String url="redirect:/login2";
				return url;	
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
