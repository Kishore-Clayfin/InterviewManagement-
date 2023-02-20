package com.cf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cf.model.Candidate;

public interface ICandidateDao extends JpaRepository<Candidate, Integer>
{
boolean existsCandidateByEmail(String email);
Candidate findCandidateByEmail(String email);
}
