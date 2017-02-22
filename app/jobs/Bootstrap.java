/*
 * Copyright (C) 2012 mPower Social
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
package jobs;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import models.Aco;
import models.Event;
import models.Form;
import models.Role;
import models.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.mvc.Controller;
import play.test.Fixtures;
import controllers.deadbolt.ExternalRestrictions;

/**
 * Bootstrap Job - Process jobs when application starts
 */
@OnApplicationStart
public class Bootstrap extends Job {
	/* (non-Javadoc)
	 * @see play.jobs.Job#doJob()
	 */
	public void doJob() {
		// loadTestData();
		housekeeping();
		loadInitialData();
		makeAccessControlObjects();
		setACLforRoles();
	}
	
	private void housekeeping() {
		// Check if 'aggregate.uploadFolder' exists or not, if not then create it
		String path = Play.configuration.getProperty("aggregate.uploadDir") + File.separator;
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		//create media directory
		String mpath = Play.configuration.getProperty("media.dir", "media") + File.separator;
		File mdir = new File(mpath);
		if(!mdir.exists()) {
			mdir.mkdirs();
		}
		
		String mediaVideoDirPath = mpath + Play.configuration.getProperty("media.dir.video", "video") + File.separator;
		File mediaVideoDir = new File(mediaVideoDirPath);
		if(!mediaVideoDir.exists()) {
			mediaVideoDir.mkdirs();
		}

		String mediaImageDirPath = mpath + Play.configuration.getProperty("media.dir.image", "image") + File.separator;
		File mediaImageDir = new File(mediaImageDirPath);
		if(!mediaImageDir.exists()) {
			mediaImageDir.mkdirs();
		}

		String mediaAudioDirPath = mpath + Play.configuration.getProperty("media.dir.audio", "audio") + File.separator;
		File mediaAudioDir = new File(mediaAudioDirPath);
		if(!mediaAudioDir.exists()) {
			mediaAudioDir.mkdirs();
		}

		//create publish directory
		String ppath = Play.applicationPath.getAbsolutePath() + File.separator + Play.configuration.getProperty("publish.file.location", "publish");
		File pdir = new File(ppath);
		if(!pdir.exists()) {
			pdir.mkdirs();
		}
				
	}

	private void loadInitialData() {
		if(User.count() == 0) {
			Fixtures.loadModels("initial-data.yml");
		}
	}

	private void makeAccessControlObjects() {
		List<Class> controllers = Play.classloader.getAssignableClasses(Controller.class);
		for(Class controller : controllers) {
			Method[] methods = controller.getMethods();
			for(Method method : methods) {
				if (Modifier.isStatic(method.getModifiers())
						&& !method.getDeclaringClass().equals(Controller.class)
						&& method.isAnnotationPresent(ExternalRestrictions.class)) {

					ExternalRestrictions annotation = method.getAnnotation(ExternalRestrictions.class);
					for(String name : annotation.value()) {
						Aco aco = Aco.findByName(name);
						if(aco == null) {
							aco = new Aco(name, controller.getSimpleName());
							Role admin = Role.findById(1L);
							aco.roles.add(admin);
							aco.save();
						}
					}
				}
			}
		}
	}

	/**
	 * Set role specific Access control
	 * */
	private void setACLforRoles() {
/*		{//M&H role ACL
			Role mhRole = Role.getMHRole();
			if (mhRole != null) {
				String[] accessList = 
						{
						"Edit User",				
						"View User",
						"ACL",
						"Edit Upazilla",				
						"Edit Union", 				
						"Edit Division",			
						"View Division",
						"Edit District",
						"View Union",
						"View District",
						"View Upazilla",
						"Dashboard",
						"View Form",
						"Edit Form",			
						"Export Data",			
						"View Role",				
						"Edit Role",			
						"Data Approve",		
						"Approved Data List",				
						"Compared Data List", 			
						"Upload CSV", 				
						"Web Entry",
						"View Data",
						};
				setACL(mhRole, accessList);
			}
		}
		
		{//M&C role ACL
			Role mcRole = Role.getMCRole();
			if (mcRole != null) {
				String[] accessList = 
						{
						"Dashboard",
						"Edit User",				
						"View User",			
						"View Form",
						"Data Approve",		
						"Approved Data List",					
						"Export Data",	
						"View Data",
					};
				setACL(mcRole, accessList);
			}
		}
		{//DM role ACL
			Role dmRole = Role.getDMRole();
			if (dmRole != null) {
				String[] accessList = 
						{
						"Dashboard",
						"View Form",
						"Data Approve",		
						"Approved Data List",					
						"Export Data",	
						"View Data",
					};
				setACL(dmRole, accessList);
			}
		}
		{//RM role ACL
			Role rmRole = Role.getRMRole();
			if (rmRole != null) {
				String[] accessList = 
					{
					"Dashboard",
					"View Form",
					"Export Data",	
					"View Data",
					};
				setACL(rmRole, accessList);
			}
		}
		{//DO role ACL
			Role doRole = Role.getDORole();
			if (doRole != null) {
				String[] accessList = 
					{
					"Dashboard",
					"View Form",
					"Export Data",	
					"View Data",
					};
				setACL(doRole, accessList);
			}
		}
		{//FS role ACL
			Role fsRole = Role.getFSRole();
			if (fsRole != null) {
				String[] accessList = 
					{
					"Dashboard",
					"View Form",
					"Export Data",	
					"View Data",
					};
				setACL(fsRole, accessList);
			}
		}
		{//FF role ACL
			Role ffRole = Role.getFFRole();
			if (ffRole != null) {
				String[] accessList = 
					{
					"Dashboard",
					"View Form",		
					"View Data",
					};
				setACL(ffRole, accessList);
			}
		}*/
		{//ENUMERATOR role ACL
			Role enumeratorRole = Role.getEnumeratorRole();
			if (enumeratorRole != null) {
				String[] accessList = 
					{
					"Dashboard",
					"View Form",		
					"View Data",
					};
				setACL(enumeratorRole, accessList);
			}
		}
		{//ENUMERATOR role ACL
			Role adminRole = Role.getAdminRole();
			if (adminRole != null) {
				String[] accessList = 
					{
						"Edit User",				
						"View User",
						"ACL",
						"Edit Upazilla",				
						"Edit Union", 				
						"Edit Division",			
						"View Division",
						"Edit District",
						"View Union",
						"View District",
						"View Upazilla",
						"Dashboard",
						"View Form",
						"Edit Form",			
						"Export Data",			
						"View Role",				
						"Edit Role",			
						"Data Approve",		
						"Approved Data List",				
						"Compared Data List", 			
						"Upload CSV", 				
						"Web Entry",
						"View Data",
					};
				setACL(adminRole, accessList);
			}
		}
	
	}
	
	//set access list to a role
	private void setACL(Role role, String[] accessList) {
		if (role != null && accessList != null) {
			for(String name : accessList) {
				Aco aco = Aco.findByName(name);
				if(aco == null) {
					aco = new Aco(name);
					Role admin = Role.findById(1L);
					aco.roles.add(admin);
					aco.save();
				}
				aco.roles.add(role);
				aco.save();
			}
		}
	}
	
}