package com.cf;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
 
import javax.servlet.http.HttpServletResponse;


import com.cf.model.Feedback;
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
	            table.addCell(String.valueOf(feedback.getSubDomRatings()));
	            
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
}