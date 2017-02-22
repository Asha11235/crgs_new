package utils;

import java.util.ArrayList;

/**
 * Use this class to get approval states of the given data for the given approval states
 * */
public class ApprovalData {

	private String formName;
	private ArrayList<ApprovalState> approvalStates;
	private ArrayList<ApprovalLogic> approvalLogic;
	
	public ApprovalData(String formName, ArrayList<ApprovalState> approvalStates, ArrayList<ApprovalLogic> approvalLogic){
		this.formName = formName;
		this.approvalStates = approvalStates;
		this.approvalLogic = approvalLogic;
	}
	
	public String getFormName() {
		return formName;
	}
	
	public ArrayList<ApprovalState> getApprovalStates() {
		return approvalStates;
	}
	
	public ArrayList<ApprovalLogic> getApprovalLogic() {
		return approvalLogic;
	}
	
}
