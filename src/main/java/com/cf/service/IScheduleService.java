package com.cf.service;

import java.util.List;

import com.cf.model.Schedule;

public interface IScheduleService {

	Schedule saveSchedule(Schedule schedule);
//	Schedule findById(int id);
//	Schedule save(Schedule schedule);
//	void deleteById(int id);
//	List<Schedule> findAll();

	List<Schedule> viewScheduleList();

	Schedule updateSchedule(Integer scheduleId);

	void deleteSchedule(Integer scheduleId);
}
