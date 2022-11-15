package com.cf.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendSimpleMessage(String to, String subject, String text) {

		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom("ajmalakmal2326@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);

	}

//	@Autowired
//	private JavaMailSender mailSender;
//	
//	public void sendMail(String to, String subject, String text,FileSystemResource file)
//	{
//
//		MimeMessage message = mailSender.createMimeMessage();
//
//		try
//		{
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//			helper.setFrom("xxxxHospital@gmail.com");
//			helper.setTo(to);
//			helper.setSubject(subject);
//			helper.setText(text);
//
////			FileSystemResource file = new FileSystemResource("G:\\Spring boot\\WORKSPACE\\Hotel\\src\\main\\resources\\qrcodes\\Phonepe.png");
//			helper.addAttachment(file.getFilename(), file);
//
//		} 
//		catch (MessagingException e)
//		{
//			throw new MailParseException(e);
//		}
//		mailSender.send(message);
//	}

}
