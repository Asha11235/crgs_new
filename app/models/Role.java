package models;

import play.*;
import play.data.validation.Required;
import play.data.validation.Unique;

import play.db.jpa.*;
import utils.UserUtils.UserRole;

import javax.persistence.*;
//import javax.validation.constraints.Pattern;

import models.deadbolt.RoleHolder;

import java.util.*;

/**
 * Role Model - Contain ACL Role data.
 */

@Entity
public class Role extends Model implements models.deadbolt.Role {

	@Required
	@Unique(message = "Role Name must be unique")
	
	public String name;

	public Role(String name) {

		this.name = name;

	}

	public static Role findByName(String name) {
		return Role.find("byName", name).first();
	}

	// From Role Interface
	@Override
	public String getRoleName() {
		return this.name;
	}

	/** return Admin role */
	public static Role getAdminRole() {
		return Role.findByName("Admin");
	}

	/** return Enumerator role */
	public static Role getEnumeratorRole() {
		return Role.findByName("Enumerator");
	}

	/*	*//** return DS role */
	/*
	 * public static Role getDSRole() { return Role.findByName("DS"); }
	 * 
	 *//** return FS role */
	/*
	 * public static Role getFSRole() { return Role.findByName("FS"); }
	 * 
	 *//** return M&H role */
	/*
	 * public static Role getMHRole() { return Role.findByName("M&EH"); }
	 * 
	 *//** return M&C role */
	/*
	 * public static Role getMCRole() { return Role.findByName("M&EC"); }
	 * 
	 *//** return DM role */
	/*
	 * public static Role getDMRole() { return Role.findByName("DM"); }
	 *//** return FF role */
	/*
	 * public static Role getFFRole() { return Role.findByName("FF"); }
	 *//** return RM role */
	/*
	 * public static Role getRMRole() { return Role.findByName("RM"); }
	 *//** return DO role *//*
							 * public static Role getDORole() { return
							 * Role.findByName("DO"); }
							 */

	/** return parent role from hierarchy */
	public static Role getParent(Role role) {
		Role parentRole = null;
		UserRole userRole = UserRole.getRole(role.name);
		switch (userRole) {

		case ENUMERATOR:
			parentRole = Role.findByName("M&EC");
			break;
		default:
			break;
		}

		return parentRole;
	}
	
	public static Role findByRoleCode(String roleCode, SchoolInformation schoolinformation) {
		return GeoUpazilla.find("byCodeAndschoolInformation", roleCode, schoolinformation).first();
	}
}