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
	public void ajaxPostMethod(@RequestBody List<Integer> list1, HttpSession session, HttpServletResponse redirect) {
		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		session.setAttribute("ajax", list1);

	}

	@GetMapping("/addschedule")
	public ModelAndView addhr(HttpSession session, HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List li1 = (List) session.getAttribute("ajax");
		List<Candidate> li = candidateDao.findAllById(li1);

		Schedule Schedule = new Schedule();
		List<Candidate> Candidate = icandidateService.viewCandidateList();
		List<User> user = iUserService.viewUserList();

		List<User> list = user.stream()
				.filter(c -> c.getRole().equalsIgnoreCase("interviewer") || c.getRole().equalsIgnoreCase("hrHead"))
				.collect(Collectors.toList());

		ModelAndView mv = new ModelAndView("scheduleRegister");
		mv.addObject("schedule", Schedule);
		mv.addObject("candidate", li);
		mv.addObject("interviewer", list);
		return mv;
	}

	@GetMapping("/scheduleInterview")
	public ModelAndView addhr(HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Schedule Schedule = new Schedule();
		List<Candidate> Candidate = icandidateService.viewCandidateList();
		List<User> user = iUserService.viewUserList();

		List<User> list = user.stream()
				.filter(c -> c.getRole().equalsIgnoreCase("interviewer") || c.getRole().equalsIgnoreCase("hrHead"))
				.collect(Collectors.toList());

		ModelAndView mv = new ModelAndView("scheduleRegister");
		mv.addObject("schedule", Schedule);
		mv.addObject("candidate", Candidate);
		mv.addObject("interviewer", list);
		return mv;
	}

	@GetMapping("/addschedule2")
	public ModelAndView addschedule2(@RequestParam Integer candidateId, @RequestParam String status,HttpSession session,
			HttpServletResponse redirect) {
System.out.println("Checkuu"+status);
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

		Schedule Schedule = new Schedule();

		Candidate candidate = icandidateService.findResumeCandidate(candidateId);
		List<User> user = iUserService.viewUserList();
		List<Candidate> listOfCandi=new ArrayList<>();
		listOfCandi.add(candidate);
		Schedule.setCandidate(listOfCandi);
		List<User> list = user.stream()
				.filter(c -> c.getRole().equalsIgnoreCase("hrHead") || c.getRole().equalsIgnoreCase("interviewer"))
				.collect(Collectors.toList());

		ModelAndView mv = new ModelAndView("hrRoundScheduleRegistration");
		mv.addObject("schedule", Schedule);

		mv.addObject("candidateId", candidateId);
		mv.addObject("candidate", candidate);
		mv.addObject("interviewer", list);
		mv.addObject("status",status);
		return mv;
	}

	@PostMapping("/saveschedule")
	public String saveschedule(@Valid @ModelAttribute Schedule schedule, BindingResult result, Model model,
			RedirectAttributes ra, HttpSession session, HttpServletResponse redirect) {

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
		List<Candidate> candidateList = schedule.getCandidate();
		List<Schedule> schedules = ischeduleService.viewScheduleList();
		int id = schedule.getUser().getUserId();
		List<Schedule> interviewer = schedules.stream().filter(x -> x.getUser().getUserId() == id)
				.collect(Collectors.toList());

		/******** Timing Validation **********/
		LocalDate dateNow = LocalDate.now();
		LocalTime time = LocalTime.now();
		LocalTime timeNow = time.truncatedTo(ChronoUnit.MINUTES);

		String s = schedule.getScheduleTime();
		String stTime = null;
		LocalTime inputTime = LocalTime.parse(s);

		int duration = 0;
		LocalTime startTime = null;
		LocalTime endTime = null;

		/********** ------------ ******************/
		if (schedule.getScheduleDate().isBefore(dateNow)
				|| ((schedule.getScheduleDate().isEqual(dateNow)) && (inputTime.isBefore(timeNow)))) {
			ra.addAttribute("errorMessage", "u can't schedule the interview for the past date or time");

			return "redirect:/addschedule";
		}

		if (interviewer != null) {
			for (Schedule sc : interviewer) {
				duration = sc.getDuration();
				stTime = sc.getScheduleTime();
				startTime = LocalTime.parse(stTime);
				endTime = startTime.plusMinutes(duration);

				if ((!((inputTime.isAfter(startTime) && inputTime.isAfter(endTime))
						|| ((inputTime.isBefore(startTime) && inputTime.isBefore(endTime)))))
						&& sc.getScheduleDate().equals(schedule.getScheduleDate())) {

					System.out.println("u can't schedule the interview at this time");
					ra.addAttribute("errorMessage", "u can't schedule the interview at this time");
					return "redirect:/addschedule";
				}
			}
		}

		if (schedule.getInterviewType().equals("Walk-in"))
			schedule.setMeetingLink("hi " + schedule.getCandidate().get(0).getCandidateName() + " your "
					+ "interview is scheduled on " + schedule.getScheduleDate() + " at " + schedule.getScheduleTime());

//		  System.out.println(schedule);
		if (result.hasErrors()) {
			return "scheduleRegister";
		}
		int size = schedule.getCandidate().size();
		// ArrayList list=null;//new ArrayList();
		if (size == 1) {

			System.out.println("entering if");

			ischeduleService.saveSchedule(schedule);
		} else {
			int tempDuration = schedule.getDuration();
			String temp = schedule.getScheduleTime();
			LocalTime tempTime = LocalTime.parse(temp);
			for (int i = 0; i < size; i++) {
				// list=new ArrayList()
				ArrayList list = new ArrayList();
				Schedule schedule1 = new Schedule();
//				schedule1.setCandidate(list);
				schedule1.setDuration(schedule.getDuration());
				schedule1.setInterviewType(schedule.getInterviewType());
				schedule1.setMeetingLink(schedule.getMeetingLink());
				schedule1.setScheduleDate(schedule.getScheduleDate());
				schedule1.setScheduleTime(tempTime.toString());
				schedule1.setUser(schedule.getUser());
				list.add(candidateList.get(i));
				schedule1.setCandidate(list);
				Schedule sch = ischeduleService.saveSchedule(schedule1);
				tempTime = tempTime.plusMinutes(tempDuration);
			}

		}

		return "redirect:/viewschedules";
	}

	@PostMapping("/saveschedule2")
	public String saveschedule2(@Valid @ModelAttribute Schedule schedule, BindingResult result, Model model,
			@RequestParam Integer candidateId, @RequestParam String status1, RedirectAttributes ra, HttpSession session,
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

		List<Candidate> candidateList = new ArrayList<Candidate>();
//System.out.println("params from add schedule status"+status1);
//		log.info("list"+candidateList);
		Candidate candidate = icandidateService.updateCandidate(candidateId);
		List<Candidate> listOfCandidate=new ArrayList<>();
		listOfCandidate.add(candidate);
		System.out.println(candidateId);
		System.out.println(candidate.getStatus());
		System.out.println(candidate.getStatus()=="TechnicalCompleted");
		if(status1.equalsIgnoreCase("nextTechnicalRound")) {
		if(candidate.getStatus().equalsIgnoreCase("TechnicalCompleted")) {
			candidate.setStatus("SecondTechnicalScheduled");
		}
		if(candidate.getStatus().equalsIgnoreCase("FirstTechnicalSelected")) {
			candidate.setStatus("SecondTechnicalScheduled");
		}
		else if(candidate.getStatus().equalsIgnoreCase("SecondTechnicalSelected")) {
			candidate.setStatus("ThirdTechnicalScheduled");
		}
		else if(candidate.getStatus().equalsIgnoreCase("ThirdTechnicalSelected")) {
			candidate.setStatus("FourthTechnicalScheduled");
		}
		}else if(status1.equalsIgnoreCase("hrRound")) {
			candidate.setStatus("HRRoundScheduled");
		}
		candidateList.add(candidate);
		log.info("list" + candidateList);

		List<Schedule> schedules = ischeduleService.viewScheduleList();
		int id = schedule.getUser().getUserId();
		List<Schedule> interviewer = schedules.stream().filter(x -> x.getUser().getUserId() == id)
				.collect(Collectors.toList());

		/******** Timing Validation **********/
		LocalDate dateNow = LocalDate.now();
		LocalTime time = LocalTime.now();
		LocalTime timeNow = time.truncatedTo(ChronoUnit.MINUTES);

		String s = schedule.getScheduleTime();
		String stTime = null;
		LocalTime inputTime = LocalTime.parse(s);

		int duration = 0;
		LocalTime startTime = null;
		LocalTime endTime = null;

		/********** ------------ ******************/
		if (schedule.getScheduleDate().isBefore(dateNow)
				|| ((schedule.getScheduleDate().isEqual(dateNow)) && (inputTime.isBefore(timeNow)))) {
			ra.addAttribute("errorMessage", "u can't schedule the interview for the past date or time");

			return "redirect:/addschedule2";
		}
		if (interviewer != null) {
			for (Schedule sc : interviewer) {
				duration = sc.getDuration();
				stTime = sc.getScheduleTime();
				startTime = LocalTime.parse(stTime);
				endTime = startTime.plusMinutes(duration);

				if ((!((inputTime.isAfter(startTime) && inputTime.isAfter(endTime))
						|| ((inputTime.isBefore(startTime) && inputTime.isBefore(endTime)))))
						&& sc.getScheduleDate().equals(schedule.getScheduleDate())) {

					System.out.println("u can't schedule the interview at this time");
					ra.addAttribute("errorMessage", "u can't schedule the interview at this time");
					return "redirect:/addschedule2";
				}
			}
		}

		if (schedule.getInterviewType().equals("Walk-in"))
			schedule.setMeetingLink("hi " + schedule.getCandidate().get(0).getCandidateName() + " your "
					+ "interview is scheduled on " + schedule.getScheduleDate() + " at " + schedule.getScheduleTime());

//		  System.out.println(schedule);
		if (result.hasErrors()) {
			return "hrRoundScheduleRegister";
		}
				
		if (candidateId != null) {
			if(ischeduleService.existsScheduleByCandidate(listOfCandidate)) {
				Schedule schedule1=ischeduleService.findByCandidate(listOfCandidate);
				schedule1.setCandidate(candidateList);
				schedule1.setDuration(schedule.getDuration());
				schedule1.setInterviewType(schedule.getInterviewType());
				schedule1.setMeetingLink(schedule.getMeetingLink());
				schedule1.setScheduleDate(schedule.getScheduleDate());
				schedule1.setScheduleTime(schedule.getScheduleTime());
				schedule1.setUser(schedule.getUser());
				ischeduleService.saveSchedule(schedule1);
			}else {
				schedule.setCandidate(candidateList);
				Schedule sche=ischeduleService.saveSchedule(schedule);
				System.out.println("sche value to check whether candidate updates"+sche);
			}
		}else {
			int tempDuration = schedule.getDuration();
			String temp = schedule.getScheduleTime();
			LocalTime tempTime = LocalTime.parse(temp);
		}

		return "redirect:/showInterviewCompleted";
	}

	@GetMapping({ "/viewschedules" })
	public ModelAndView getAllschedules(HttpSession session, HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!(checkUser.getRole().equals("hr") || checkUser.getRole().equals("interviewer")
				|| checkUser.getRole().equals("hrHead"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String role = (String) session.getAttribute("interviewer");
		User user2 = new User();
		user2.setRole(role);

		User user = (User) session.getAttribute("loginDetails");
		List<Schedule> scheduleList = ischeduleService.viewScheduleList();
		if (user.getRole().equalsIgnoreCase("hr")) {
			ModelAndView mav = new ModelAndView("scheduleList");
			mav.addObject("schedule", scheduleList);
			mav.addObject("role", user2);
			return mav;
		} else {
			List<Schedule> sch = scheduleList.stream().filter(s -> s.getUser().getUserId() == user.getUserId())
					.collect(Collectors.toList());
			ModelAndView mav = new ModelAndView("scheduleList");
			mav.addObject("schedule", sch);
			mav.addObject("role", user2);
			return mav;
		}
	}

//
	@GetMapping("/showUpdateSchedule")
	public ModelAndView showUpdateSchedule(@RequestParam Integer scheduleId, HttpSession session,
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

		Schedule schedule = ischeduleService.updateSchedule(scheduleId);
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
	public String deleteSchedule(@RequestParam Integer scheduleId, HttpSession session, HttpServletResponse redirect) {

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

		ischeduleService.deleteSchedule(scheduleId);
		return "redirect:/viewschedules";
	}

	@GetMapping("/showInterviewCompleted")
	public ModelAndView getfeedback(HttpSession session, HttpServletResponse redirect)// (@RequestParam Integer
																						// InterviewerId)
	{

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!(checkUser.getRole().equals("hr") || checkUser.getRole().equals("interviewer")
				|| checkUser.getRole().equals("hrHead"))) {
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
	public ModelAndView givefeedback(HttpSession session, HttpServletResponse redirect)// (@RequestParam Integer
																						// InterviewerId)
	{

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!(checkUser.getRole().equals("hr") || checkUser.getRole().equals("interviewer")
				|| checkUser.getRole().equals("hrHead"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User user = (User) session.getAttribute("loginDetails");
		ModelAndView mav = new ModelAndView("CompletedList");

		List<Schedule> scheduleList = ischeduleService.viewScheduleList();
		List<Schedule> list = null;
		if (user.getRole().equals("hr")) {
			list = ischeduleService.viewScheduleList();
		} else {
			list = scheduleList.stream().filter(s -> s.getUser().getUserId() == user.getUserId())
					.collect(Collectors.toList());
		}
//		System.out.println(list);
		ArrayList<Schedule> a = new ArrayList<Schedule>();

		for (Schedule schedules : list) {
			for (int i = 0; i < schedules.getCandidate().size(); i++) {
				if (schedules.getCandidate().get(i).getStatus() != null) {
					if (schedules.getCandidate().get(i).getStatus().equalsIgnoreCase("SECONDTECHNICALCOMPLETED")||schedules.getCandidate().get(i).getStatus().equalsIgnoreCase("THIRDTECHNICALCOMPLETED")||schedules.getCandidate().get(i).getStatus().equalsIgnoreCase("FOURTHTECHNICALCOMPLETED")||schedules.getCandidate().get(i).getStatus().equalsIgnoreCase("TECHNICALCOMPLETED")||schedules.getCandidate().get(i).getStatus().equalsIgnoreCase("HRROUNDCOMPLETED"))// &&
					// schedules.getInterviewer().getInterviewerId()==InterviewerId)
					{
						a.add(schedules);
					}
				}
			}
		}

		mav.addObject("schedule", a);
		mav.addObject("role", checkUser.getRole());
		return mav;
	}

}
