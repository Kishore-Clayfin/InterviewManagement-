package com.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cf.model.Candidate;
import com.cf.model.Feedback;

public interface IFeedbackDao extends JpaRepository<Feedback, Integer>
{
Feedback findByCandidate(Candidate candidate);
boolean existsFeedbackByCandidate(Candidate candidate);

}
