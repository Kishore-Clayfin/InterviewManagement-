package com.cf.model;

import java.sql.Blob;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
	private String candidateName;
	private String email;
	private Long mobileNumber;
	private String highQualification;
	private Float cgpa;
	private String roleAppliedFor;
	private String alternateEmail;
	private Float experience;
	@Lob
	private byte[] resume;
	private String status;
	
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;
	
	@JoinColumn(name = "domainId")
	@OneToOne(cascade  = CascadeType.PERSIST)
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
