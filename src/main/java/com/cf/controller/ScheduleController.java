package com.cf.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cf.model.Candidate;
import com.cf.model.Schedule;
import com.cf.model.User;
import com.cf.repository.ICandidateDao;
import com.cf.service.ICandidateService;
import com.cf.service.IFeedbackService;
import com.cf.service.IScheduleService;
import com.cf.service.IUserService;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Controller
//@SessionAttributes("todos")
public class ScheduleController {
	
	@Autowired
	private ICandidateDao candidateDao;

	@Autowired
	private IFeedbackService iFeedbackService;
	@Autowired
	private ICandidateService icandidateService;
	@Autowired
	private IScheduleService ischeduleService;
	@Autowired
	private IUserService iUserService;
	
	@PostMapping("/ajaxPost")
	public String ajaxPostMethod(@RequestBody List<Integer> list1, HttpSession session)
	{
		session.setAttribute("ajax", list1);
	//	System.out.println(list1);
	
		return "redirect:/addschedule";
	}

	
	@GetMapping("/addschedule")
	public ModelAndView addhr( HttpSession session ) {
		List li1=(List) session.getAttribute("ajax");
		//	System.out.println(list1);
		List<Candidate> li=candidateDao.findAllById(li1);
	//	System.out.println(li);
		
		Schedule Schedule = new Schedule();
		List<Candidate> Candidate = icandidateService.viewCandidateList();
		List<User> user = iUserService.viewUserList();

		
  List<User> list = user.stream().filter(c ->
  c.getRole().equalsIgnoreCase("interviewer")|| c.getRole().equalsIgnoreCase("hrHead")) .collect(Collectors.toList());
 

		ModelAndView mv = new ModelAndView("scheduleRegister");
		mv.addObject("schedule", Schedule);
//		mv.addObject("candidate", Candidate);
		mv.addObject("candidate", li);
		mv.addObject("interviewer", list);
		return mv;
	}

	@GetMapping("/scheduleInterview") 
	public ModelAndView addhr() {
		Schedule Schedule = new Schedule();
		List<Candidate> Candidate = icandidateService.viewCandidateList();
		List<User> user = iUserService.viewUserList();

		List<User> list = user.stream().filter(c -> c.getRole().equalsIgnoreCase("interviewer")|| c.getRole().equalsIgnoreCase("hrHead"))
				.collect(Collectors.toList());

		ModelAndView mv = new ModelAndView("scheduleRegister");
		mv.addObject("schedule", Schedule);
		mv.addObject("candidate", Candidate);
		mv.addObject("interviewer", list);
		return mv;
	}
	
	
	@GetMapping("/addschedule2")
	public ModelAndView addschedule2(@RequestParam Integer candidateId,HttpSession session,HttpServletResponse redirect) 
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


		Schedule Schedule = new Schedule();
		
	
		
		
		
		Candidate candidate = icandidateService.findResumeCandidate(candidateId);
		List<User> user = iUserService.viewUserList();

		
		  List<User> list = user.stream().filter(c ->
		   c.getRole().equalsIgnoreCase("hrHead")) .collect(Collectors.toList());
		 

		ModelAndView mv = new ModelAndView("hrRoundScheduleRegistration");
		mv.addObject("schedule", Schedule);
		
		mv.addObject("candidateId", candidateId);
		mv.addObject("candidate", candidate);
		mv.addObject("interviewer", list);
		return mv;
	}
	
	
	

	@PostMapping("/saveschedule")
	public String saveschedule(@Valid @ModelAttribute Schedule schedule,BindingResult result, Model model,RedirectAttributes ra,HttpSession session,HttpServletResponse redirect) 
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


		List<Candidate> candidateList=schedule.getCandidate();
		
		
		
		
		
		List<Schedule> schedules = ischeduleService.viewScheduleList();
		int id=schedule.getUser().getUserId();
		List<Schedule> interviewer=schedules.stream().filter(x -> x.getUser().getUserId()==id).collect(Collectors.toList());
		
		/********      Timing Validation        **********/
		LocalDate dateNow=LocalDate.now();
		LocalTime time=LocalTime.now();
		LocalTime timeNow=time.truncatedTo(ChronoUnit.MINUTES);
		
		
		String s=schedule.getScheduleTime();
		String stTime=null;
		LocalTime inputTime= LocalTime.parse(s);
		
		int duration=0;
		LocalTime startTime= null;
		LocalTime endTime=null;
		
		/**********     ------------  ******************/
		if(schedule.getScheduleDate().isBefore(dateNow)||((schedule.getScheduleDate().isEqual(dateNow))&&(inputTime.isBefore(timeNow)) ))
		{
			ra.addAttribute("errorMessage","u can't schedule the interview for the past date or time");
			
			return "redirect:/addschedule";
		}
		
		
