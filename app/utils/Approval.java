package utils;

import java.util.ArrayList;

import controllers.Application;
import models.Role;
import models.User;
import play.Logger;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.mvc.Scope.Flash;

public class Approval extends ApplicationClass {

	public static final int ACTION_ACCEPT = 1001;
	public static final int ACTION_REJECT = 1002;
	public static final int ACTION_DEFAULT = 990;

	public static final int NULL_VALUE = -999999;

	private ArrayList<ApprovalData> approvals;

	public Approval() {
		this.approvals = new ArrayList<ApprovalData>();
	}

	public ApprovalData getApproval(String formName) {
		for (ApprovalData app : approvals) {
			if (app.getFormName().equals(formName))
				return app;
		}
		return null;
	}

	public void addApproval(ApprovalData approval) {
		this.approvals.add(approval);
	}

	public boolean removeApproval(String formName) {
		for (ApprovalData app : approvals) {
			if (app.getFormName().equals(formName))
				return approvals.remove(app);
		}
		return false;
	}

	public boolean hasApproval(String formName) {
		for (ApprovalData app : approvals) {
			if (app.getFormName().equals(formName))
				return true;
		}
		return false;
	}

	public int getDataApprovalState(ApprovalData formApprovalData,
			long formDataId, User user, int currentApprovalStatus, int action) {
		ArrayList<ApprovalState> states = formApprovalData.getApprovalStates();
		ArrayList<ApprovalLogic> logics = formApprovalData.getApprovalLogic();
		String currentState = null;
		for (ApprovalState s : states) {
			String tempState = s.get(currentApprovalStatus);
			if (tempState != null) {
				currentState = tempState;
				break;
			}
		}
		ArrayList<String> actions = new ArrayList<String>();
		for (ApprovalLogic l : logics) {
			Logger.info("Logic: " + l.toString());
			if (l.hasState(currentState)) {
				Logger.info("User Name: " + user.name);
				String userRole = user.role.name;
				Logger.info("Role Name: " + userRole);
				if (userRole != null && l.getRoleName().equals(userRole)) {
					if (action == ACTION_ACCEPT) {
						Logger.info("Accept Action: " + l.getAcceptAction());
						actions.add(l.getAcceptAction());
					} else if (action == ACTION_REJECT) {
						actions.add(l.getRejectAction());
					} else {
						actions.add(l.getDefaultAction());
					}
				}else if(userRole != null){
					Flash.current().success(userRole + "has no permission to approve information.");
				}
			}
		}
		
		
		/*ArrayList<ApprovalState> states = Application.approval.getApproval("FSTRN").getApprovalStates();
		for (ApprovalState s: states){
			String statename = s.get(stateNumber);
		}*/
		
		Logger.info("List of possible actions : " + actions.toString());
		int ret = NULL_VALUE;
		for (String act : actions) {
			for (ApprovalState s : states) {
				if (s.getName().equals(act)) {
					ret = s.getValue();
				}
			}
		}
		Logger.info("Result: " + ret);
		return ret;
	}
}
