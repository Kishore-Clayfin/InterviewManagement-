package com.cf.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Domain 
{
	@Id
	@GeneratedValue
	private Integer domainId;
	@NotBlank(message = "Domain Name cannot be empty.")
	private String domainName;
	
	@Valid
	//@JoinColumn(name = "domainSubCategId")
	@JoinTable(name = "domainSubCategoryId")
	@ManyToMany
	private List<DomainCategory>  domainCategory;

}
