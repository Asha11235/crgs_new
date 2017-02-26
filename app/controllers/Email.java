package controllers;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Email extends Controller {
	Session mailSession;
	protected void setMailServerProperties(){
		Properties emailProperties = new Properties();
		emailProperties.put("mail.smtp.port", "587");
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.starttls.enable","true");
		mailSession = Session.getDefaultInstance(emailProperties, null);
	}
	
	protected MimeMessage draftEmailMessage(String emailAddress, String subject, String body) throws AddressException, MessagingException{
		String toEmails = emailAddress;
		String emailSubject = subject;
		String emailBody = body;
		MimeMessage emailMessage = new MimeMessage(mailSession);
		
		/* *
		 *set the eail recipients
		 * */
		 /*for(int i=0;i<toEmails.length;i++){
			 emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
		 }*/
		emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails));
		 emailMessage.setSubject(emailSubject);
		 
		 //if sending HTML mail
		 emailMessage.setContent(emailBody, "text/html");
		return emailMessage;
		
	}
	
	protected void sendEmail( String emailAddress, String subject, String body)throws AddressException, MessagingException{
		String fromUser = "mpowersocial123@gmail.com";
		String fromUserEmailPassword = "mpower123456";
		
		String emailHost = "smtp.gmail.com";
		Transport transport = mailSession.getTransport("smtp");
		transport.connect(emailHost, fromUser, fromUserEmailPassword);
		
		MimeMessage emailMessage = draftEmailMessage(emailAddress,subject,body);
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully....!");
	}

}
