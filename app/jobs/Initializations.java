package jobs;

import java.io.File;

import models.Form;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Initializations extends Job{

	@Override
	public void doJob() throws Exception {
		super.doJob();
		
		/*Form form = Form.findByShortName("WB_BPD");
		if(form!=null){
    	byte[] byteApprovalLogic = form.approvalxml;
    	new FormValidationParsing(byteApprovalLogic);
		}*/
	}
	
}
