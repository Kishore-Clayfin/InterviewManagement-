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
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
	@NotBlank(message = "User's name Should n't be empty.")
	@Pattern(regexp = "^[A-Za-z]{5,15}$", 
	message = "User's name Should Be  minimum 5 characters and maximum 15 characters")
	private String username;
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
			message = " User's Password Should Be >7 and < 15 Characters and Atleast One Upper Case "
					+ "and Atleast One Lower Case and Atleast One Digit and Atleast One Special Character" )
	private String password;
	@NotBlank(message = "User's role Should n't be empty.")
	private String role;
	private Boolean enabled;
	@NotBlank(message = "User's alternate email cannot be empty.")
	@Email
	@Column(unique = true)
	private String email;
	
	@Valid
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
