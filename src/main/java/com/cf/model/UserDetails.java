package com.cf.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
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
public class UserDetails 
{
	@Id
	@GeneratedValue
	private Integer userDetailsId;
	@Email(message = "Invalid Email address")
	private String personalMailId;
	@NotNull(message = "Mobile Number Can't be empty")
	@Min(value =1,message = "Mobile number should be +ve")
	private Long mobileNumber;
	@NotBlank(message = "Designation can't be empty")
	private String designation;
	@NotBlank(message = "Experties can't be empty")
	private String experties;
}
