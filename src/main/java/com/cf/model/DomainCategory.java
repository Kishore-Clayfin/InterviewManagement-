package com.cf.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class DomainCategory 
{
	@Id
	@GeneratedValue
	private Integer domSubCatId;
	
	@NotBlank( message = "Domain Subcategory Name can't be empty")
	private String domSubCatName;
	
}
