package com.cf.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.type.TrueFalseType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "users")
@Getter
@Setter
//@ToString
@NoArgsConstructor
public class User 
{
	@Id
	@GeneratedValue
	private Integer userId;
	private String username;
	private String password;
	private String role;
	private Boolean enabled;
	@Column(unique = true)
	private String email;
	
	@JoinColumn(name = "userDetailsId")
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private UserDetails userDetails;
	
//	@JoinColumn(name = "candidateId")
	@OneToMany(mappedBy = "user")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Candidate> candidate;

//	@Override
//	public String toString() {
//		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", role=" + role
//				+ ", enabled=" + enabled + ", email=" + email + ", userDetails=" + userDetails + ", candidate="
//				+ candidate + "]";
//	}
	
	
}
