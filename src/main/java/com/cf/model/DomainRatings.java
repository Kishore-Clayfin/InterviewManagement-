//package com.cf.model;
//
//import java.util.List;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.ManyToMany;
//import javax.persistence.OneToOne;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@ToString
//@AllArgsConstructor
//@Entity
//public class DomainRatings {
//    @Id
//    @GeneratedValue
//	private Integer ratingId;
//	
//    private String subRatingName;
//    
//    private String subRatingValue;
//    
//    @ManyToMany
//	private List<Domain> domain;
//	
//	@ManyToMany
//	private List<Candidate> candidate;
//	
//}