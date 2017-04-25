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


	public static String getTotalStudent(Long id) throws SQLException {

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;

		String query = "";
        String totalStudent="";
		query = " SELECT SUM(SchoolInformation.maleStruden) +SUM(SchoolInformation.femailStudent) " +
				" as ItemSum " +
				" from SchoolInformation , Data where Data.schools_id= SchoolInformation.id and Data.id= " + id;

		PreparedStatement queryForExecution1 = conn.prepareStatement(query);
		rs = queryForExecution1.executeQuery();

		try {

			while (rs.next()) {

				totalStudent= rs.getString("ItemSum");
			}
		}catch(Exception e){

			}

		return totalStudent;
	}

	public static String getApprovalStatus(Long id) throws SQLException {

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;

		String query = "";
		String approvalStatus="";
		query = " SELECT Data.approvalStatus \n" +
				"FROM Data\n" +
				"WHERE Data.id=" + id;

		PreparedStatement queryForExecution1 = conn.prepareStatement(query);
		rs = queryForExecution1.executeQuery();

		try {

			while (rs.next()) {

				approvalStatus= rs.getString("approvalStatus");
			}
		}catch(Exception e){

		}

		return approvalStatus;
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
			
		}
		if( (formId != null ) && ( formId== 1 ) )
		{
			formclause = " and Water.form_id = " + formId;
			
			waterquery = waterquery + whereClause + formclause ;
			PreparedStatement queryForExecution1 = conn.prepareStatement(waterquery);
			waterrs = queryForExecution1.executeQuery();
			
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
			
		
		
		return msg;
	}


	public static String sanitationDataDetails(String formId,String dataId) throws SQLException {


		Logger.info(dataId);
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;

		String query = "";
		String msg = "";
		query = "SELECT  NewCrgs.Sanitation.res_type, \n" +
				"        NewCrgs.Sanitation.num_toilet_unusable, \n" +
				"        NewCrgs.Sanitation.why_toilet_unusable,\n" +
				"        NewCrgs.Sanitation.toilet_clean_interval,\n" +
				"        NewCrgs.Sanitation.harpic,\n" +
				"        NewCrgs.Sanitation.is_sanitation_prob_informed,\n" +
				"        NewCrgs.Sanitation.how_informed_sanitation_prob,\n" +
				"        NewCrgs.Sanitation.how_informed_sanitation_prob_other,\n" +
				"        NewCrgs.Sanitation.took_step_sanitation_prob,\n" +
				"        NewCrgs.Sanitation.rank_sanitation\n" +
				"FROM NewCrgs.Sanitation\n" +
				"WHERE NewCrgs.Sanitation.data_id=" +dataId ;

		PreparedStatement queryForExecution = conn.prepareStatement(query);
		rs = queryForExecution.executeQuery();

		String resType=null;
		String numberOfToiletUnusable=null;
		String whyToiletUnusable=null;
		String toiletCleanInterval=null;
		String harpic=null;
		String isSanitationProbInformed=null;
		String howInformedProb=null;
		String howInformedProbOther=null;
		String tookStep=null;
		String rank=null;


		try {

			while (rs.next()) {

				resType=rs.getString("res_type");
				numberOfToiletUnusable=rs.getString("num_toilet_unusable");
				whyToiletUnusable=rs.getString("why_toilet_unusable");
				toiletCleanInterval=rs.getString("toilet_clean_interval");
				harpic=rs.getString("harpic");
				isSanitationProbInformed=rs.getString("is_sanitation_prob_informed");
				howInformedProb=rs.getString("how_informed_sanitation_prob");
				howInformedProbOther=rs.getString("how_informed_sanitation_prob_other");
				tookStep=rs.getString("took_step_sanitation_prob");
				rank=rs.getString("rank_sanitation");

				//msg=msg + ";" + resType ;


             msg=msg + ";" + resType + ";" + numberOfToiletUnusable + ";" + whyToiletUnusable + ";" + toiletCleanInterval +
					   ";" + harpic + ";" + isSanitationProbInformed + ";" + howInformedProb + ";" + howInformedProbOther +
					   ";" + tookStep + ";" + rank;
			}

			/*if( numberOfToiletUnusable!=null){

				msg = msg + ";" + numberOfToiletUnusable;
			}

			else if(numberOfToiletUnusable==null){

				msg = msg + ";" + "no";

			}

			if( whyToiletUnusable!=null){

				msg = msg + ";" + whyToiletUnusable;
			}

			else if(whyToiletUnusable==null){

				msg = msg + ";" + "no";

			}

			if( toiletCleanInterval!=null){

				msg = msg + ";" + toiletCleanInterval;
			}

			else if(toiletCleanInterval==null){

				msg = msg + ";" + "no";

			}

			if( harpic!=null){

				msg = msg + ";" + harpic;
			}

			else if(harpic==null){

				msg = msg + ";" + "no";

			}

			if( isSanitationProbInformed!=null){

				msg = msg + ";" + isSanitationProbInformed;

				if(isSanitationProbInformed.equals("1")){

					msg = msg + ";" + howInformedProb;

					if(howInformedProb.contains("5")){

						msg = msg + ";" + howInformedProbOther ;
					}

					else{

						msg = msg + ";" + "no" ;
					}
				}
			}

			else if(isSanitationProbInformed==null){

				msg = msg + ";" + "no" + ";" + "no" + ";" + "no";

			}


			if( tookStep!=null){

				msg = msg + ";" + tookStep;
			}

			else if(tookStep==null){

				msg = msg + ";" + "no";

			}

			if( rank!=null){

				msg = msg + ";" + rank;
			}

			else if(rank==null){

				msg = msg + ";" + "no";

			}*/
		} catch (Exception e) {



		}

		return msg;
	}

	public static String waterDataDetail(String formId , String dataId) throws SQLException {

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;

		String query = "";
		String msg = "";
		query = "SELECT  Water.res_type, \n" +
				"        Water.water_source, \n" +
				"        Water.num_waterSource,\n" +
				"        Water.activeWaterSource,\n" +
				"        Water.is_potable,\n" +
				"        Water.why_not_potable,\n" +
				"        Water.why_not_potable_other,\n"+
				"        Water.is_tank_cleaned,\n" +
				"        Water.is_informed_authority_water_prob,\n" +
				"        Water.how_informed_water_prob,\n" +
				"        Water.how_informed_water_prob_other,\n" +
				"        Water.water_prob_solved_authority,\n" +
				"        Water.rank_water\n" +
				"FROM Water\n" +
				"WHERE Water.data_id=" +dataId ;

		PreparedStatement queryForExecution = conn.prepareStatement(query);
		rs = queryForExecution.executeQuery();

		String resType=null;
		String waterSource=null;
		String numWaterSource=null;
		String activeWaterSource=null;
		String isPotable=null;
		String whyNotPotable=null;
		String isTankCleaned=null;
		String isInformedAuthorityWaterProb=null;
		String whyNotPotableOther=null;
		String howInformedWaterProb=null;
		String howInformedWaterProbOther=null;
		String waterProbSolvedAuthority=null;
		String rank=null;

		try {

			while (rs.next()) {

				resType = rs.getString("res_type");
				waterSource = rs.getString("water_source");
				numWaterSource = rs.getString("num_waterSource");
				activeWaterSource = rs.getString("activeWaterSource");
				isPotable = rs.getString("is_potable");
				whyNotPotable = rs.getString("why_not_potable");
				whyNotPotableOther = rs.getString("why_not_potable_other");
				isTankCleaned = rs.getString("is_tank_cleaned");
				isInformedAuthorityWaterProb = rs.getString("is_informed_authority_water_prob");
				howInformedWaterProb = rs.getString("how_informed_water_prob");
				howInformedWaterProbOther = rs.getString("how_informed_water_prob_other");
				waterProbSolvedAuthority = rs.getString("water_prob_solved_authority");
				rank = rs.getString("rank_water");

				Logger.info("rank: "+rank);

				//msg = msg + ";" + resType;


            msg=msg + ";" + resType + ";" + waterSource + ";" + numWaterSource + ";" + activeWaterSource +
					   ";" + isPotable + ";" + whyNotPotable + ";" + whyNotPotableOther + ";" + isTankCleaned +
					   ";" + isInformedAuthorityWaterProb +  ";" + howInformedWaterProb + ";" + howInformedWaterProbOther + ";" + waterProbSolvedAuthority + ";" + rank;



			}

		}catch(Exception e){



		}

			return msg;
		}

	public static String schoolEnviornmentDataDetails(String formId , String dataId) throws SQLException {

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;

		String query = "";
		String msg = "";

		query="SELECT " +
				"SchoolEnvironment.res_type, " +
				"SchoolEnvironment.cleanInterval_SchoolYard, " +
				"SchoolEnvironment.cleanInterval_ClassRoom, " +
				"SchoolEnvironment.StuHearTeacher, " +
				"SchoolEnvironment.stuSeatArrange, " +
				"SchoolEnvironment.teacherStage, " +
				"SchoolEnvironment.complained_SchoolEnvironment, " +
				"SchoolEnvironment.how_informed_Environment_prob, " +
				"SchoolEnvironment.how_informed_Environment_prob_other, " +
				"SchoolEnvironment.took_step_Environment_prob, " +
				"SchoolEnvironment.rank_Environment, " +
				"SchoolEnvironment.rank_Edu_Quality, " +
				"SchoolEnvironment.scareSafe_SchoolWay, " +
				"SchoolEnvironment.yhy_not_feel_safe, " +
				"SchoolEnvironment.schoolCanteen " +
				"FROM " +
				"SchoolEnvironment "+
				"WHERE SchoolEnvironment.data_id=" +dataId ;

		PreparedStatement queryForExecution = conn.prepareStatement(query);
		rs = queryForExecution.executeQuery();

		String resType=null;
		String cleanIntervalSchoolYard=null;
		String cleanIntervalClassRoom=null;
		String stuHearTeacher=null;
		String stuSeatArrange=null;
		String teacherStage=null;
		String complainedSchoolEnvironment=null;
		String howInformedEnvironmentProb=null;
		String howInformedEnvironmentProbOther=null;
		String tookStepEnvironmentProb=null;
		String rankEnvironment=null;
		String rankEduQuality=null;
		String scareSafeSchoolWay=null;
		String yhyNotFeelSage=null;
		String schoolCanteen=null;

		try{

			while(rs.next()){

				resType = rs.getString("res_type");
				cleanIntervalSchoolYard = rs.getString("cleanInterval_SchoolYard");
				cleanIntervalClassRoom = rs.getString("cleanInterval_ClassRoom");
				stuHearTeacher = rs.getString("StuHearTeacher");
				stuSeatArrange = rs.getString("stuSeatArrange");
				teacherStage = rs.getString("teacherStage");
				complainedSchoolEnvironment = rs.getString("complained_SchoolEnvironment");
				howInformedEnvironmentProb = rs.getString("how_informed_Environment_prob");
				howInformedEnvironmentProbOther = rs.getString("how_informed_Environment_prob_other");
				tookStepEnvironmentProb = rs.getString("took_step_Environment_prob");
				rankEnvironment = rs.getString("rank_Environment");
				rankEduQuality = rs.getString("rank_Edu_Quality");
				scareSafeSchoolWay = rs.getString("scareSafe_SchoolWay");
				yhyNotFeelSage = rs.getString("yhy_not_feel_safe");
				schoolCanteen = rs.getString("schoolCanteen");


			}

			msg=msg + ";" + resType + ";" + cleanIntervalSchoolYard + ";" + cleanIntervalClassRoom + ";" + stuHearTeacher +
					  ";" + stuSeatArrange + ";" + teacherStage + ";" + complainedSchoolEnvironment + ";" + howInformedEnvironmentProb +
					  ";" + howInformedEnvironmentProbOther +  ";" + tookStepEnvironmentProb + ";" + rankEnvironment +
					  ";" + rankEduQuality + ";" + scareSafeSchoolWay + ";" + "," + yhyNotFeelSage + ";" + schoolCanteen;

			Logger.info("teacher: " + stuHearTeacher);


		}catch(Exception e){

		}



		return msg;
	}

	public static String schoolSportsDataDetails(String formId , String dataId) throws SQLException {

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;

		String query = "";
		String msg = "";

		query="SELECT \n" +
				"SportsRecreation.res_type,\n" +
				"SportsRecreation.facilitiesAvailable,\n" +
				"SportsRecreation.instrumentUsable,\n" +
				"SportsRecreation.schoolType,\n" +
				"SportsRecreation.instrumentEqualAccess,\n" +
				"SportsRecreation.whyNot_EqualAccess,\n" +
				"SportsRecreation.sportsTeacher,\n" +
				"SportsRecreation.lastMonth_Activity,\n" +
				"SportsRecreation.rank_SportsRecreation\n" +
				"FROM\n" +
				"SportsRecreation "+
				"WHERE SportsRecreation.data_id=" +dataId ;

		PreparedStatement queryForExecution = conn.prepareStatement(query);
		rs = queryForExecution.executeQuery();

		String resType=null;
		String facilitiesAvailable=null;
		String instrumentUsable=null;
		String schoolType=null;
		String instrumentEqualAccess=null;
		String whyNotEqualAccess=null;
		String sportsTeacher=null;
		String lastMonthActivity=null;
		String rankSportsRecreation=null;


		try{

			while(rs.next()){

				resType = rs.getString("res_type");
				facilitiesAvailable = rs.getString("facilitiesAvailable");
				instrumentUsable = rs.getString("instrumentUsable");
				schoolType = rs.getString("schoolType");
				instrumentEqualAccess = rs.getString("instrumentEqualAccess");
				whyNotEqualAccess = rs.getString("whyNot_EqualAccess");
				sportsTeacher = rs.getString("sportsTeacher");
				lastMonthActivity = rs.getString("lastMonth_Activity");
				rankSportsRecreation = rs.getString("rank_SportsRecreation");



			}

			msg=msg + ";" + resType + ";" + "," +facilitiesAvailable + ";" + instrumentUsable + ";" + schoolType +
					";" + instrumentEqualAccess + ";" + "," +whyNotEqualAccess + ";" + sportsTeacher + ";" + "," +lastMonthActivity +
					";" + rankSportsRecreation ;


		}catch(Exception e){

		}



		return msg;
	}
}
