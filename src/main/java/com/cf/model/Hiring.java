package com.cf.model;


import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hiring {
	@Id
	@GeneratedValue
	private Integer hiringId;
	
	
	
	private String hiringManager;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate hiringStartDate;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate hiringClosureDate;
	
	@OneToOne (cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	private Domain domain;
	
	@OneToOne 
	@JoinColumn(name = "recruiter")
	private User user;
	
	
	
}
