package com.cf.model;
import java.util.Map;

import javax.annotation.Generated;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;
@Entity
@Data
public class History {
	@Id
	@GeneratedValue
	private Integer historyId;
	private Integer candidateId;
	private String candidateName;
	private String email;
	private  Long mobileNumber;
	private String highQualification;
	private Float cgpa;
	private String roleAppliedFor;
	private String alternateEmail;
	private Float experience;
	@Lob
	private byte[] resume;
	private String resumeName;
	private String status;
	private Float expectedCtc;
	private Float currentCtc;
	private  Long alternateMobileNumber;
	private String userName;
	private Integer feedbackId;
	private Integer rating;
	private String feed_back;
	private String interviewerFbStatus;
	private String hrFbStatus;
	private String domainName;
	private String domainRatings;
	
}