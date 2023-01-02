package com.cf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cf.model.Candidate;
import com.cf.model.Domain;
import com.cf.model.User;
import com.cf.repository.ICandidateDao;
import com.cf.repository.IDomainDao;
import com.cf.service.ICandidateService;

@Controller
public class ExcelController {
	@Autowired
	private ICandidateService camdidateService;

	@Autowired
	private IDomainDao domainDao;

	public Domain findDomain(String domainName) {
		// comment for checking
		Domain domain = domainDao.findByDomainName(domainName);
		return domain;
	}

	@GetMapping("/excelPage")
	public String getExc(HttpServletResponse redirect) {
		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "excel";
	}

	int count = 0;

	@PostMapping("/import")
	public String mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile, HttpSession session,
			HttpServletResponse redirect) throws IOException {

		if (LoginController.checkUser == null) {
			try {
				redirect.sendRedirect("/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User user = (User) session.getAttribute("loginDetails");
		int count = 0;
		List<Candidate> tempStudentList = new ArrayList<Candidate>();
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
			XSSFSheet worksheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = worksheet.iterator();

			while (rowIterator.hasNext()) {
				Candidate candidate = new Candidate();
				candidate.setUser(user);

				int i = 0;
				if (count == 0) {
					rowIterator.next();
					count++;
				} else {

					Row row = rowIterator.next();
					// For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext()) {

						Cell cell = cellIterator.next();

						// Check the cell type and format accordingly
						switch (cell.getCellType()) {

						case Cell.CELL_TYPE_NUMERIC:

							if (i == 0) {

								candidate.setCandidateId((int) cell.getNumericCellValue());
//		        		  System.out.print(cell.getNumericCellValue() + "\t");
							} else if (i == 3) {
								candidate.setMobileNumber((long) cell.getNumericCellValue());
//		        		  System.out.print(cell.getNumericCellValue() + "\t");
							} else if (i == 5) {
								candidate.setCgpa((float) cell.getNumericCellValue());
//			        	  System.out.print(cell.getNumericCellValue() + "\t");
							} else if (i == 8) {
								candidate.setExperience((float) cell.getNumericCellValue());
//		        		  System.out.print(cell.getNumericCellValue() + "\t");
							} else if (i == 9) {
								candidate.setAlternateMobileNumber((long) cell.getNumericCellValue());
//		        		  System.out.print(cell.getNumericCellValue() + "\t");
							} else if (i == 10) {
								candidate.setCurrentCtc(((float) cell.getNumericCellValue()));
//		        		  System.out.print(cell.getNumericCellValue() + "\t");
							}
							i++;
							break;
						case Cell.CELL_TYPE_STRING:
							if (i == 1) {
								candidate.setCandidateName(cell.getStringCellValue());
//			        	  System.out.print(cell.getStringCellValue() + "\t");
							} else if (i == 2) {
								candidate.setEmail(cell.getStringCellValue());
//			        		  System.out.print(cell.getStringCellValue() + "\t");
							} else if (i == 4) {
								candidate.setHighQualification(cell.getStringCellValue());
//			        		  System.out.print(cell.getStringCellValue() + "\t");
							} else if (i == 6) {
								candidate.setRoleAppliedFor(cell.getStringCellValue());
//			        		  System.out.print(cell.getStringCellValue() + "\t");
							} else if (i == 7) {
								candidate.setAlternateEmail(cell.getStringCellValue());
//			        		  System.out.print(cell.getStringCellValue() + "\t");
							} else if (i == 11) {
								String dom = cell.getStringCellValue();
								Domain domain = findDomain(dom);
								System.err.println(domain);
								candidate.setDomain(domain);
//			        		  System.out.print(cell.getStringCellValue() + "\t");
							}
							i++;
							break;
						}
					}
					tempStudentList.add(candidate);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(tempStudentList);
		camdidateService.bulkSaveCandidate(tempStudentList);
		return "redirect:/viewCandidates";
	}
}
