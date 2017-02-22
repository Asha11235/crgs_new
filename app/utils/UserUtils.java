package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import play.Logger;
import models.Role;
import models.User;


public class UserUtils {

	public enum UserRole {
		//ADMIN("Admin"), MH("M&EH"), MC("M&EC"),DM("DM"),FF("FF"),RM("RM"),DO("DO"),ENUMERATOR("Enumerator");
		ADMIN("Admin"),ENUMERATOR("NCTF");

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

    /**
     * @param id Logged in user id
     * @return enumerator list
     */
    public static List<User> getEnListByLoggedInUser(long id){
    	Logger.info("(UserUtils.java) userId = " + id);
    	User user = User.findById(id);
    	String roleName = user.role.name;   
    	List<User> enumeratorList = new ArrayList<User>();
    	UserRole userRole = UserRole.getRole(roleName);
    	switch (userRole) {
		case ADMIN:	
			enumeratorList = User.findAllEnumeratorUsers();
			break;
		/*case DS:
			enumeratorList = getEnListByDsUser(id);
			break;
		case FS:
			enumeratorList = getAssignedEnumeratorList(id);
			break;
*/		case ENUMERATOR:
			enumeratorList = new ArrayList<User>((Collection<? extends User>) User.findById(id));
			break;
		default:
			break;
		}
    	if(enumeratorList.size() > 0) return enumeratorList;
    	return null;
    }
    
    private static List<User> getEnListByDsUser(long dsId){
    	List<User> assignedEnumeratorList = new ArrayList<User>();
    	List<User> fsUserList = assignedFsList(dsId);
    	for(User user : fsUserList){
    		assignedEnumeratorList.addAll(getAssignedEnumeratorList(user.id));
		}
    	return assignedEnumeratorList;
    } 
    
    /**
     * @param dsId is Data Supervisor id
     * @return field supervisor list subordinate of DS
     */
    private static List<User> assignedFsList(long dsId){
		List<User> assignedFsList = new ArrayList<User>();
		List<User> fsUserList = User.findFsByDsUser(dsId);
		for(User user : fsUserList){
			assignedFsList.add(user);
		}
		return assignedFsList;
	}
    
    /**
     * @param fsId is Field Supervisor id
     * @return enumerator list subordinate of FS  
     */
    private static List<User> getAssignedEnumeratorList(Long fsId){
    	List<User> assignedEnumeratorList = new ArrayList<User>();
		List<User> enUserList = User.findEnumeratorByFsUser(fsId);
		for(User user : enUserList){
			assignedEnumeratorList.add(user);
		}
		return assignedEnumeratorList;    	
    }    

}
