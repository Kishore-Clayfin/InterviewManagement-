package com.cf.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cf.model.Candidate;
import com.cf.model.Schedule;
import com.cf.model.User;
import com.cf.repository.IScheduleDao;
import com.cf.service.ICandidateService;
import com.cf.service.IScheduleService;
import com.cf.service.IUserService;

@Controller
//@SessionAttributes("todos")
public class ScheduleController {

	@Autowired
	private ICandidateService icandidateService;
	@Autowired
	private IScheduleService ischeduleService;
	@Autowired
	private IUserService iUserService;
	

	@GetMapping("/addschedule")
	public ModelAndView addhr() {
		Schedule Schedule = new Schedule();
		List<Candidate> Candidate = icandidateService.viewCandidateList();
		List<User> user = iUserService.viewUserList();

		
		  List<User> list = user.stream().filter(c ->
		  c.getRole().equalsIgnoreCase("interviewer")|| c.getRole().equalsIgnoreCase("hrHead")) .collect(Collectors.toList());
		 

		ModelAndView mv = new ModelAndView("scheduleRegister");
		mv.addObject("schedule", Schedule);
		mv.addObject("candidate", Candidate);
		mv.addObject("interviewer", list);
		return mv;
	}

	@PostMapping("/saveschedule")
	public String saveschedule(@ModelAttribute Schedule schedule)//, Model model) 
	{
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
			System.out.println("u can't schedule the interview for the past date or time");
			
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
//					  model.addAttribute("errorMessage",
//							  "u can't schedule the interview at this time"); 
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
		 
		ischeduleService.saveSchedule(schedule);

		return "redirect:/viewschedules";
	}

	@GetMapping({ "/viewschedules" })
	public ModelAndView getAllschedules(HttpSession session) 
	{
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
	public ModelAndView showUpdateSchedule(@RequestParam Integer scheduleId) 
	{
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
	public String deleteSchedule(@RequestParam Integer scheduleId) {
		ischeduleService.deleteSchedule(scheduleId);
		return "redirect:/viewschedules";
	}

	@GetMapping("/giveFeedback")
	public ModelAndView getfeedback()// (@RequestParam Integer InterviewerId)
	{
		ModelAndView mav = new ModelAndView("CompletedList");

		List<Schedule> list = ischeduleService.viewScheduleList();

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
//		System.out.println(a);

		mav.addObject("schedule", a);

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
