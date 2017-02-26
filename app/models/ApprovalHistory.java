package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.type.DateType;
import org.joda.time.DateTime;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ApprovalHistory extends Model{
	
	public int status;
	
	@Required
	@ManyToOne
	public User changedBy;
	
	//public Dat changedAt;
	
	@Required
	@ManyToOne
	public Data data;
	
	public enum UserRole {
		ADMIN("Admin"), DS("DS"), FS("FS"), ENUMERATOR("Enumerator");

		private String roleTxt;

		UserRole(String role) {
			this.roleTxt = role;
		}

		public static UserRole getRole(String role) {
			if (role != null) {
				for (UserRole b : UserRole.values()) {
					if (role.equalsIgnoreCase(b.roleTxt)) {
						return b;
					}
				}
			}
			return null;
		}
	};

	
	public static List<ApprovalHistory> findByApprovalStatus(int status, User loggedInUser) {
		String roleName = loggedInUser.role.name;
		UserRole role = UserRole.getRole(roleName);
		List<User> assignedFsList = assignedFsList(loggedInUser.id);
		if(Integer.valueOf(status) == 0){ //When approval status is any			
			switch (role) {
			case ADMIN:		
				return ApprovalHistory.findAll();				
			case DS:
				return ApprovalHistory.find("select a from ApprovalHistory a where a.changedBy in (:changed)").setParameter("changed", assignedFsList).fetch();						
			case FS:
				return ApprovalHistory.find("byChangedBy_id", loggedInUser.id).fetch();						
			default:
				return null;				
			}			
		}else{ //When approval status is approved or rejected
			switch (role) {
			case ADMIN:		
				return ApprovalHistory.find("byStatus", status).fetch();				
			case DS:
				return ApprovalHistory.find("select a from ApprovalHistory a where a.status = ? and a.changedBy in (:changed)",status).setParameter("changed", assignedFsList).fetch();				
			case FS:
				return ApprovalHistory.find("byStatusAndChangedBy_id", status, loggedInUser.id).fetch();						
			default:
				return null;				
			}
		}    	
    }
	
/*	private static List<Long> assignedFsList(long loggedInUserId){
		List<Long> assignedFsId = new ArrayList<Long>();
		List<User> fsUserList = User.findFsByDsUser(loggedInUserId);
		for(User user : fsUserList){
			assignedFsId.add(user.id);
		}
		return assignedFsId;
	}
*/
	private static List<User> assignedFsList(long loggedInUserId){
		List<User> assignedFsList = new ArrayList<User>();
		List<User> fsUserList = User.findFsByDsUser(loggedInUserId);
		for(User user : fsUserList){
			assignedFsList.add(user);
		}
		return assignedFsList;
	}
	
}
