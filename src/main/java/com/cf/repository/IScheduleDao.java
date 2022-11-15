package com.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cf.model.Schedule;

public interface IScheduleDao extends JpaRepository<Schedule, Integer>
{

}
