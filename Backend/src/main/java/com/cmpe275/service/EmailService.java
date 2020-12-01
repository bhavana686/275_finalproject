package com.cmpe275.service;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class EmailService {

	
	public  ResponseEntity<Object> sendEmail(String mailTo,String subject,String message) {
	    String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "cmpe275group8@gmail.com";
        String password = "directexchange!08";
	 
		try {
			 sendplainemail(host, port, mailFrom, password, mailTo,
	                    subject, message);
	          
				return new ResponseEntity<>("Email Sent", HttpStatus.OK);
		} 
		
		 catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public  ResponseEntity<Object> sendEmail(HttpServletRequest request, @RequestBody JsonNode body) {
	    String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "cmpe275group8@gmail.com";
        String password = "directexchange!08";
	 
		try {
			 sendplainemail(host, port, mailFrom, password, body.get("sendto").asText(),
					 body.get("subject").asText() ,body.get("message").asText());
	          
				return new ResponseEntity<>("Email Sent", HttpStatus.OK);
		} 
		
		 catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	
	 public void sendplainemail(String host, String port,
	            final String userName, final String password, String toAddress,
	            String subject, String message) throws AddressException,
	            MessagingException {
	 
	       
	        Properties properties = new Properties();
	        properties.put("mail.smtp.host", host);
	        properties.put("mail.smtp.port", port);
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	 
	       
	        Authenticator auth = new Authenticator() {
	            public PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(userName, password);
	            }
	        };
	 
	        Session session = Session.getInstance(properties, auth);
	 
	       
	        Message msg = new MimeMessage(session);
	 
	        msg.setFrom(new InternetAddress(userName));
	        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
	        msg.setSubject(subject);
	        msg.setSentDate(new Date());
	    
	        msg.setText(message);
	        msg.setContent("<h1>"+message+"</h1>","text/html");
	        Transport.send(msg);
	 
	    }


}
