package controllers;

import groovy.ui.Console;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeValueExp;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xpath.internal.operations.And;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import models.CaseReport;
import models.Data;
import models.GeoDistrict;
import models.GeoDivision;
import models.GeoUpazilla;
import models.OverallReport;
import models.PollDefination;
import models.PollVoteReply;
import models.Sanitation;
import models.SchoolEnvironment;
import models.SchoolInformation;
import models.SportsRecreation;
import models.UnitData;
import models.User;
import models.Water;
import play.Logger;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPA;
import play.mvc.With;
import sun.util.logging.resources.logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@With(Deadbolt.class)
public class Reports extends Controller {
	
	//static int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
	//static int CurrentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
	
	public static void overallReport(){
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		renderArgs.put("geoDivisionList", geoDivisionList);
		render();
	}

	public static void waterReport() throws SQLException {

		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		renderArgs.put("geoDivisionList", geoDivisionList);
		render();
	}

	public static void sanitationReport() {
		Logger.info("sanitationReport report");
		

		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		renderArgs.put("geoDivisionList", geoDivisionList);
		

		render();
	}

	public static void schoolEnvironmentReport() {


		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		renderArgs.put("geoDivisionList", geoDivisionList);
	
		
		render();
	}

	public static void sportsAndRecreationReport() {


		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		
		renderArgs.put("geoDivisionList", geoDivisionList);
		

		render();
	}
	
	@ExternalRestrictions("View Case")
	public static void caseReport() {
		
		/*List<String> caseType = new ArrayList<String>();
		
		caseType.add(0,"Case Type");
		caseType.add( 1,"Rape");
		caseType.add( 2,"Mental Torture");
		caseType.add( 3,"Physical Assault");
		caseType.add( 4,"Sexual Assault");
		caseType.add( 5,"Forced Prostitution");
		caseType.add( 6,"Influenced Suicide");
		caseType.add( 7,"Abduction");
		caseType.add( 8,"Forced/Underaged Marriage");
		caseType.add( 9,"Threat");
		caseType.add( 10,"Acid Violence");
		caseType.add( 11,"Domestic Violence");
		caseType.add( 12,"Physical/Corporal & Mental Punishment");
		caseType.add( 13,"Other");*/
		
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();	
		//List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
	   // List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
	    
		//renderArgs.put("geoDistrictList", geoDistrictList);
		//renderArgs.put("geoUpazillaList", geoUpazillaList);
		renderArgs.put("geoDivisionList", geoDivisionList);
       // renderArgs.put("caseType", caseType);
		List<CaseReport> caseList = CaseReport.findAll();
		render(caseList);
	}

