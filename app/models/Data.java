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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;

import play.Logger;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import play.mvc.Util;

/**
 * Data Model - Definition of survey form data.
 */
@Entity
public class Data extends Model {
	
	/**ID to identify questions those are submitted in a single request.*/
	
	@Required
	public String submissionId;

	/**Tag name of a group question.*/
	public String groupTagName;
	
	public String respondentId;
	
	/**CRGS School principle activity is recorded Here by using this fields */
	public Boolean isVisited;
	public Date resolveDate;
	
	@ManyToOne
	public SchoolInformation schools;
	
	@ManyToOne
	public Ngo ngo;
	
	/*@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	public Data comparedTo;*/
	
	/**Iterator for parsing group question.*/
	public int iteration = 1;
	
	public int approvalStatus;
	
	public int approvalState;
	
	/** If farmer will not have any activity then status would be false, otherwise true */
	public Boolean isFarmerActive = true;
	
	/** The form. */
	@Required
	@ManyToOne
    public Form form;

	/** The xml. */
	//@Required
	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] xml;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] dataSpecificXml;

	@Required
	@ManyToOne
	public User sender;
	
	@Required
	@ManyToOne
	public Role senderRole;
	
	public Boolean webEntry = false;
	
	/** The items, will not be persist. Will be used in View*/
	@Transient
	public List items;

	/** The received. */
	public Date received;

	public Date startTime;
	public Date endTime;

	public String[] audioData;

	// Extra Data for Dashboard
	public Double latitude;
	public Double longitude;
	public Double accuracy;
	public String image;
	public Boolean isExtracted = false;
	public String webOrMob = "";
	
	
	

	/**
	 * Instantiates a new data.
	 *
	 * @param form the form
	 * @param xml the xml
	 */
	
	
	public Data(Form form, byte[] xml) {
		this.form = form;
		this.xml = xml;
	}
	
	public static List<Data> findByRoleName(Role role){
		
		return Data.find("bySenderRole", role).fetch();
	}
	
	public static List<Data> findByFormId(Form form){
		
		return Data.find("byForm", form).fetch();
	}


	/**
	 * Before insert.
	 */
	@PrePersist
    protected void beforeInsert() {
        this.received = new Date();
        this.form.lastReceived = this.received;
        this.form.dataCount++;
    }

	/**
	 * Before remove.
	 */
	@PreRemove
    protected void beforeRemove() {
        this.form.dataCount--;
    }
	
	public int countData(){
		//Data keyData = Data.findById(id);
		List<Data> data = Data.findAll();
		return data.size();
	}
	
	
	public static String getDataReport(Long divisionId,Long districtId, Long upazillaId, Long schoolId ,Long formId) throws SQLException {
		
	
		String msg="";
		String waterquery = null ;
		String sanitationquery = null;
		String schoolenvrmentquery = null ;
		String schoolsportsquery = null;
		Connection conn = play.db.DB.getConnection();
		ResultSet waterrs = null;
		ResultSet sanitationrs = null;
		ResultSet envrmentrs = null;
		ResultSet sportsrs = null;
		String schoolid=null;
		String schoolname=null;
		String address=null;
		String createdat=null;
		String displayname=null;
		String shortname=null;
		String approvalstatus=null;
		String dataId = null ;
		
		
		waterquery = " SELECT Data.id as data_id, SchoolInformation.schoolRegNo as schoolId , " +
				" SchoolInformation.name as schoolName , " + 
				" GeoUpazilla.name as Address , " +
				" Water.created_at , " +
				" User.displayName, " +
				" Form.shortName ,  " +
				" Data.approvalStatus " +
				" FROM Water " +
				" JOIN SchoolInformation ON Water.school_id = SchoolInformation.id " +
				" JOIN GeoUpazilla ON SchoolInformation.geoupazilla_id = GeoUpazilla.id " +
				" JOIN User ON Water.sender_id = User.id " +
				" JOIN Form ON Form.id = Water.form_id " +
				" JOIN Data ON Data.id = Water.data_id " ;
		
		sanitationquery = " SELECT Data.id as data_id, SchoolInformation.schoolRegNo as schoolId , " +
				" SchoolInformation.name as schoolName , " + 
				" GeoUpazilla.name as Address , " +
				" Sanitation.created_at , " +
				" User.displayName, " +
				" Form.shortName ,  " +
				" Data.approvalStatus " +
				" FROM Sanitation " +
				" JOIN SchoolInformation ON Sanitation.school_id = SchoolInformation.id " +
				" JOIN GeoUpazilla ON SchoolInformation.geoupazilla_id = GeoUpazilla.id " +
				" JOIN User ON Sanitation.sender_id = User.id " +
				" JOIN Form ON Form.id = Sanitation.form_id " +
				" JOIN Data ON Data.id = Sanitation.data_id " ;
		
		schoolenvrmentquery = " SELECT Data.id as data_id, SchoolInformation.schoolRegNo as schoolId , " +
				" SchoolInformation.name as schoolName , " + 
				" GeoUpazilla.name as Address , " +
				" SchoolEnvironment.created_at , " +
				" User.displayName, " +
				" Form.shortName ,  " +
				" Data.approvalStatus " +
				" FROM SchoolEnvironment " +
				" JOIN SchoolInformation ON SchoolEnvironment.school_id = SchoolInformation.id " +
				" JOIN GeoUpazilla ON SchoolInformation.geoupazilla_id = GeoUpazilla.id " +
				" JOIN User ON SchoolEnvironment.sender_id = User.id " +
				" JOIN Form ON Form.id = SchoolEnvironment.form_id " +
				" JOIN Data ON Data.id = SchoolEnvironment.data_id " ;
		
		schoolsportsquery = " SELECT Data.id as data_id, SchoolInformation.schoolRegNo as schoolId , " +
				" SchoolInformation.name as schoolName , " + 
				" GeoUpazilla.name as Address , " +
				" SportsRecreation.created_at , " +
				" User.displayName, " +
				" Form.shortName ,  " +
				" Data.approvalStatus " +
				" FROM SportsRecreation " +
				" JOIN SchoolInformation ON SportsRecreation.school_id = SchoolInformation.id " +
				" JOIN GeoUpazilla ON SchoolInformation.geoupazilla_id = GeoUpazilla.id " +
				" JOIN User ON SportsRecreation.sender_id = User.id " +
				" JOIN Form ON Form.id = SportsRecreation.form_id " +
				" JOIN Data ON Data.id = SportsRecreation.data_id " ;
		
		
		String whereClause = "  ";
		String formclause = " ";

		if (divisionId != null) {

			whereClause += " WHERE SchoolInformation.geoDivision_id = " + divisionId;

		}
		if (districtId != null) {

			whereClause += " and SchoolInformation.geoDistrict_id = " + districtId;

		}
		if (upazillaId != null) {

			whereClause += " and SchoolInformation.geoUpazilla_id = " + upazillaId;

		}
		if (schoolId != null) {

			whereClause += " and SchoolInformation.id = " + schoolId;

		}
		
		if(formId==null){
			
			waterquery = waterquery + whereClause + formclause ;
			sanitationquery = sanitationquery + whereClause + formclause ;
			schoolenvrmentquery = schoolenvrmentquery + whereClause + formclause ;
			schoolsportsquery = schoolsportsquery + whereClause + formclause ;
			
			PreparedStatement queryForExecution1 = conn.prepareStatement(waterquery);
			PreparedStatement queryForExecution2 = conn.prepareStatement(sanitationquery);
			PreparedStatement queryForExecution3 = conn.prepareStatement(schoolenvrmentquery);
			PreparedStatement queryForExecution4 = conn.prepareStatement(schoolsportsquery);
			
			/*Logger.info("waterquery: " + waterquery);
			Logger.info("sanitationquery: " +  sanitationquery);
			Logger.info("schoolenvrmentquery: " + schoolenvrmentquery);
			Logger.info("schoolsportsquery: " +  schoolsportsquery);*/
			
			waterrs = queryForExecution1.executeQuery();
			sanitationrs = queryForExecution2.executeQuery();
			envrmentrs = queryForExecution3.executeQuery();
			sportsrs = queryForExecution4.executeQuery();	
			
			try {

				while (waterrs.next()) {
					
                    dataId = waterrs.getString("data_id");
					schoolid = waterrs.getString("schoolId");
					schoolname = waterrs.getString("schoolName");
					address = waterrs.getString("Address") ;
					createdat = waterrs.getString("created_at") ;
					displayname = waterrs.getString("displayName") ;
					shortname = waterrs.getString("shortName") ;
					approvalstatus = waterrs.getString("approvalStatus") ;
					
					msg = msg + ";" +  dataId + ";" + schoolid + ";" + schoolname + ";" + address + ";" + createdat + ";" + displayname + ";" + shortname + ";" + approvalstatus ;
					//count++;
				}
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			
			
			try {

				while (sanitationrs.next()) {
 
					dataId = sanitationrs.getString("data_id");
					schoolid = sanitationrs.getString("schoolId");
					schoolname = sanitationrs.getString("schoolName");
					address = sanitationrs.getString("Address") ;
					createdat = sanitationrs.getString("created_at") ;
					displayname = sanitationrs.getString("displayName") ;
					shortname = sanitationrs.getString("shortName") ;
					approvalstatus = sanitationrs.getString("approvalStatus") ;
					
					msg = msg + ";" +  dataId + ";" + schoolid + ";" + schoolname + ";" + address + ";" + createdat + ";" + displayname + ";" + shortname + ";" + approvalstatus ;
					//count++;
				}
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
		
			try {

				while (envrmentrs.next()) {
                    
					dataId = envrmentrs.getString("data_id");
					schoolid = envrmentrs.getString("schoolId");
					schoolname = envrmentrs.getString("schoolName");
					address = envrmentrs.getString("Address") ;
					createdat = envrmentrs.getString("created_at") ;
					displayname = envrmentrs.getString("displayName") ;
					shortname = envrmentrs.getString("shortName") ;
					approvalstatus = envrmentrs.getString("approvalStatus") ;
					
					msg = msg + ";" +  dataId + ";" + schoolid + ";" + schoolname + ";" + address + ";" + createdat + ";" + displayname + ";" + shortname + ";" + approvalstatus ;
					//count++;
				}
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			
			
			try {

				while (sportsrs.next()) {
                    
					dataId = sportsrs.getString("data_id");
					schoolid = sportsrs.getString("schoolId");
					schoolname = sportsrs.getString("schoolName");
					address = sportsrs.getString("Address") ;
					createdat = sportsrs.getString("created_at") ;
					displayname = sportsrs.getString("displayName") ;
					shortname = sportsrs.getString("shortName") ;
					approvalstatus = sportsrs.getString("approvalStatus") ;
					
					msg = msg + ";" +  dataId + ";" + schoolid + ";" + schoolname + ";" + address + ";" + createdat + ";" + displayname + ";" + shortname + ";" + approvalstatus ;
					//count++;
				}
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			
			
			
			
		}
		
		else {
			
		
		if( (formId != null ) && ( formId== 1 ) )
		
		{
			formclause = " and Water.form_id = " + formId;
			
			waterquery = waterquery + whereClause + formclause ;
			PreparedStatement queryForExecution1 = conn.prepareStatement(waterquery);
			waterrs = queryForExecution1.executeQuery();
			
			Logger.info("water: " + waterquery);
			
			try {

				while (waterrs.next()) {
					
                    dataId = waterrs.getString("data_id");
					schoolid = waterrs.getString("schoolId");
					schoolname = waterrs.getString("schoolName");
					address = waterrs.getString("Address") ;
					createdat = waterrs.getString("created_at") ;
					displayname = waterrs.getString("displayName") ;
					shortname = waterrs.getString("shortName") ;
					approvalstatus = waterrs.getString("approvalStatus") ;
					
					msg = msg + ";" +  dataId + ";" + schoolid + ";" + schoolname + ";" + address + ";" + createdat + ";" + displayname + ";" + shortname + ";" + approvalstatus ;
					//count++;
				}
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			
		}
		
		else if( (formId != null ) && ( formId== 2 ) )
		{
			formclause = " and Sanitation.form_id = " + formId;
			
			sanitationquery = sanitationquery + whereClause + formclause ;
			PreparedStatement queryForExecution2 = conn.prepareStatement(sanitationquery);
			sanitationrs = queryForExecution2.executeQuery();
			
			try {

				while (sanitationrs.next()) {
 
					dataId = sanitationrs.getString("data_id");
					schoolid = sanitationrs.getString("schoolId");
					schoolname = sanitationrs.getString("schoolName");
					address = sanitationrs.getString("Address") ;
					createdat = sanitationrs.getString("created_at") ;
					displayname = sanitationrs.getString("displayName") ;
					shortname = sanitationrs.getString("shortName") ;
					approvalstatus = sanitationrs.getString("approvalStatus") ;
					
					msg = msg + ";" +  dataId + ";" + schoolid + ";" + schoolname + ";" + address + ";" + createdat + ";" + displayname + ";" + shortname + ";" + approvalstatus ;
					//count++;
				}
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
		}
		
		
		else if( (formId != null ) && ( formId== 3 ) )
		{
			formclause = " and SchoolEnvironment.form_id = " + formId;
			schoolenvrmentquery = schoolenvrmentquery + whereClause + formclause ;
			PreparedStatement queryForExecution3 = conn.prepareStatement(schoolenvrmentquery);
			envrmentrs = queryForExecution3.executeQuery();
			
			try {

				while (envrmentrs.next()) {
                    
					dataId = envrmentrs.getString("data_id");
					schoolid = envrmentrs.getString("schoolId");
					schoolname = envrmentrs.getString("schoolName");
					address = envrmentrs.getString("Address") ;
					createdat = envrmentrs.getString("created_at") ;
					displayname = envrmentrs.getString("displayName") ;
					shortname = envrmentrs.getString("shortName") ;
					approvalstatus = envrmentrs.getString("approvalStatus") ;
					
					msg = msg + ";" +  dataId + ";" + schoolid + ";" + schoolname + ";" + address + ";" + createdat + ";" + displayname + ";" + shortname + ";" + approvalstatus ;
					//count++;
				}
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
		}
		
		else if( (formId != null ) && ( formId== 4 ) )
		{
			formclause = " and SportsRecreation.form_id = " + formId;
			schoolsportsquery = schoolsportsquery + whereClause + formclause ;
			PreparedStatement queryForExecution4 = conn.prepareStatement(schoolsportsquery);
			sportsrs = queryForExecution4.executeQuery();
			
			try {

				while (sportsrs.next()) {
                    
					dataId = sportsrs.getString("data_id");
					schoolid = sportsrs.getString("schoolId");
					schoolname = sportsrs.getString("schoolName");
					address = sportsrs.getString("Address") ;
					createdat = sportsrs.getString("created_at") ;
					displayname = sportsrs.getString("displayName") ;
					shortname = sportsrs.getString("shortName") ;
					approvalstatus = sportsrs.getString("approvalStatus") ;
					
					msg = msg + ";" +  dataId + ";" + schoolid + ";" + schoolname + ";" + address + ";" + createdat + ";" + displayname + ";" + shortname + ";" + approvalstatus ;
					//count++;
				}
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			
			
			
		}
			
		}
		
		return msg;
	}
}
