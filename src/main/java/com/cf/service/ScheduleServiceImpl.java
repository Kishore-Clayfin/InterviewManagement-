package com.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.model.Candidate;
import com.cf.model.Schedule;
import com.cf.model.User;
import com.cf.repository.IScheduleDao;

import lombok.extern.log4j.Log4j2;
@Log4j2
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
		log.info("new schedule added");
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
		log.info("find all schedules from the database");
		return iScheduleDao.findAll();
	}

	@Override
	public Schedule updateSchedule(Integer scheduleId)
	{
		Schedule schedule= iScheduleDao.findById(scheduleId).orElseThrow(null);
		log.info("Schedule with scheduleId "+scheduleId +" updated");
		return schedule;
	}

	@Override
	public void deleteSchedule(Integer scheduleId) {
		iScheduleDao.deleteById(scheduleId);
		log.info("Schedule with scheduleId "+scheduleId +" deleted");
	}

	@Override
	public Schedule findByCandidate(List<Candidate> candidate) {
		// TODO Auto-generated method stub
		Schedule schedule=iScheduleDao.findByCandidateIn(candidate);
		return schedule;
	}

	@Override
	public boolean existsScheduleByCandidate(List<Candidate> candidateList) {
		// TODO Auto-generated method stub
		boolean checkByCandidate=iScheduleDao.existsScheduleByCandidateIn(candidateList);
		return checkByCandidate;
	}

	@Override
	public List<Schedule> findScheduleByUser(User user) {
		// TODO Auto-generated method stub
		List<Schedule> listOfSchedule=iScheduleDao.findScheduleByUser(user);
		return listOfSchedule;
	}

}
