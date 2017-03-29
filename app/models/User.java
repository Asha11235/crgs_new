/*
 * Copyright (C) 2011 mPower Health
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package models;

import play.*;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.*;
import play.libs.Crypto;
import play.libs.Crypto.HashType;
import interfaces.Assignable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import models.deadbolt.RoleHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User Model - Contains user data.
 */

@Entity
public class User extends Model implements RoleHolder {
	@Required(message = "Required!")
	@Unique
	@MaxSize(120)
	@Email
	public String email;

	@Required
	@Unique
	@MaxSize(15)
	@MinSize(4)
	@Match(value = "^\\w*$", message = "Not a valid username")
	public String name;

	@Required
	@MaxSize(15)
	@MinSize(4)
	@Match(value = "[^=]*", message = "Not a valid password")
	@Password
	public String password;

	@Transient
	@Equals(value = "password", message = "Password doesn't match")
	@Password
	public String confirmPassword;

	@MaxSize(100)
	public String displayName;

	@Required
	@ManyToOne
	public Role role;

	@Required(message = "Can not be null")
	@ManyToOne(fetch=FetchType.LAZY)
	public SchoolInformation school;

	@Required(message = "Can not be null")
	@ManyToOne(fetch=FetchType.LAZY)
	public Ngo ngo;

	/* GEO-location Hierarchy */
	@Required(message = "Can not be null")
	@ManyToOne (fetch=FetchType.LAZY)
	public GeoDivision geoDivision;
	
	@Required(message = "Can not be null")
	@ManyToOne(fetch=FetchType.LAZY)
	public GeoDistrict geoDistrict;
	
	@Required(message = "Can not be null")
	@ManyToOne(fetch=FetchType.LAZY)
	public GeoUpazilla geoUpazilla;

	/**
	 * @param email
	 * @param password
	 */
	public User(String email, String password ) {
		this.email = email;
		this.password = password;
		
		/* this.geoPSUs = new TreeSet<GeoPSU>(); */
	}

	/**
	 * @param email
	 * @param password
	 * @param name
	 */
	public User(String email, String password, String name) {
		this(email, password);
		this.name = name;
	}

	public User(String email, String password, String name, Role role) {
		this(email, password, name);
		this.role = role;
	}

	// From RoleHolder Interface
	@Override
	public List<? extends Role> getRoles() {
		List<Role> list = new ArrayList<Role>();
		list.add(this.role);
		return list;
	}

	/**
	 * Authenticate.
	 *
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return the user
	 */
	public static User authenticate(String username, String password) {
		return User.find("byNameAndPassword", username, Crypto.passwordHash(password, HashType.SHA512)).first();
	}
	
	

	/**
	 * Find User by name
	 */
	public static User findByName(String name) {
		return User.find("byName", name).first();
	}

	/**
	 * Before save a User
	 */
	@PrePersist
	@PreUpdate
	public void beforeSave() {
		updatePassword();
	}

	/**
	 * Update password by its hash value
	 */
	private void updatePassword() {
		//Logger.info("password set for " + this);
		this.password = Crypto.passwordHash(this.password, HashType.SHA512);
	}

	/**
	 * Return list of User with given role
	 */
	public static List<User> getRoleUser(Role role) {
		return User.find("role = ?", role).fetch();
	}

	/*@Override
	public String toString() {
		return "User [email=" + email + ", name=" + name + ", password=" + password + ", confirmPassword="
				+ confirmPassword + ", displayName=" + displayName + ", role=" + role + ", school=" + school + ", ngo="
				+ ngo + ", geoDivision=" + geoDivision + ", geoDistrict=" + geoDistrict + ", geoUpazilla=" + geoUpazilla
				+ "]";
	}
*/
	public static User findByLogin(String username) {
		return User.find("byName", username).first();
	}

	public static List<User> findFsByDsUser(long dsId) {
		return User.find("byDs_id", dsId).fetch();
	}

	public static List<User> findEnumeratorByFsUser(long fsId) {
		return User.find("byFs_id", fsId).fetch();
	}

