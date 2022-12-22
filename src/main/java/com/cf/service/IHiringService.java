package com.cf.service;

import java.util.List;

import com.cf.model.Hiring;

public interface IHiringService {

	Hiring saveHiring(Hiring hiring);

	List<Hiring> viewHiringList();

	Hiring updateHiring(Integer hiringId);

	void deleteHiring(Integer hiringId);

}