package com.cf.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Schedule 
{
	@Id
	@GeneratedValue
	private Integer scheduleId; 
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate scheduleDate;
	private String scheduleTime;
	private Integer duration;
	private String meetingLink;
	private String interviewType;
	
	@JoinColumn(name = "userId")
	@OneToOne(cascade = { CascadeType.PERSIST,CascadeType.MERGE})
	private User user;
	
//	@JoinColumn(name = "candidateId")
	@ManyToMany(cascade = { CascadeType.PERSIST,CascadeType.MERGE})
	private List<Candidate> candidate;
}