//		for (Schedule schedule2 : trainer)
//		{
//			System.out.println(schedule2);
//		}
//		schedule.getUser().getUserId();
//		 schedules.stream().filter(schedule.getIntreviewTime())
//		 Model mv= new Model;

//		schedule.getUser().getRole().equals("interviewer");
		
		  if(interviewer!=null)
		  {
			  for (Schedule sc : interviewer) 
			  { 
				  duration=sc.getDuration();
				  stTime= sc.getScheduleTime();
				  startTime= LocalTime.parse(stTime);
				  endTime=startTime.plusMinutes(duration);
				  
				  
				  
				  if(  ( !((inputTime.isAfter(startTime)&&inputTime.isAfter(endTime))||((inputTime.isBefore(startTime)&&inputTime.isBefore(endTime))))  )  && sc.getScheduleDate().equals(schedule.getScheduleDate())  ) 
				  {
					  
					  System.out.println("u can't schedule the interview at this time");
					  ra.addAttribute("errorMessage",
							  "u can't schedule the interview at this time"); 
					  return "redirect:/addschedule"; 
				  } 
//				  else if(sc.getScheduleDate().equals(schedule.getScheduleDate()) && sc.getScheduleTime().equals(schedule.getScheduleTime()) ) 
//				  {
//					  System.out.println("u can't schedule the interview at this time");
////					  model.addAttribute("errorMessage","u can't schedule the interview at this time");  
//					  return  "redirect:/addschedule"; 
//							  
//				  }
			  
			  }
		  }
		  
		  if (schedule.getInterviewType().equals("Walk-in")) schedule.setMeetingLink( "hi " +
		  schedule.getCandidate().get(0).getCandidateName() + " your " +
		  "interview is scheduled on " + schedule.getScheduleDate() + " at " +
		  schedule.getScheduleTime());
		  
//		  System.out.println(schedule);
		 if(result.hasErrors())
		 {
			 return "scheduleRegister";
		 }
		int size=schedule.getCandidate().size();
	//	ArrayList list=null;//new ArrayList();
		if(size==1)
		{
			
			System.out.println("entering if");
			
			ischeduleService.saveSchedule(schedule);
		}
