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

import javax.persistence.*;

import models.deadbolt.RoleHolder;

import java.util.*;

/**
 * User Model - Contains user data.
 */

@Entity
public class User extends Model implements RoleHolder {
	@Required
    @Unique
    @MaxSize(120)
    @Email
    public String email; 

    @Required
    @Unique
    @MaxSize(15)
    @MinSize(4)
    @Match(value="^\\w*$", message="Not a valid username")
    public String name;

    @Required
    @MaxSize(15)
    @MinSize(4)
    @Match(value="[^=]*", message="Not a valid password")
    @Password
    public String password;

    @Transient
    @Equals(value="password", message="Password doesn't match")
    @Password
    public String confirmPassword;

    @MaxSize(100)
    public String displayName;

    @Required
    @ManyToOne
    public Role role;
    
    @Required
    @ManyToOne
    public SchoolInformation school;
    
    @Required
    @ManyToOne
    public Ngo ngo;
    
    /* GEO-location Hierarchy */
    @ManyToOne
	public GeoDivision geoDivision;
	@ManyToOne
	public GeoDistrict geoDistrict;
	@ManyToOne
	public GeoUpazilla geoUpazilla;
    
    /**
     * @param email
     * @param password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        /*this.geoPSUs = new TreeSet<GeoPSU>();*/
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
	 * @param username the username
	 * @param password the password
	 * @return the user
	 */
	public static User authenticate(String username, String password) {
		return User.find("byNameAndPassword", username, Crypto.passwordHash(password, HashType.SHA512)).first();
    }

	/**
	 * Find User by name
	 * */
	public static User findByName(String name) {
		return User.find("byName", name).first();
	}

	/**
	 * Before save a User
	 * */
	@PrePersist
	@PreUpdate
	public void beforeSave() {
		updatePassword();
	}
	
	/**
	 * Update password by its hash value
	 * */
	private void updatePassword() {
		Logger.info("password set for " + this);
		this.password = Crypto.passwordHash(this.password, HashType.SHA512);
	}

	/**
	 * Return list of User with given role
	 * */
	public static List<User> getRoleUser(Role role) {
		return User.find("role = ?", role).fetch();
	}

	@Override
	public String toString() {
		return this.id + " " + this.name + " " + this.email + " ";
	}
	
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
	 * Return parent User. From Edmis hierarchy, M&H for M&C, M&C for DM, DM for FF, otherwise null
	 * */
	public User getParentUser() {
		User parentUser = null;
		return parentUser;
	}
	
	public static List<User> getChildEnumerators(User loggedInUser){
		List<User> enumList = new ArrayList<User>();
			List<User> users = User.findAll();
			
			for(User user: users){
					if(user.getParentUser().equals(loggedInUser)
							|| user.getParentUser().getParentUser().equals(loggedInUser)){
						enumList.add(user);		
					}
			}
		return enumList;		
	}	
}