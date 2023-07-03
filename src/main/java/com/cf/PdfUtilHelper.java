package com.cf;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
 
import javax.servlet.http.HttpServletResponse;


import com.cf.model.Feedback;
import com.cf.model.History;
import com.lowagie.text.*;

import com.lowagie.text.pdf.*;
 

public class PdfUtilHelper {
	 private void writeTableHeader(PdfPTable table) {
	        PdfPCell cell = new PdfPCell();
	        cell.setBackgroundColor(Color.gray);
	        cell.setPadding(5);
	         
	        Font font = FontFactory.getFont(FontFactory.HELVETICA);
	        font.setColor(Color.WHITE);
	         
	        cell.setPhrase(new Phrase("CandidateId", font));
	         
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("CandidateName", font));
	        table.addCell(cell);
	        
	        cell.setPhrase(new Phrase("OverallRating", font));
	        table.addCell(cell);
	        
	        cell.setPhrase(new Phrase("Feedback", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("InterviewerFbStatus", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("HrFbStatus", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("DomainName", font));
        table.addCell(cell);
	        cell.setPhrase(new Phrase("SubCategoryRatings", font));
	        table.addCell(cell);
	      
	    }
	 
	     
	    private void writeTableData(PdfPTable table,Feedback feedback) {
	       // for (User user : listUsers) {
	    	
			/* System.out.println(feedback.getDomain().getDomainName()); */
	  

	    	
	            table.addCell(String.valueOf(feedback.getCandidate().getCandidateId()));
	            table.addCell(String.valueOf(feedback.getCandidate().getCandidateName()));
	            table.addCell(String.valueOf(feedback.getRating()));

	            table.addCell(String.valueOf(feedback.getFeed_back()));
	            table.addCell(String.valueOf(feedback.getInterviewerFbStatus()));
	            table.addCell(String.valueOf(feedback.getHrFbStatus()));
	            table.addCell(String.valueOf(feedback.getCandidate().getDomain().getDomainName()));
	            String subDomRat=feedback.getDomainRatings().replaceAll("=",":");//.replaceAll("{","").replaceAll("}","");
	            String subDomRat1=subDomRat.replace("{"," ");//.replaceAll("}","");
	            String subDomRat2=subDomRat1.replace("}"," ").replaceAll(",","  ,  ");
	            table.addCell(String.valueOf(subDomRat2));
	            
	           // table.addCell(bill.getBillDate().toString());
	           
	       // }
	    }
	     
	    public void export(HttpServletResponse response,Feedback feedback) throws DocumentException, IOException {
	        Document document = new Document(PageSize.A4);
	        PdfWriter.getInstance(document, response.getOutputStream());
	         
	        document.open();
	        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	        font.setSize(16);
	        font.setColor(Color.gray);
	         
	        Paragraph p = new Paragraph("Feedback ", font);
	        p.setAlignment(Paragraph.ALIGN_CENTER);
	     
	         
	        document.add(p);
	         
	        PdfPTable table = new PdfPTable(8);
	        table.setWidthPercentage(110f);
	        table.setWidths(new float[] {3.0f, 3.4f, 3.5f, 3.5f,3.5f,3.5f,3.5f,3.5f});
	        table.setSpacingBefore(2);
	         
	        writeTableHeader(table);
	        writeTableData(table,feedback);
	         
	        document.add(table);
	         
	        document.close();
	         
	    }
	    public void exportAll(HttpServletResponse response,List<Feedback> feedback) throws DocumentException, IOException {
	        Document document = new Document(PageSize.A4);
	        PdfWriter.getInstance(document, response.getOutputStream());
	         
	        document.open();
	        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	        font.setSize(16);
	        font.setColor(Color.gray);
	         
	        Paragraph p = new Paragraph("Feedback ", font);
	        p.setAlignment(Paragraph.ALIGN_CENTER);
	     
	         
	        document.add(p);
	         
	        PdfPTable table = new PdfPTable(8);
	        table.setWidthPercentage(110f);
	        table.setWidths(new float[] {3.0f, 3.4f, 3.5f, 3.5f,3.5f,3.5f,3.5f,3.5f});
	        table.setSpacingBefore(2);
	         
	        writeTableHeader(table);
	        for (Feedback feedback1 : feedback) 
	        {
	        	  writeTableData(table,feedback1);
			}
	      
	         
	        document.add(table);
	         
	        document.close();
	         
	    }
	    private void writeTableHeaderHistory(PdfPTable table) {
	        PdfPCell cell = new PdfPCell();
	        cell.setBackgroundColor(Color.gray);
	        cell.setPadding(5);	         
	        Font font = FontFactory.getFont(FontFactory.HELVETICA);
	        font.setColor(Color.WHITE);	         
	        cell.setPhrase(new Phrase("HistoryId", font)); 
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("CandidateId", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("CandidateName", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Email", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Other Email", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Mobile Number", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Other Mobile", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Cgpa", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Ctc", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Ectc", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Domain", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Resume", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Created By", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("status", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Feedback Id", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("Feedback", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("OverallRating", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("SubCategoryRatings", font));
	        table.addCell(cell);     
	        cell.setPhrase(new Phrase("InterviewerFbStatus", font));
	        table.addCell(cell);
	        cell.setPhrase(new Phrase("HrFbStatus", font));
	        table.addCell(cell);
	  
	       
	      
	    }
	    private void writeTableDataHistory(PdfPTable table,History history) {
		       // for (User user : listUsers) {
		    	
				/* System.out.println(feedback.getDomain().getDomainName()); */
		  

		    	
		            table.addCell(String.valueOf(history.getHistoryId()));
		            table.addCell(String.valueOf(history.getCandidateId()));
		            table.addCell(String.valueOf(history.getCandidateName()));
		            table.addCell(String.valueOf(history.getEmail()));
		            table.addCell(String.valueOf(history.getAlternateEmail()));
		            table.addCell(String.valueOf(history.getMobileNumber()));
		            table.addCell(String.valueOf(history.getAlternateMobileNumber()));
		            table.addCell(String.valueOf(history.getCgpa()));
		            table.addCell(String.valueOf(history.getCurrentCtc()));
		            table.addCell(String.valueOf(history.getExpectedCtc()));
		            table.addCell(String.valueOf(history.getDomainName()));
		            table.addCell(String.valueOf(history.getResumeName()));
		            table.addCell(String.valueOf(history.getUserName()));
		            table.addCell(String.valueOf(history.getStatus()));
		            table.addCell(String.valueOf(history.getFeedbackId()));
		            table.addCell(String.valueOf(history.getFeed_back()));
		            table.addCell(String.valueOf(history.getRating()));
		            table.addCell(String.valueOf(history.getDomainRatings()));      
		            table.addCell(String.valueOf(history.getInterviewerFbStatus()));
		            table.addCell(String.valueOf(history.getHrFbStatus()));
		    }
		     
		    public void exportHistory(HttpServletResponse response,History feedback) throws DocumentException, IOException {
		        Document document = new Document(PageSize.A4);
		        PdfWriter.getInstance(document, response.getOutputStream());
		         
		        document.open();
		        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		        font.setSize(16);
		        font.setColor(Color.gray);
		         
		        Paragraph p = new Paragraph("Feedback ", font);
		        p.setAlignment(Paragraph.ALIGN_CENTER);
		     
		         
		        document.add(p);
		         
		        PdfPTable table = new PdfPTable(20);
		        table.setWidthPercentage(110f);
		        table.setWidths(new float[] {3.0f, 3.4f, 3.5f, 3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f});
		        table.setSpacingBefore(2);
		         
		        writeTableHeaderHistory(table);
		        writeTableDataHistory(table,feedback);
		         
		        document.add(table);
		         
		        document.close();
		         
		    }
		    public void exportAllHistory(HttpServletResponse response,List<History> feedback) throws DocumentException, IOException {
		        Document document = new Document(PageSize.A4);
		        PdfWriter.getInstance(document, response.getOutputStream());
		         
		        document.open();
		        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		        font.setSize(16);
		        font.setColor(Color.gray);
		         
		        Paragraph p = new Paragraph("Feedback ", font);
		        p.setAlignment(Paragraph.ALIGN_CENTER);
		     
		         
		        document.add(p);
		         
		        PdfPTable table = new PdfPTable(20);
		        table.setWidthPercentage(110f);
		        table.setWidths(new float[] {3.0f, 3.4f, 3.5f, 3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f});
		        table.setSpacingBefore(2);
		         
		        writeTableHeaderHistory(table);
		        for (History feedback1 : feedback) 
		        {
		        	  writeTableDataHistory(table,feedback1);
				}
		      
		         
		        document.add(table);
		         
		        document.close();
		         
		    }
}