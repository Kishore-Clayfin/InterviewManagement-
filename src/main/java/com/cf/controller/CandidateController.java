package com.cf.controller;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cf.model.Candidate;
import com.cf.model.Domain;
import com.cf.model.User;
import com.cf.repository.ICandidateDao;
import com.cf.repository.IuserDao;
import com.cf.service.ICandidateService;
import com.cf.service.IDomainService;
//import com.cf.service.IFirebaseService;

@Controller
public class CandidateController {
	@Autowired
	private ICandidateService iCandidateService;

	@Autowired
	private IDomainService iDomainService;

	@Autowired
	private ICandidateDao iCandidateDao;
	
//	@Autowired
//	private IFirebaseService firebase;
	
	@Autowired
	private IuserDao iUserDao;
	@GetMapping("/addCandidate")
	public ModelAndView addCandidate(HttpSession session, HttpServletResponse redirect) {
Integer userId=null;
//		System.err.println(LoginController.checkUser==null);
		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
try {
		User check = (User) session.getAttribute("loginDetails");
		System.out.println("null or not check"+check);
		System.out.println("empty??"+check==null);
		if (!check.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}catch(Exception e) {
	System.out.println("null pointer exception");
}

		List<Domain> domain = iDomainService.viewDomainList();

		Candidate candidate = new Candidate();
		ModelAndView mav = new ModelAndView("candidateRegister");
		mav.addObject("candidate", candidate);
		mav.addObject("domain", domain);
		mav.addObject("userId",userId);
		mav.addObject("canId", null);
		mav.addObject("resName",null);
		return mav;
	}

	@PostMapping("/saveCandidate")
	public String saveCandidate(@Valid @ModelAttribute Candidate candidate, BindingResult result,
			@RequestParam("file") MultipartFile file,@RequestParam Integer exists,@RequestParam Integer candiExist, Model mav, HttpSession session, RedirectAttributes attributes,
			HttpServletResponse redirect) throws IOException {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String mob;
		Long nullcheck;
		User obj = (User) session.getAttribute("loginDetails");
		User user = new User();

		List<Domain> domain = iDomainService.viewDomainList();

		mav.addAttribute("domain", domain);

		boolean b = result.hasErrors();
		System.err.println(b);

		String name = file.getOriginalFilename();
		
		System.err.println("hiiiiiii" + name);

		nullcheck = candidate.getMobileNumber();

		if (result.hasErrors()) {
			return "candidateRegister";

		} else if (nullcheck == null) {
			attributes.addAttribute("numberNull", "Please enter a  mobile number");
			return "redirect:/addCandidate";
		}

		mob = candidate.getMobileNumber().toString();

		boolean mobv = Pattern.matches("^[9876]\\d{9}$", mob);

		if (!mobv == true) {
			attributes.addAttribute("numberError", "Please enter a valid mobile number");
			return "redirect:/addCandidate";
		}
		System.out.println("is file empty"+file.isEmpty());
		System.out.println("is file null"+file==null);
		if(!file.isEmpty()) {
		if (!file.getContentType().endsWith("pdf")) {
			attributes.addAttribute("fileError", "Only pdf format resume is allowed");
			return "redirect:/addCandidate";

		}
		System.out.println("resume is empty");
		}
		if ((candidate.getExperience() == null)) {
			candidate.setExpectedCtc(0.0f);
			candidate.setCurrentCtc(0.0f);

		}
//		if(candiExist==null) {
//		candidate.setResume(file.getBytes());
//		candidate.setResumeName(name);
//		}
//		else {
//			Candidate candi=iCandidateService.updateCandidate(candiExist);
//			candidate.setResume(candi.getResume());
//			candidate.setResumeName(candi.getResumeName());
//		}
		if(!file.isEmpty()||candiExist==null) {
		candidate.setResume(file.getBytes());
		candidate.setResumeName(name);
		}else{
			Candidate candi=iCandidateService.findResumeCandidate(candiExist);
			candidate.setResume(candi.getResume());
			candidate.setResumeName(candi.getResumeName());
		}
//		user.setUserId(obj.getUserId());
		if(exists==null) {
		candidate.setUser(obj);
		}else
		{
			User user2=iUserDao.findById(exists).orElseThrow();
			candidate.setUser(user2);
		}
		
		//DEFAULT STATUS
//		System.err.println(candidate.getStatus());
//		if(candidate.getStatus()==null) 
//		{
//			System.err.println("-------Comming Inside---------");
//			candidate.setStatus("INCOMPLETE");
//		}
//		String candidateEmail=candidate.getCandidateName()+".pdf";
//		String download=(String)firebase.upload(file,candidateEmail);
		System.out.println("filenammmme "+file.getOriginalFilename());
		System.out.println("firebaseUploadStarts");
		//candidate.setDownloadUrl(download);
//		System.out.println(download);
		
		iCandidateService.saveCandidate(candidate);

		return "redirect:/viewCandidates";
	}
	
	@PostMapping("/uploadOrEditResume")
	public String uploadOrEditResume(@RequestParam Integer candidateId,@RequestParam("file") MultipartFile file, HttpSession session,
			HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String name = file.getOriginalFilename();
		Candidate candidate=iCandidateService.updateCandidate(candidateId);
		System.out.println(name);
		try {
		candidate.setResume(file.getBytes());
		candidate.setResumeName(name);
		}
		catch(Exception e) {
			
		}
		iCandidateService.saveCandidate(candidate);
		return "redirect:/viewCandidates";
	}

	
	@GetMapping("/viewCandidates")
	public ModelAndView getAllCandidates(HttpSession session, HttpServletResponse redirect) {
		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if ((!checkUser.getRole().equals("hr"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ModelAndView mav = new ModelAndView("candidateList");
		mav.addObject("candidate", iCandidateService.viewCandidateList());
		return mav;
	}

	@GetMapping("/showUpdateCandidate")
	public ModelAndView showUpdateCandidate(@RequestParam Integer candidateId,HttpSession session,
			HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Domain> domain = iDomainService.viewDomainList();
		
		ModelAndView mav = new ModelAndView("candidateRegister");
		Candidate candidate = iCandidateService.updateCandidate(candidateId);
		
		Integer userId=candidate.getUser().getUserId();
		Integer canId=candidate.getCandidateId();
		System.out.println(candidate.getResumeName());
		mav.addObject("candidate", candidate);
		mav.addObject("domain", domain);
		mav.addObject("userId", userId);
		mav.addObject("canId", canId);
		mav.addObject("userId", candidate.getUser().getUserId());
		mav.addObject("resName", candidate.getResumeName());
		return mav;
	}
	
	@GetMapping("/updateExpectedCtc")
	public ModelAndView updateExpectedCtc(@RequestParam Integer candidateId, HttpSession session,
			HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		List<Domain> domain = iDomainService.viewDomainList();

		ModelAndView mav = new ModelAndView("candidateUpdateCtc");
		Candidate candidate = iCandidateService.updateCandidate(candidateId);
		mav.addObject("candidate", candidate);
		mav.addObject("domain", domain);
		return mav;
	}
	
	@PostMapping("/saveExpectedCtc")
	public String saveExpectedCtc(@ModelAttribute Candidate candidate, HttpSession session, RedirectAttributes attributes,
			HttpServletResponse redirect) throws IOException {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("candidate from model and view"+candidate.getExpectedCtc());
      Candidate candidate1=iCandidateService.updateCandidate(candidate.getCandidateId());
      System.out.println("candidate from jpa repo"+candidate1);
      candidate1.setExpectedCtc(candidate.getExpectedCtc());
      System.out.println("candidate after setting expected ctc"+candidate1.getExpectedCtc());
    Candidate dummy= iCandidateDao.save(candidate1);
    System.out.println("updated candidate"+dummy);
		return "redirect:/showInterviewCompleted";
	}

	@Transactional
	@GetMapping("/updateStatus")
	public String updateCandidateStatus(@RequestParam Integer candidateId, @RequestParam String status,
			HttpSession session, HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!(checkUser.getRole().equals("hr") || checkUser.getRole().equals("hrHead")
				|| checkUser.getRole().equals("interviewer"))) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Candidate candi = iCandidateService.updateCandidateStatus(candidateId, status);
		return "redirect:/viewschedules";
	}
	
//	@GetMapping("/download1")
//    public StreamingResponseBody downloadFile1(String candidateEmail) throws IOException {
//		//ResponseEntity<InputStreamResource>
////		firebase.download(candidateEmail);
////       File file = new File(FILE_PATH);
////       InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
////		InputStream resource =(InputStream) firebase.download(candidateEmail);
//		InputStreamResource resource2 = new InputStreamResource(resource);
//		OutputStream out=null;
//       return outputStream -> {
//           int nRead;
//           byte[] data = new byte[1024];
//           while ((nRead = resource.read(data, 0, data.length)) != -1) {
//               System.out.println("Writing some bytes..");
//               outputStream.write(data, 0, nRead);
//           }
//       };
//    }

	@GetMapping("/deleteCandidate")
	public String deleteCandidate(@RequestParam Integer candidateId, HttpSession session,
			HttpServletResponse redirect) {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User checkUser = (User) session.getAttribute("loginDetails");
		if (!checkUser.getRole().equals("hr")) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		iCandidateService.deleteCandidate(candidateId);
		return "redirect:/viewCandidates";
	}

	@GetMapping("/downloadFile")
	public void downloadFile(@RequestParam Integer candidateId, Model model, HttpSession session,
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
		if (!checkUser.getRole().equals("hr")) {
			try {
				response.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Candidate candidate1 = iCandidateService.findResumeCandidate(candidateId);
		if (candidate1 != null) {

			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename = " + candidate1.getResumeName() + ".pdf";
			response.setHeader(headerKey, headerValue);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(candidate1.getResume());
			outputStream.close();
		}
		
	//	String name = file.getOriginalFilename();
		String filename = candidate1.getResume().toString();
//		downloadFile1(filename);
		String candidateEmail=candidate1.getCandidateName()+".pdf";
//		String download = (String)firebase.download(candidateEmail);
//		System.out.println("Downloaded File" + download);
		
	}

}