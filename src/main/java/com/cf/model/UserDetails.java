package com.cf.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	private String personalMailId;
	private Long mobileNumber;
	private String designation;
	private String experties;
}
