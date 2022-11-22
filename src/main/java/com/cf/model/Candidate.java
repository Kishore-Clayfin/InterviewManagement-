package com.cf.model;

import java.sql.Blob;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
//@ToString
@NoArgsConstructor
public class Candidate 
{
	@Id
	@GeneratedValue
	private Integer candidateId;
	
	
	@NotBlank(message = "Candidates's name Should n't be empty.")
	@Pattern(regexp = "^[A-Za-z]{5,15}$", 
	message = "Candidates's name Should Be  minimum 5 characters and maximum 15 characters")
	private String candidateName;
	@NotBlank(message = "Candidate's email cannot be empty.")
	@Email
	private String email;
	//@Pattern(regexp="^[+]91[6789]\\d{9}$",message="Candidate's mobile no should start with +91 and should be 10digits ")
    @Min(value=1,message=" mobile number can't be -ve ")
    @NotNull(message = "Mobile Number can't be empty")
	private  Long mobileNumber;
	@NotBlank(message = "Candidates's qualification Should n't be empty.")

	private String highQualification;
	@NotNull(message = "Candidate's marks cannot be empty.")
	@Min(value=1,message="cgpa can't be -ve ")
	@Max(value = 10,message = "cgpa should be below 10")
	private Float cgpa;
	@NotBlank(message = "Candidates's position Should n't be empty.")
	
	private String roleAppliedFor;
	@NotBlank(message = "Candidate's alternate email cannot be empty.")
	@Email
	private String alternateEmail;
	@NotNull(message = "Candidate's experience cannot be empty.")
	private Float experience;
	//@NotNull(message = "Candidate's resume cannot be empty.")

	@Lob
	private byte[] resume;
	private String status;
	
	@Min(value=0,message="ctc can't be -ve ")
	private Float expectedCtc;
	@Min(value=0,message="ctc can't be -ve ")
	private Float currentCtc;
	
	@Min(value=1,message=" mobile number can't be -ve ")
    @NotNull(message = "Mobile Number can't be empty")
//	@Column(nullable = false)
	private  Long alternateMobileNumber;
	
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;
	@JoinColumn(name = "domainId")
	@OneToOne
	private Domain domain;

	@Override
	public String toString() {
		return "Candidate [candidateId=" + candidateId + ", candidateName=" + candidateName + ", email=" + email
				+ ", mobileNumber=" + mobileNumber + ", highQualification=" + highQualification + ", cgpa=" + cgpa
				+ ", roleAppliedFor=" + roleAppliedFor + ", alternateEmail=" + alternateEmail + ", experience="
				+ experience + ", resume=" + resume + ", status=" + status + ", user=" + user + ", domain=" + domain
				+ "]";
	}
	
	
}
