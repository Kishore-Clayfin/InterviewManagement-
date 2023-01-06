package com.cf.service;

import java.util.List;

import com.cf.model.Candidate;
import com.cf.model.Schedule;

public interface IScheduleService {

	Schedule saveSchedule(Schedule schedule);

	List<Schedule> viewScheduleList();

	Schedule updateSchedule(Integer scheduleId);

	void deleteSchedule(Integer scheduleId);
	
	Schedule  findByCandidate(List<Candidate> candidate);	
	
	boolean existsScheduleByCandidate(List<Candidate> candidateList);
}
