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
	
	public static void caseReport() {
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();	
		renderArgs.put("geoDivisionList", geoDivisionList);

		List<CaseReport> caseList = CaseReport.findAll();
		render(caseList);
	}

	public static String loadReport(Long formId, Long divisionId,Long districtId, Long upazillaId, Long schoolId, 
			Long studentType, Date startDate, Date endDate) throws SQLException {

		Map<String, Long> mp = new HashMap<String, Long>();
		switch (Integer.parseInt("" + formId)) {

		case 1:
			Logger.info("divisionId : " + divisionId + " districtId : " + districtId + " upazillaId : " + upazillaId
					+ " schoolId : " + schoolId);
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
	
	public static String LoadCaseReport(Long divisionId, Long districtId, Long upazillaId, Long unionId) throws SQLException, ParseException {

		Map<String, String> mp = new HashMap<String, String>();
		
		mp = CaseReport.getReport(divisionId, districtId, upazillaId, unionId);
			
		Gson gson = new Gson();

		return gson.toJson(mp);
	}

}
