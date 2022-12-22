package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.model.Schedule;
import com.cf.repository.IScheduleDao;
@Service
public class ScheduleServiceImpl implements IScheduleService {

	@Autowired
	private IScheduleDao iScheduleDao;
	
	@Autowired 
	private EmailService email;
    
	@Override
	public Schedule saveSchedule(Schedule schedule) 
	{
		
		Schedule s= iScheduleDao.save(schedule);
		String to="";
		String text;
		if(s.getCandidate().size()==1)
		{
			text="Your interview has been scheduled on "+s.getScheduleDate()+" at "+s.getScheduleTime()+ " and Connect through  the link given below "+"\n"+"\n"+s.getMeetingLink();
			 to=s.getCandidate().get(0).getEmail();
			 System.out.println(to);
				email.sendSimpleMessage(to, "Interview", text);
				email.sendSimpleMessage(schedule.getUser().getEmail(), "Meeting", s.getMeetingLink());
		}
		else
		{
				s.getCandidate().stream().forEach(
					x->email.sendSimpleMessage(x.getEmail(), "Interview", "Hi, "+x.getCandidateName()+" your interview is scheduled on "+
			s.getScheduleDate()+"at "+s.getScheduleTime()+" use the below link to connect in Google Meet "+s.getMeetingLink())
			);
		}
		
		
		System.out.println("Mail sent");
		return s;
	}

	@Override
	public List<Schedule> viewScheduleList() {		
		return iScheduleDao.findAll();
	}

	@Override
	public Schedule updateSchedule(Integer scheduleId)
	{
		Schedule schedule= iScheduleDao.findById(scheduleId).orElseThrow(null);
		return schedule;
	}

	@Override
	public void deleteSchedule(Integer scheduleId) {
		iScheduleDao.deleteById(scheduleId);
	}

}
