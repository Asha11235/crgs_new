package utils;

import java.util.ArrayList;

public class ApprovalLogic {

	private String roleName;
	private ArrayList<String> onState;
	
	private String acceptAction;
	private String rejectAction;
	private String defaultAction;
	
	public ApprovalLogic(String role, String onState, String accept, String reject, String def){
		this.roleName = role;
		this.onState = new ArrayList<String>();
		String [] states = onState.split("[,\\s]\\s*");
		for(String s: states)
			this.onState.add(s);
		this.acceptAction = accept;
		this.rejectAction = reject;
		this.defaultAction = def;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @return the onState
	 */
	/*public String getOnState() {
		return onState;
	}*/

	/**
	 * Checks for the current state's existence in the logic list
	 * @param state
	 * @return true if state exists, false otherwise
	 */
	public boolean hasState(String state){
		for (String s: this.onState)
			if (s.equals(state))
				return true;
		return false;
	}
	
	/**
	 * @return the acceptAction
	 */
	public String getAcceptAction() {
		return acceptAction;
	}

	/**
	 * @return the rejectAction
	 */
	public String getRejectAction() {
		return rejectAction;
	}

	/**
	 * @return the defaultAction
	 */
	public String getDefaultAction() {
		return defaultAction;
	}
	
	@Override
	public String toString(){
		if (defaultAction != null)
			return roleName + " " + onState + " " + acceptAction + " " + rejectAction;
		else
			return roleName + " " + onState + " " + acceptAction + " " + rejectAction + " " + defaultAction;
	}
	
}
