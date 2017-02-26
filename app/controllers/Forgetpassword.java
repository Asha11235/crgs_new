package controllers;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import models.PollDefination;
import models.PollQuestionOption;
import models.User;
import models.Vote;
import play.Logger;
import play.data.validation.Required;

public class Forgetpassword extends Controller{
	
	public static String submitForgetPassword(){
   	 Logger.info("Here is submitForgetPassword");
   	 String user_name = "";
   	 user_name = request.params.get("user_name");
   	 
   	 if(user_name.length() != 0){
	   	 User user = User.findByName(user_name);
	   	 Logger.info("User name:"+user_name);
	   	 //String email[] = {"+emailAddress+"};
	   	 //String email = "anyacharjee@gmail.com";
	   	 String email = user.email;
	   	 String password = "123"+user_name;
	   	 String message_subject = "Password reset message";
	   	 String body = "Password has been reset.<br>Your password is:"+password;
	   	 user.password = password;
	  	 user.save();
	  	 Email emailSending = new Email();
	  	 emailSending.setMailServerProperties();
	  	 try {
			emailSending.sendEmail(email,message_subject, body);
	  	 } catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  	 } catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  	 }
  	 
   	 }
   	 
   	 //if(user_name)
   	 return user_name;
   	 
   	
	}
	
	 public static void submit(){
	    	String polldev = request.params.get("polldef.id");
	    	String [] options = request.params.getAll("group1");
	    	Logger.info("Voting Controller "  + polldev);
	    	PollDefination poll = PollDefination.findById(Long.parseLong(polldev));
	    	
	    	for(int i = 0;i<options.length; i++){
	    		PollQuestionOption option =PollQuestionOption.findById(Long.parseLong(options[i]));
	    		Vote vote = new Vote();
	    		vote.poll = poll;
	    		vote.opton = option;
	    		vote.date = new Date();
	    		vote.save();
	    	}
	    	//flash.success("Thanks for your vote");
	    	try {
				Secure.login();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	
}
