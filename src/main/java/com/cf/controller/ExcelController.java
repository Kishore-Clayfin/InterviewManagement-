package com.cf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
public class ExcelController 
{	
	@Autowired
	private ICandidateService camdidateService;

	@Autowired
	private IDomainDao domainDao;
	@GetMapping("/excelPage")
	public String getExc() 
	{
		return "excel";
	}
	int count=0;
	@PostMapping("/import")
	public String mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile,HttpSession session) throws IOException 
	{
		
		User user= (User) session.getAttribute("loginDetails");
//		Domain dom=new Domain();
//		dom.setDomainId(13);
//		
		
    	List<Candidate> tempStudentList = new ArrayList<Candidate>();
		try {
//		
		    XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
		    XSSFSheet worksheet = workbook.getSheetAt(0);
		    Iterator<Row> rowIterator = worksheet.iterator();
//		    Candidate can=new Candidate();
//		    can.setEmail(getExc())te
		    
		    while (rowIterator.hasNext())
		    {
		    	Candidate candidate=new Candidate();
		    	candidate.setUser(user);
//		    	candidate.setDomain(dom);
		    	int i=0;
		    	if(count==0) {
		    		 rowIterator.next();
		    		count++;	
		    	}
		    	else {
		    	
		    	
		      Row row = rowIterator.next();
		      //For each row, iterate through all the columns
		      Iterator<Cell> cellIterator = row.cellIterator();

		      while (cellIterator.hasNext()) 
		      {
		    	  
		    	  
		        Cell cell = cellIterator.next();
		        
		        
		        //Check the cell type and format accordingly
		        switch (cell.getCellType()) 
		        {
		        
		          case Cell.CELL_TYPE_NUMERIC:
		        	  
		        	  if(i==0) {
		        		  
		        		  candidate.setCandidateId((int)cell.getNumericCellValue() );
		        		  System.out.print(cell.getNumericCellValue() + "\t");
		        	  }
		        	  else if(i==3) {
		        		  candidate.setMobileNumber((long)cell.getNumericCellValue() );
		        		  System.out.print(cell.getNumericCellValue() + "\t");
		        	  }
		        	  else if(i==5) {
			        	  candidate.setCgpa((float)cell.getNumericCellValue() );
			        	  System.out.print(cell.getNumericCellValue() + "\t");
			        	  }
		        	  else if(i==8) {
		        		  candidate.setExperience((float)cell.getNumericCellValue() );
		        		  System.out.print(cell.getNumericCellValue() + "\t");
		        	  }
		            
		            i++;
		            break;
		          case Cell.CELL_TYPE_STRING:
//		            System.err.println(i);
		            if(i==1) {
			        	  candidate.setCandidateName(cell.getStringCellValue() );
			        	  System.out.print(cell.getStringCellValue() + "\t");
			        	  }
			        	  else if(i==2) {
			        		  candidate.setEmail(cell.getStringCellValue() );
			        		  System.out.print(cell.getStringCellValue() + "\t");
			        	  }
			        	  else if(i==4) {
			        		  candidate.setHighQualification(cell.getStringCellValue() );
			        		  System.out.print(cell.getStringCellValue() + "\t");
			        	  } 
			        	  else if(i==6) {
			        		  candidate.setRoleAppliedFor(cell.getStringCellValue() );
			        		  System.out.print(cell.getStringCellValue() + "\t");
			        	  } 
			        	  else if(i==7) {
			        		  candidate.setAlternateEmail(cell.getStringCellValue() );
			        		  System.out.print(cell.getStringCellValue() + "\t");
			        	  } 
			        	  else if(i==9)
			        	  {
			        		  Domain domain=domainDao.findByDomainName((cell.getStringCellValue()).toUpperCase());
			        		  candidate.setDomain(domain);
			        		  System.out.print(cell.getStringCellValue() + "\t");
			        	  } 
		            i++;
		            break;
		        }
		      }
		      System.out.println("");
//		      System.out.println(candidate);
		      tempStudentList.add(candidate);
//				System.out.println(tempStudentList);

		    }
		    	
		    	
		    	
		    }
		}
		   // file.close();
		   catch (Exception e) {
		    e.printStackTrace();
		  }
//		    for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
//		        Candidate tempStudent = new Candidate();
//		            
//		        XSSFRow row = worksheet.getRow(i);
//		            tempStudent.setCandidateId((int) row.getCell(0).getNumericCellValue());
//		            tempStudent.setCandidateName(row.getCell(1).getStringCellValue());
////		            tempStudent.setEmail(row.getCell(3).getStringCellValue());
////		            Long mobile=Long.parseLong(row.getCell(4).getStringCellValue());
//		           // tempStudent.setMobileNumber((long)row.getCell(4).getNumericCellValue());
//		            tempStudent.setHighQualification(row.getCell(5).getStringCellValue());
////		            Float cgpa=Float.parseFloat(row.getCell(i+5).getStringCellValue());
//		       //     tempStudent.setCgpa((float)row.getCell(i+5).getNumericCellValue());
//		            tempStudent.setRoleAppliedFor(row.getCell(7).getStringCellValue());
//		         //   tempStudent.setAlternateEmail(row.getCell(7).getStringCellValue());
//		            //tempStudent.setExperience((float)row.getCell(1).getNumericCellValue());
////		            Float experience=Float.parseFloat(row.getCell(8).getStringCellValue());
////		            tempStudent.setExperience(experience);
//		        tempStudentList.add(tempStudent);   
//		    }
		System.out.println(tempStudentList);
		camdidateService.bulkSaveCandidate(tempStudentList);
	    return "excel";
	}
}