	public static String loadReport(Long formId, Long divisionId,Long districtId, Long upazillaId, Long schoolId, 
			Long studentType, String startDatee, String endDatee) throws SQLException {

		Map<String, Long> mp = new HashMap<String, Long>();



		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = new Date();
		Date endDate = new Date();


		Logger.info("startDatee: " + startDatee + "  endDatee: "+ endDatee);

		if(startDatee.length() == 0 || endDatee.length()==0){

			startDate = null;
			endDate = null;

		}

		if(startDatee.length() != 0 && endDatee.length() != 0 ){
			try {
				Logger.info("check");
				startDate = sdf.parse(startDatee);
				endDate = sdf.parse(endDatee);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Logger.info("rstartDate: " + startDate + "rendDate: "+ endDate);
		switch (Integer.parseInt("" + formId)) {

		case 1:
			Logger.info("rrrrrrrrrrrrrrrrrrrrrr: "+"divisionId : " + divisionId + " districtId : " + districtId + " upazillaId : " + upazillaId
					+ " schoolId : " + schoolId  + " startDate: " + startDate + " endDate: " + endDate);
			mp = Water.getWaterData(divisionId, districtId, upazillaId, schoolId, studentType, startDate, endDate);
			break;

		case 2:
			mp = Sanitation.getSanitationData(divisionId, districtId, upazillaId, schoolId, studentType, startDate, endDate);
			break;

		case 3:
			mp = SchoolEnvironment.getSchoolEnvironmentData(divisionId, districtId, upazillaId, schoolId, studentType, startDate, endDate);
			break;

		case 4:
			mp = SportsRecreation.getSportsRecreationData(divisionId, districtId, upazillaId, schoolId, studentType, startDate, endDate);
			break;
			
		default:
			break;
		}

		Gson gson = new Gson();

		return gson.toJson(mp);
	}
	
	public static String firstChartInOverallReport(Long divisionId,Long districtId, Long upazillaId, Long schoolId, 
			Long studentType, Date startDate, Date endDate) throws SQLException, ParseException {
		
		Map<String, String> mp = new HashMap<String, String>();
		
		
		
		mp = OverallReport.firstChartInOverallReport(divisionId, districtId, upazillaId, schoolId, studentType, startDate, endDate);
		

		Gson gson = new Gson();
		return gson.toJson(mp);
	}
	
	public static String secondChartInOverallReport(Long divisionId, Long districtId, Long upazillaId, Long schoolId,
			Long studentType, Date startDate, Date endDate, Long formId, Boolean divisionFilter, Boolean districtFilter,
			Boolean upazillaFilter, Boolean schoolFilter) throws SQLException, ParseException {

		Map<String, String> mp = new HashMap<String, String>();
		
		
		
		mp = OverallReport.secondChartInOverallReport(divisionId, districtId, upazillaId, schoolId, 
				studentType, startDate, endDate, formId, divisionFilter, districtFilter, upazillaFilter, schoolFilter);
			
		Gson gson = new Gson();

		return gson.toJson(mp);
	}
	
	public static String LoadCaseReport(Long divisionId, Long districtId, Long upazillaId, Long unionId ,int caseType) throws SQLException {

		String mp = "";
		
		mp = CaseReport.getCaseReport(divisionId, districtId, upazillaId, unionId , caseType);
			
		Gson gson = new Gson();

		return gson.toJson(mp);
	}
	
	public static void voteReply(String gender, String option, String age,String pollId){

			Logger.info("PollId: " + pollId);

			//Long pollid= Long.parseLong(pollId);
			PollDefination polldef = PollDefination.findById(pollId);

			PollVoteReply pollvotereply = new PollVoteReply();

			pollvotereply.gender=gender;
			pollvotereply.age=age;
			//pollvotereply.poll=polldef;
			pollvotereply.answer=option;

			Logger.info("optn: " +  option);
			pollvotereply.save();


			try {
				if(session.get("username") != null){

					Forms.landingPage();
				}
				else{

					Secure.login();
				}

			}catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



			//Forms.landingPage();
		}

	public static String loadPoll() throws SQLException {

		String mp = "";

		mp = PollDefination.getPollReport();

		//Logger.info("mp:" + mp);

		Gson gson = new Gson();

		return gson.toJson(mp);
	}


	public static String getWaterDataDetails(String formId,String dataId) throws SQLException {

		String msg = "";

		msg=Data.waterDataDetail(formId,dataId);


		Gson gson = new Gson();

		return gson.toJson(msg);
	}

	public static String getSanitationDataDetails(String formId,String dataId) throws SQLException {

		Logger.info("hit!: " + formId + " " + dataId);

		String msg = "";

          msg=Data.sanitationDataDetails(formId,dataId);


		Gson gson = new Gson();

		return gson.toJson(msg);
	}

	public static String getEnviornmentDataDetails(String formId,String dataId) throws SQLException {


		Logger.info("hittttt");


		String msg = "";

		msg=Data.schoolEnviornmentDataDetails(formId,dataId);


		Gson gson = new Gson();

		return gson.toJson(msg);
	}

	public static String getSportsDataDetails(String formId,String dataId) throws SQLException {


		Logger.info("hittttt");


		String msg = "";

		msg=Data.schoolSportsDataDetails(formId,dataId);


		Gson gson = new Gson();

		return gson.toJson(msg);

	}
	

}
