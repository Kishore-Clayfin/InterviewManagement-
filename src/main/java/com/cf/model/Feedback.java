package com.cf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
	@GeneratedValue
	private Integer feedbackId;
	private Integer rating;
	@Column(name = "feedback")
	private String feed_back;
	private String interviewerFbStatus;
	private String hrFbStatus;
	
	
	@JoinColumn(name = "domainId")
	@OneToOne
	private Domain domain;
	
	@JoinColumn(name = "candidateId")
	@OneToOne
	private Candidate candidate;
}
