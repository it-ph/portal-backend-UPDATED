package com.personiv.portal.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender sender;

	public  void sendMail(String receiver,String subject,String message,String from) throws MessagingException {
		
		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);    
		
		msg.setContent(message,"text/plain");
		helper.setTo(receiver);
		helper.setFrom(from);
		helper.setSubject(subject);
		sender.send(msg);
	
	}
	
	public boolean isValidEmail(String email) {
		boolean valid = false;
		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			valid = true;
		}catch(AddressException e) {
			e.printStackTrace();
		}
		return valid;
	}
}
