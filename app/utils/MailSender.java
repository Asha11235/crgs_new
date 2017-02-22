package utils;

import java.net.MalformedURLException;
import java.net.URL;


import models.CommentStatus;
import models.SchoolInformation;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import play.Logger;
import play.Play;

public class MailSender {

	/**
	 * send comment to data sender email
	 * */
	public static void sendCommentByGmail(SchoolInformation comment) throws MalformedURLException, EmailException {
		String fromAddr = "agriculture.ext@gmail.com";
		String fromName = "Agriculture Extension";
		/*String toAddr = comment.schoolHeadSirMailId;
		String subject = "Expert's recommendation for Register a School \"" + comment.name+ "\"";
		String body = "Dear Concern,"
						+ "\n\n" 
						+ "The recommendation for User "
						+ "<strong>\" , UserName : " + comment.schoolHeadSirUserName + "\"</strong>"
						+ " ,  password: "
						+ "<strong>\"" + comment.schoolHeadSirPassword+ "\"</strong>"
						+ " follows:\n"
						+ "<span>" +"User M name "+ comment.studentMaleUserName + "User M Pass "+ comment.studentMalePassword + "</span>"
						+ "<span>" +"User FM name "+ comment.studentFemaleUserName + "User FM Pass "+ comment.studentFemalePassword + "</span>"
						+ "<strong>"
						+ "\n\nRegards,"
						+ "\nExperts from"
						+ "\nUSAID Agriculture Extension Support Activity Project"
						+ "</strong>"*/
						;
		//replace newline to html line break
		/*body = body.replaceAll("\n", "<br/>");
		
		sendMail(fromAddr, fromName, toAddr, subject, body);
		
		comment.status = CommentStatus.SENT;
		comment.save();*/
		
	}
	
	/**
	 * send mail
	 * */
	public static void sendMail(String fromAddr, String fromName, String toAddr, String subject, String body ) throws EmailException, MalformedURLException {
		HtmlEmail email = new HtmlEmail();
		email.addTo(toAddr);
		email.setFrom(fromAddr, fromName);
		email.setSubject(subject);
		// embed the image and get the content id
		/*URL url = new URL("http://www.playframework.com/assets/images/favicon.png");
		String cid = email.embed(url, "Zenexity logo");*/
		// set the html message
		//email.setHtmlMsg("<html>"+ body +"Zenexity logo - <img src=\"cid:"+cid+"\"></html>");
		email.setHtmlMsg("<html>"+ body + "</html>");
		// set the alternative message
		email.setTextMsg(body);
		
		/*
		//email conf
		#smtp conf for mPower mail send
		mail.smtp.host=smtp.gmail.com
		mail.smtp.user=agriculture.ext@gmail.com
		mail.smtp.pass=easypassword
		mail.smtp.channel=ssl
		*/
		email.setHostName(Play.configuration.getProperty("mail.smtp.host"));
		email.setAuthentication(Play.configuration.getProperty("mail.smtp.user"), Play.configuration.getProperty("mail.smtp.pass"));
		email.setSSL(true);
		email.setCharset("UTF-8");
		
		email.send();
	}

}
