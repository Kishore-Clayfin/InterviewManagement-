package com.cf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cf.model.Candidate;
import com.cf.model.Schedule;

public interface IScheduleDao extends JpaRepository<Schedule, Integer>
{
Schedule  findByCandidateIn(List<Candidate> candidate);	
boolean existsScheduleByCandidateIn(List<Candidate> candidateList);
}