//		for (int i=0;i<size;i++)
//		{
//			list.add(candidateList.get(i));
//			System.out.println(list);
//			
//			Schedule schedule1=new Schedule();
//			schedule1.setCandidate(list);
//			schedule1.setDuration(schedule.getDuration());
//			schedule1.setInterviewType(schedule.getInterviewType());
//			schedule1.setMeetingLink(schedule.getMeetingLink());
//			schedule1.setScheduleDate(schedule.getScheduleDate());
//			schedule1.setScheduleId(null);
//			schedule1.setScheduleTime(schedule.getScheduleTime());
//			schedule1.setUser(schedule.getUser());
//			
////			System.out.println(list);
////			schedule.setCandidate(list);
////			System.out.println(schedule1.getScheduleId());
//			Schedule sch= ischeduleService.saveSchedule(schedule1);
////			System.out.println("Schedule => "+sch);
//			list.clear();
//		}
		else
		{
			int tempDuration=schedule.getDuration();
			String temp=schedule.getScheduleTime();
			LocalTime tempTime= LocalTime.parse(temp);
//			Schedule schedule1=schedule;
//			List<Schedule> li=new ArrayList<Schedule>();
//			System.out.println("entering else");
			for (int i=0;i<size;i++)
			{
			//	list=new ArrayList()
				ArrayList list=new ArrayList();
				Schedule schedule1=new Schedule();
//				schedule1.setCandidate(list);
				schedule1.setDuration(schedule.getDuration());
				schedule1.setInterviewType(schedule.getInterviewType());
				schedule1.setMeetingLink(schedule.getMeetingLink());
				schedule1.setScheduleDate(schedule.getScheduleDate());
				//schedule1.setScheduleId(null);
				schedule1.setScheduleTime(tempTime.toString());
				schedule1.setUser(schedule.getUser());
				
				list.add(candidateList.get(i));
				//System.out.println(list);
				schedule1.setCandidate(list);
			//	System.out.println(schedule1);
//				li.add(schedule1);
//				System.out.println(li);
				Schedule sch= ischeduleService.saveSchedule(schedule1);
				tempTime=tempTime.plusMinutes(tempDuration);
				//System.out.println(schedule1.getCandidate());
//				System.out.println("Schedule => "+sch);
				//list.clear();
			}
			
			
			
			
			
			
			
			
		}

		return "redirect:/viewschedules";
	}
	
	
	
	
	
	@PostMapping("/saveschedule2")
	public String saveschedule2(@Valid @ModelAttribute Schedule schedule,BindingResult result, Model model,@RequestParam Integer candidateId,RedirectAttributes ra,HttpSession session,HttpServletResponse redirect) 
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


		List<Candidate> candidateList=new ArrayList<Candidate>();
		
		
//		log.info("list"+candidateList);
		Candidate candidate=icandidateService.updateCandidate(candidateId);
		
		
		candidateList.add(candidate);
		log.info("list"+candidateList);
		
		
		List<Schedule> schedules = ischeduleService.viewScheduleList();
		int id=schedule.getUser().getUserId();
		List<Schedule> interviewer=schedules.stream().filter(x -> x.getUser().getUserId()==id).collect(Collectors.toList());
		
		/********      Timing Validation        **********/
		LocalDate dateNow=LocalDate.now();
		LocalTime time=LocalTime.now();
		LocalTime timeNow=time.truncatedTo(ChronoUnit.MINUTES);
		
		
		String s=schedule.getScheduleTime();
		String stTime=null;
		LocalTime inputTime= LocalTime.parse(s);
		
		int duration=0;
		LocalTime startTime= null;
		LocalTime endTime=null;
		
		/**********     ------------  ******************/
		if(schedule.getScheduleDate().isBefore(dateNow)||((schedule.getScheduleDate().isEqual(dateNow))&&(inputTime.isBefore(timeNow)) ))
		{
			ra.addAttribute("errorMessage","u can't schedule the interview for the past date or time");
			
			return "redirect:/addschedule2";
		}
		
		
//		for (Schedule schedule2 : trainer)
//		{
//			System.out.println(schedule2);
//		}
//		schedule.getUser().getUserId();
//		 schedules.stream().filter(schedule.getIntreviewTime())
//		 Model mv= new Model;

//		schedule.getUser().getRole().equals("interviewer");
		
		  if(interviewer!=null)
		  {
			  for (Schedule sc : interviewer) 
			  { 
				  duration=sc.getDuration();
				  stTime= sc.getScheduleTime();
				  startTime= LocalTime.parse(stTime);
				  endTime=startTime.plusMinutes(duration);
				  
				  
				  
				  if(  ( !((inputTime.isAfter(startTime)&&inputTime.isAfter(endTime))||((inputTime.isBefore(startTime)&&inputTime.isBefore(endTime))))  )  && sc.getScheduleDate().equals(schedule.getScheduleDate())  ) 
				  {
					  
					  System.out.println("u can't schedule the interview at this time");
					  ra.addAttribute("errorMessage",
							  "u can't schedule the interview at this time"); 
					  return "redirect:/addschedule2"; 
				  } 
//				  else if(sc.getScheduleDate().equals(schedule.getScheduleDate()) && sc.getScheduleTime().equals(schedule.getScheduleTime()) ) 
//				  {
//					  System.out.println("u can't schedule the interview at this time");
////					  model.addAttribute("errorMessage","u can't schedule the interview at this time");  
//					  return  "redirect:/addschedule"; 
//							  
//				  }
			  
			  }
		  }
		  
		  if (schedule.getInterviewType().equals("Walk-in")) schedule.setMeetingLink( "hi " +
		  schedule.getCandidate().get(0).getCandidateName() + " your " +
		  "interview is scheduled on " + schedule.getScheduleDate() + " at " +
		  schedule.getScheduleTime());
		  
