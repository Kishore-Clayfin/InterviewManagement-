package com.cf.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cf.service.IHistoryService;

@Controller
public class HistoryController {
@Autowired
private IHistoryService historyService;

@GetMapping("/viewHistoryList")
public ModelAndView viewHistory(HttpSession session) {
	ModelAndView mav = new ModelAndView("historyList");
	mav.addObject("historyList", historyService.findAllHistory());
	return mav;
}
}
