package controllers;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
import models.ApprovalHistory;
import models.Data;
import models.User;

public class ApprovedDataList extends Controller{
	
	public static void index() {
//		List<ApprovalHistory> history = ApprovalHistory.findAll();
		List<ApprovalHistory> history = ApprovalHistory.findByApprovalStatus(Integer.valueOf("0"), loggedInUser());;
		int len = history.size();
		render(history, len);
	}
	
	public static void updateStatus(){		
		String status =request.params.get("dataStatus");
		Logger.info(" (ApprovedDataList) Approval_status [0 for any data, 1 for approved data, -1 for rejected data] = " + status);		
		List<ApprovalHistory> history = ApprovalHistory.findByApprovalStatus(Integer.valueOf(status), loggedInUser());;		
		int len = history.size();
		render(history, len);
	}	

	static User loggedInUser(){
		return User.findByName(session.get("username"));		
	}

}