//		  System.out.println(schedule);
		 if(result.hasErrors())
		 {
			 return "hrRoundScheduleRegister";
		 }
		
		
		if(candidateId!=null)
		{
			schedule.setCandidate(candidateList);
			ischeduleService.saveSchedule(schedule);
			
		}
		
		
		
		
//		for (int i=0;i<size;i++)
//		{
//			list.add(candidateList.get(i));
//			System.out.println(list);
//			
//			Schedule schedule1=new Schedule();
//			schedule1.setCandidate(list);
//			schedule1.setDuration(schedule.getDuration());
//			schedule1.setInterviewType(schedule.getInterviewType());
//			schedule1.setMeetingLink(schedule.getMeetingLink());
//			schedule1.setScheduleDate(schedule.getScheduleDate());
//			schedule1.setScheduleId(null);
//			schedule1.setScheduleTime(schedule.getScheduleTime());
//			schedule1.setUser(schedule.getUser());
//			
////			System.out.println(list);
////			schedule.setCandidate(list);
////			System.out.println(schedule1.getScheduleId());
//			Schedule sch= ischeduleService.saveSchedule(schedule1);
////			System.out.println("Schedule => "+sch);
//			list.clear();
//		}
		else
		{
			int tempDuration=schedule.getDuration();
			String temp=schedule.getScheduleTime();
			LocalTime tempTime= LocalTime.parse(temp);
//			Schedule schedule1=schedule;
//			List<Schedule> li=new ArrayList<Schedule>();
//			System.out.println("entering else");
			
			
			
			
			
			
			
			
			
		}

		return "redirect:/showInterviewCompleted";
	}
	
	
	
	
	
	
	
	

	

	@GetMapping({ "/viewschedules" })
	public ModelAndView getAllschedules(HttpSession session,HttpServletResponse redirect) 
	{
		User checkUser=(User)session.getAttribute("loginDetails");
		if(!(checkUser.getRole().equals("hr")||checkUser.getRole().equals("interviewer")||checkUser.getRole().equals("hrHead")))
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		// @ModelAttribute("todos") List<Candidate> li) {
//		System.out.println(session.getAttribute("data"));
//		ModelAndView mav = new ModelAndView("scheduleList");
//		mav.addObject("schedule", ischeduleService.viewScheduleList());
//		return mav;
		String role= (String) session.getAttribute("interviewer");
		User user2= new User();
		user2.setRole(role);
		
		
		User user= (User) session.getAttribute("loginDetails");
		List<Schedule> scheduleList= ischeduleService.viewScheduleList();
		if(user.getRole().equalsIgnoreCase("hr"))
		{
			ModelAndView mav = new ModelAndView("scheduleList");
			mav.addObject("schedule", scheduleList);
			mav.addObject("role", user2);
			return mav;
		}
		else
		{
			List<Schedule> sch= scheduleList.stream().filter(s -> s.getUser().getUserId()==user.getUserId()).collect(Collectors.toList());
			ModelAndView mav = new ModelAndView("scheduleList");
			mav.addObject("schedule", sch);
			mav.addObject("role", user2);
			return mav;
		}
	}
//
	@GetMapping("/showUpdateSchedule")
	public ModelAndView showUpdateSchedule(@RequestParam Integer scheduleId,HttpSession session,HttpServletResponse redirect) 
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


//		Schedule s =new Schedule();
//		User user1=new User();
		
		Schedule schedule = ischeduleService.updateSchedule(scheduleId);
