package com.cf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cf.model.Candidate;
import com.cf.model.Schedule;
import com.cf.model.User;
@Repository
public interface IScheduleDao extends JpaRepository<Schedule, Integer>
{
Schedule  findByCandidateIn(List<Candidate> candidate);	
boolean existsScheduleByCandidateIn(List<Candidate> candidateList);
List<Schedule> findByCandidate(Candidate candidate);
List<Schedule> findScheduleByUser(User user);
}
