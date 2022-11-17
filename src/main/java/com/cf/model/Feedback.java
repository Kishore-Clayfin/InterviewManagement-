package com.cf.model;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	@NotNull(message = "Rating can't be empty")
	private Integer rating;
	@Column(name = "feedback")
	@NotBlank(message = "Feedback can't be empty")
	private String feed_back;
//	@NotBlank(message = "Interviewer Status can't be empty")
	private String interviewerFbStatus;
//	@NotBlank(message = "Hr Status can't be empty")
	private String hrFbStatus;
	
	@Column(nullable = true)
	private String domainRatings;
	
	@ElementCollection
	@MapKeyColumn(name = "subName")
	@Column(name = "subRating")
	@CollectionTable(name = "subDomRatings")
	private Map<String, Integer> subDomRatings;
	
	
	
	@JoinColumn(name = "domainId")
	@OneToOne
	private Domain domain;
	
	@JoinColumn(name = "candidateId")
	@OneToOne
	private Candidate candidate;
	
//	@OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
//	private DomainRatings domainRatings;
	
}
