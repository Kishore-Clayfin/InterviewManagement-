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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
	@NotNull(message = "Duration should be not empty")
	@Min(value = 1,message = "Duration should be above 1")
	private Integer duration;
	@NotBlank(message = "Meeting Link can't be empty")
	private String meetingLink;
	@NotBlank(message = "Interview type can't be empty")
	private String interviewType;
	
	@JoinColumn(name = "userId")
	@OneToOne(cascade = { CascadeType.PERSIST,CascadeType.MERGE})
	private User user;
	
//	@JoinColumn(name = "candidateId")
	@ManyToMany(cascade = { CascadeType.PERSIST,CascadeType.MERGE})
	private List<Candidate> candidate;
}