	public static List<User> findAllEnumeratorUsers() {
		return User.find("byRole_id", 4).fetch();
	}

	public boolean isAdmin() {
		return role.equals(Role.getAdminRole());
	}

	public boolean isEnumerator() {
		return role.equals(Role.getEnumeratorRole());
	}

	/**
	 * Return parent User. From Edmis hierarchy, M&H for M&C, M&C for DM, DM for
	 * FF, otherwise null
	 */
	public User getParentUser() {
		User parentUser = null;
		return parentUser;
	}

	public static List<User> getChildEnumerators(User loggedInUser) {
		List<User> enumList = new ArrayList<User>();
		List<User> users = User.findAll();

		for (User user : users) {
			if (user.getParentUser().equals(loggedInUser)
					|| user.getParentUser().getParentUser().equals(loggedInUser)) {
				enumList.add(user);
			}
		}
		return enumList;
	}
	
	

	public static String getUserData(Long divisionId, Long districtId, Long upazillaId,
			Long schoolId, Long roleId, Date startDate, Date endDate) throws SQLException {

		//Logger.info("getUserData");
		String qString = null;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String firstDateOfPreviousMonth = null;
		String lastDateOfPreviousMonth = null;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);

		calendar.set(Calendar.DATE, 1);
		if (startDate == null)
			startDate = calendar.getTime();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		if (endDate == null)
			endDate = calendar.getTime();

		try {
			firstDateOfPreviousMonth = dateFormat.format(startDate) + " 00:00:00";
			lastDateOfPreviousMonth = dateFormat.format(endDate) + " 23:59:59";

		} catch (Exception e) {
			e.printStackTrace();
		}

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;

		
		 qString =
		 "SELECT User.id, User.displayName , Role.name as role_name, GeoDivision.name as geodivision_name , "
		 +
		  "GeoDistrict.name as geodistrict_name , GeoUpazilla.name as geoupazilla_name, "
		+ "SchoolInformation.name as school_name FROM User JOIN Role ON " +
		  "User.role_id = Role.id JOIN GeoDivision ON User. geoDivision_id = GeoDivision.id JOIN GeoDistrict ON "
		 +
		 "User .geodistrict_id = GeoDistrict.id JOIN GeoUpazilla ON User.geoupazilla_id= GeoUpazilla.id JOIN SchoolInformation ON "
		 + "User.school_id= SchoolInformation.id where ";
		

		String whereClause = " ";

		if (divisionId != null) {

			whereClause += " User.geoDivision_id = " + divisionId;

		}
		if (districtId != null) {

			whereClause += " and User.geoDistrict_id = " + districtId;

		}
		if (upazillaId != null) {

			whereClause += " and User.geoUpazilla_id = " + upazillaId;

		}
		if (schoolId != null) {

			whereClause += " and User.id = " + schoolId;

		}
		
		if (roleId != null) {

			whereClause += " and User.role_id = " + roleId;

		}

		qString = qString + whereClause;
		
		//Logger.info("qString1 : " + qString);

		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		rs = queryForExecution.executeQuery();
 
		//Map<String, String> mp = new HashMap<String,String>();
		
		String schoolid=null;
		String username=null;
		String rolename=null;
		String divisionname=null;
		String districtname=null;
		String upazillaname=null;
		String schoolname=null;
		String msg="";
		
		int count = 0;
		try {

			while (rs.next()) {

				schoolid = rs.getString("id");
				username = rs.getString("displayName");
				rolename = rs.getString("role_name") ;
				divisionname = rs.getString("geodivision_name") ;
				districtname = rs.getString("geodistrict_name") ;
				upazillaname = rs.getString("geoupazilla_name") ;
				schoolname = rs.getString("school_name") ;
				
				msg = msg + ";" + schoolid + ";" + username + ";" + rolename + ";" + divisionname + ";" + districtname + ";" + upazillaname + ";" + schoolname ;
				//count++;
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		
		return msg;
	}
	
	

}