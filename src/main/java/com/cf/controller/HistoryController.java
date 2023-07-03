package com.cf.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cf.PdfUtilHelper;
import com.cf.model.Candidate;
import com.cf.model.Feedback;
import com.cf.model.History;
import com.cf.model.User;
import com.cf.service.IHistoryService;

@Controller
public class HistoryController {
@Autowired
private IHistoryService historyService;

@GetMapping("/viewHistoryList")
public ModelAndView viewHistory(HttpSession session) {
	ModelAndView mav = new ModelAndView("historyList");
	mav.addObject("historyList", historyService.findAllHistory());
	mav.addObject("title","HISTORY LIST");
	return mav;
}
@GetMapping("/viewRejectedList")
public ModelAndView viewDeleteHistory(HttpSession session) {
	ModelAndView mav = new ModelAndView("historyList");
	mav.addObject("historyList", historyService.findAllDeletedCandidates());
	mav.addObject("title","REJECTED LIST");
	return mav;
}
@GetMapping("/viewHistoryResume")
public ResponseEntity<byte[]> viewFile(@RequestParam Integer historyId, Model model, HttpSession session,
		HttpServletResponse response ) throws IOException {

	if (LoginController.checkUser == null) {
		try {
			response.sendRedirect("/login");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	User checkUser = (User) session.getAttribute("loginDetails");
	if (!(checkUser.getRole().equals("hr"))) {
		try {
			response.sendRedirect("/login");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	History candidate1 = historyService.findHistoryById(historyId);
	ResponseEntity<byte[]> response1=null;
	if (candidate1 != null) {
		 HttpHeaders headers = new HttpHeaders();

		    headers.setContentType(MediaType.parseMediaType("application/pdf"));
//		response.setContentType("application/octet-stream");
		
		String headerValue = "attachment; filename = " + candidate1.getResumeName();// + ".pdf";
		
		headers.add("content-disposition", "inline;filename=" + candidate1.getResumeName());
		  headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		   response1 = new ResponseEntity<byte[]>(candidate1.getResume(), headers, HttpStatus.OK);
		
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(candidate1.getResume());
		outputStream.close();
	}
	
//	String name = file.getOriginalFilename();
	String filename = candidate1.getResume().toString();
//	downloadFile1(filename);
	String candidateEmail=candidate1.getCandidateName()+".pdf";
//	String download = (String)firebase.download(candidateEmail);
//	System.out.println("Downloaded File" + download);
	return response1;
}
@GetMapping("/downloadHistoryResume")
public void downloadFile(@RequestParam Integer historyId, Model model, HttpSession session,
		HttpServletResponse response ) throws IOException {

	if (LoginController.checkUser == null) {
		try {
			response.sendRedirect("/login");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	User checkUser = (User) session.getAttribute("loginDetails");
	if (!(checkUser.getRole().equals("hr") || checkUser.getRole().equals("interviewer")
			|| checkUser.getRole().equals("hrHead"))) {
		try {
			response.sendRedirect("/login");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	History candidate1 = historyService.findHistoryById(historyId);
	if (candidate1 != null) {

		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename = " + candidate1.getResumeName();// + ".pdf";
		response.setHeader(headerKey, headerValue);
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(candidate1.getResume());
		outputStream.close();
	}
	
//	String name = file.getOriginalFilename();
	String filename = candidate1.getResume().toString();
//	downloadFile1(filename);
	String candidateEmail=candidate1.getCandidateName()+".pdf";
//	String download = (String)firebase.download(candidateEmail);
//	System.out.println("Downloaded File" + download);
	
}

@GetMapping("/generatePdfHistory")
public void generatePdfFile(HttpServletResponse response, @RequestParam(value = "historyId") Integer historyId)
		throws IOException {
	if (LoginController.checkUser == null) {
		try {
			response.sendRedirect("/login");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	History feedback1 = historyService.findHistoryById(historyId);
	System.out.println(historyId);
	System.out.println(historyId);
	PdfUtilHelper export = new PdfUtilHelper();
	export.exportHistory(response, feedback1);

}

@GetMapping("/generateAllPdfHistory")
public void generateAllPdfFileHisory(HttpServletResponse response) throws IOException {
	if (LoginController.checkUser == null) {
		try {
			response.sendRedirect("/login");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	List<History> feedback2 = historyService.findAllHistory();
	PdfUtilHelper export = new PdfUtilHelper();

	export.exportAllHistory(response, feedback2);

}
}