//		int userId=schedule.getUser().getUserId();
//		user1.setUserId(userId);
//		schedule.setUser(user1);
//		schedule.setCandidate(null);
		List<Candidate> Candidate = icandidateService.viewCandidateList();
		List<User> user = iUserService.viewUserList();

		List<User> list = user.stream().filter(c -> c.getRole().equalsIgnoreCase("interviewer"))
				.collect(Collectors.toList());

		ModelAndView mv = new ModelAndView("scheduleRegister");
		mv.addObject("schedule", schedule);
		mv.addObject("candidate", Candidate);
		mv.addObject("interviewer", list);
		return mv;

	}
	
	

	@GetMapping("/deleteSchedule")
	public String deleteSchedule(@RequestParam Integer scheduleId,HttpSession session,HttpServletResponse redirect) {
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


		ischeduleService.deleteSchedule(scheduleId);
		return "redirect:/viewschedules";
	}

	@GetMapping("/showInterviewCompleted")
	public ModelAndView getfeedback(HttpSession session,HttpServletResponse redirect)// (@RequestParam Integer InterviewerId)
	{
		User checkUser=(User)session.getAttribute("loginDetails");
		if(!(checkUser.getRole().equals("hr")||checkUser.getRole().equals("interviewer")||checkUser.getRole().equals("hrHead")))
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		ModelAndView mav = new ModelAndView("InterviewCompletedList");
		mav.addObject("feedback", iFeedbackService.viewFeedbackList());
		return mav;
		
	}
	
	
	
	
	
	
	
	@GetMapping("/giveFeedback")
	public ModelAndView givefeedback(HttpSession session,HttpServletResponse redirect)// (@RequestParam Integer InterviewerId)
	{
		User checkUser=(User)session.getAttribute("loginDetails");
		if(!(checkUser.getRole().equals("hr")||checkUser.getRole().equals("interviewer")||checkUser.getRole().equals("hrHead")))
		{
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		User user= (User) session.getAttribute("loginDetails");
		ModelAndView mav = new ModelAndView("CompletedList");

		List<Schedule> scheduleList = ischeduleService.viewScheduleList();
		List<Schedule> list=null;
		if(user.getRole().equals("hr"))
		{
			 list = ischeduleService.viewScheduleList();
		}
		else
		{
			 list= scheduleList.stream().filter(s -> s.getUser().getUserId()==user.getUserId()).collect(Collectors.toList());
		}
//		System.out.println(list);
		ArrayList<Schedule> a = new ArrayList<Schedule>();

		for (Schedule schedules : list)
		{
			for (int i = 0; i < schedules.getCandidate().size(); i++) 
			{
				if(schedules.getCandidate().get(i).getStatus()!=null)
				{
					if (schedules.getCandidate().get(i).getStatus().equalsIgnoreCase("COMPLETED"))// &&
						// schedules.getInterviewer().getInterviewerId()==InterviewerId)
						{
							a.add(schedules);
						}
				}
			}
		}
		

//		if(user.getRole().equalsIgnoreCase("hr"))
//		{
//			mav.addObject("schedule", a);
//			return mav;
//		}
//		else
//		{
//			List<Schedule> sch= a.stream().filter(s -> s.getUser().getUserId()==user.getUserId()).collect(Collectors.toList());
//			
//			mav.addObject("role", sch);
//			return mav;
//		}
//		System.out.println(a);

		mav.addObject("schedule", a);
		mav.addObject("role",checkUser.getRole());

//		List<Candidate> can = IcandidateService.viewCandidateList();
//		
////		System.out.println(can);
//		
//		ArrayList<Candidate> a=new ArrayList<Candidate>();
//	
//		for (Candidate candid : can)
//		{
//			
////			System.out.println(candid);
//			if(candid.getStatus()!=null)
//			{
////				if(candid.getStatus().equals("COMPLETED"))
////				{
//					a.add(candid);
////				}
//			}
//		}
////		System.out.println(list);
//		
//		mav.addObject("Candidate", a);

		return mav;
	}
	
	
	

}
