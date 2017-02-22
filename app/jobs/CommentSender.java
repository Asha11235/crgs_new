package jobs;

import models.SchoolInformation;
import play.Logger;
import play.jobs.Job;
import utils.MailSender;

/**
 * job to send comment by mail
 * */
public class CommentSender extends Job{

	public Long commentId;
	
	public CommentSender(Long commentId) {
		this.commentId = commentId;
	}
	
	@Override
	public void doJob()  throws Exception {
		SchoolInformation comment = SchoolInformation.findById(commentId);
		if(comment != null) {
			MailSender.sendCommentByGmail(comment);
			//Logger.info("Mail successfully sent to user " + comment.data.sender.login + " for farmer id " + comment.data.farmerId);
		}
	}
	
}
