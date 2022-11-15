package com.cf.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Feedback 
{
	@Id
	private Integer feedbackId;
	private Integer rating;
	private String feedback;
	private String interviewerFbStatus;
	private String hrFbStatus;
	
	
	@JoinColumn(name = "domainId")
	@OneToOne
	private Domain domain;
	
	@JoinColumn(name = "candidateId")
	@OneToOne
	private Candidate candidate;
}
