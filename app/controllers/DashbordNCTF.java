package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import models.Data;
import models.Form;
import models.SchoolInformation;
import models.User;
import play.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

//import controllers.Reports.schoolInfo;

public class DashbordNCTF extends Controller{
	
	public static void index() {
		List<Form> forms = Form.findAll();
        
        int headSelector = 1;
        
        /*String queryTotalSchool = "SELECT count(*) as school_count FROM SchoolInformation";*/
        String querybaseLineInfo  = "select count(*) as school_count,sum(totalToilets) as totalToilets,ceil(sum(femaleToiletRatio)/count(*)) as femaleToiletRatio from SchoolInformation";
        Logger.info("querybaseLineInfo"+querybaseLineInfo );
//		String queryToiletUsability = "SELECT sum(num_toilet__1) as total_toilet,sum(num_toilet_unusable__1) as unusable_toilet FROM tblFlat";
		String queryToiletUsability = "SELECT sum(num_toilet__1) as total_toilet,sum(num_toilet_unusable__1) as unusable_toilet FROM vwFlatToilet";
		
		// TODO get data from SchoolInformation once model is updated
		String queryToiletGirlsRatio = "";
		String queryToiletDisableFriendly = "SELECT count(is_toilet_usable_differentlyAble__1) as disabled FROM vwFlatToilet where is_toilet_usable_differentlyAble__1 = 1 ";
		String querySoapHandwash= "SELECT (SELECT count(soap_handwash__1) FROM tblFlat where soap_handwash__1=1) as soap_handwash_none, "+
									"(SELECT count(soap_handwash__1) FROM tblFlat where soap_handwash__1=2) as soap_handwash_mostly_none, "+
									"(SELECT count(soap_handwash__1) FROM tblFlat where soap_handwash__1=3) as soap_handwash_half, "+
									"(SELECT count(soap_handwash__1) FROM tblFlat where soap_handwash__1=4) as soap_handwash_mostly_some, "+
									"(SELECT count(soap_handwash__1) FROM tblFlat where soap_handwash__1=5) as soap_handwash_all";
		
		String queryTissue = "SELECT (SELECT count(toilet_tissue__1) FROM tblFlat where toilet_tissue__1=1) as tissue_none, "+
								"(SELECT count(toilet_tissue__1) FROM tblFlat where toilet_tissue__1=2) as tissue_mostly_none, "+
								"(SELECT count(toilet_tissue__1) FROM tblFlat where toilet_tissue__1=3) as tissue_half, "+
								"(SELECT count(toilet_tissue__1) FROM tblFlat where toilet_tissue__1=4) as tissue_mostly_some, "+
								"(SELECT count(toilet_tissue__1) FROM tblFlat where toilet_tissue__1=5) as tissue_all";
		
		String queryDustbin = "SELECT (SELECT count(toilet_dustbin__1) FROM tblFlat where toilet_dustbin__1=1) as dustbin_none, " +
								"(SELECT count(toilet_dustbin__1) FROM tblFlat where toilet_dustbin__1=2) as dustbin_mostly_none, " +
								"(SELECT count(toilet_dustbin__1) FROM tblFlat where toilet_dustbin__1=3) as dustbin_half, " +
								"(SELECT count(toilet_dustbin__1) FROM tblFlat where toilet_dustbin__1=4) as dustbin_mostly_some, " +
								"(SELECT count(toilet_dustbin__1) FROM tblFlat where toilet_dustbin__1=5) as dustbin_all";
		
		String querySandal = "SELECT (SELECT count(sandal__1) FROM tblFlat where sandal__1=1) as sandal_none, "+
								"(SELECT count(sandal__1) FROM tblFlat where sandal__1=2) as sandal_mostly_none, "+
								"(SELECT count(sandal__1) FROM tblFlat where sandal__1=3) as sandal_half, "+
								"(SELECT count(sandal__1) FROM tblFlat where sandal__1=4) as sandal_mostly_some, "+
								"(SELECT count(sandal__1) FROM tblFlat where sandal__1=5) as sandal_all";
		
		String queryMug = "SELECT (SELECT count(mug__1) FROM tblFlat where mug__1=1) as mug_none,"+
							"(SELECT count(mug__1) FROM tblFlat where mug__1=2) as mug_mostly_none,"+
							"(SELECT count(mug__1) FROM tblFlat where mug__1=3) as mug_half,"+
							"(SELECT count(mug__1) FROM tblFlat where mug__1=4) as mug_mostly_some,"+
							"(SELECT count(mug__1) FROM tblFlat where mug__1=5) as mug_all";
		
		String queryInformedWater = "SELECT count(is_informed_authority_water_prob__1) AS informed_water_prob FROM tblFlat where is_informed_authority_water_prob__1 = 1;";
		String queryInformedHygene = "SELECT count(is_informed_hygiene__1 ) AS informed_hygiene_prob FROM tblFlat where is_informed_hygiene__1= 1";
		String queryInformedSanitaion = "SELECT count(is_sanitation_prob_informed__1 ) AS informed_sanitation_prob From tblFlat where is_sanitation_prob_informed__1 = 1";
		String queryWaterProbSolved = "Select count(water_prob_solved_authority__1) AS water_prob_solved From tblFlat where water_prob_solved_authority__1 IN (1,2,3,4,5)";
		String queryHygeneProbSolved = "Select count(authority_solve_hygiene_prob__1) AS hygiene_prob_solved From tblFlat where authority_solve_hygiene_prob__1 IN (1,2,3,4,5)";
		String querySanitaionProbSolved = "Select count(took_step_sanitation_prob__1) AS sanitation_prob_solved From tblFlat where took_step_sanitation_prob__1 IN (1,2,3,4,5)";
		//Causes behind unsafe water
		String queryCausesBehindUnsafeWater = "SELECT count(why_not_potable_1__1) as badOdor,"
				+ "count(why_not_potable_2__1) as iron,count(why_not_potable_3__1) as mnOrAs,"
				+ "count(why_not_potable_4__1) as other FROM tblFlat";
		
		String queryInactiveToiletReason = "";
		queryInactiveToiletReason = "SELECT "+
		"count(why_toilet_unusable_1__1) as WaterTabBroken,"+ 
		"count(why_toilet_unusable_2__1) as WaterSourcesFarAway,"+
		"count(why_toilet_unusable_3__1) as NoWater,"+
		"count(why_toilet_unusable_4__1) as NoLock,"+
		"count(why_toilet_unusable_5__1) as BrokenDoor,"+
		"count(why_toilet_unusable_6__1) as NoWaterContainer,"+
		"count(why_toilet_unusable_7__1) as OverflowWater,"+
		"count(why_toilet_unusable_8__1) as WaterClogging,"+
		"count(why_toilet_unusable_9__1) as NoDustbin,"+
		"count(why_toilet_unusable_10__1) as NoLight,"+
		"count(why_toilet_unusable_11__1) as Insects,"+
		"count(why_toilet_unusable_12__1) as BadSmell,"+
		"count(why_toilet_unusable_13__1) as Other "+
		"FROM tblFlat ";
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		
		String totalSchool= null, totalToilet = null, unusableToilet = null, femaleToiletRatio = null, disabledFriendly = null;
		String soapHandwashNone = null ,soapHandwashMostlyNone= null ,soapHandwashHalf= null ,soapHandwashMostlySome= null ,soapHandwashAll= null ;
		String tissueNone = null ,tissueMostlyNone= null ,tissueHalf= null ,tissueMostlySome= null ,tissueAll= null ;
		String dustbinNone = null ,dustbinMostlyNone= null ,dustbinHalf= null ,dustbinMostlySome= null ,dustbinAll= null ;
		String sandalNone = null ,sandalMostlyNone= null ,sandalHalf= null ,sandalMostlySome= null ,sandalAll= null ;
		String mugNone = null ,mugMostlyNone= null ,mugHalf= null ,mugMostlySome= null ,mugAll= null ;
		String waterProbCount = null, waterProbSolvedCount = null,sanitaionProbCount = null, sanitaionProbSolvedCount = null, hygieneProbCount = null, hygieneProbSolvedCount = null;
		String badOdor = null ,iron = null, manganeseOrArsenic= null, other  = null ;
		
		JsonObject statistics= new JsonObject();
		JsonArray json_dataList =new JsonArray();
		JsonArray json_dataListReasonUnWater =new JsonArray();
		JsonArray inactiveToiletReasonJsonArray =new JsonArray();
		try {
			stmt = conn.createStatement();
			//rs = stmt.executeQuery(queryTotalSchool);
			rs = stmt.executeQuery(querybaseLineInfo);
			while(rs.next()){
				totalSchool = rs.getString("school_count");
				Logger.info("total school:"+totalSchool);
				femaleToiletRatio = rs.getString("femaleToiletRatio");
				totalToilet = rs.getString("totalToilets");
			}
			if(totalSchool == null) {
				totalSchool = "0";
			}
			if(femaleToiletRatio == null) {
				femaleToiletRatio = "0";
			}
			if(totalToilet == null) {
				totalToilet = "0";
			}
			
			
			rs = stmt.executeQuery(queryToiletUsability);
			while(rs.next()){
				//totalToilet = rs.getString("total_toilet");
				unusableToilet = rs.getString("unusable_toilet");
			}
			/*if(totalToilet == null) {
				totalToilet = "0";
			}*/
			if(unusableToilet == null) {
				unusableToilet = "0";
			}
			
			/*TODO needs to be done later*/
			//femaleToiletRatio = "0";
			/*
			rs = stmt.executeQuery(queryToiletGirlsRatio);
			while(rs.next()){
				femaleToiletRatio = rs.getString("school_count");
			}
			if(femaleToiletRatio == null) {
				femaleToiletRatio = "0";
			}
			*/
			
			
			
			rs = stmt.executeQuery(queryToiletDisableFriendly);
			while(rs.next()){
				disabledFriendly = rs.getString("disabled");
			}
			if(disabledFriendly == null) {
				disabledFriendly = "0";
			}
			
			
			rs = stmt.executeQuery(querySoapHandwash);
			while(rs.next()){
				soapHandwashNone = rs.getString("soap_handwash_none");
				soapHandwashMostlyNone = rs.getString("soap_handwash_mostly_none");
				soapHandwashHalf = rs.getString("soap_handwash_half");
				soapHandwashMostlySome = rs.getString("soap_handwash_mostly_some");
				soapHandwashAll= rs.getString("soap_handwash_all");
			}
			if(soapHandwashNone == null) {
				soapHandwashNone = "0";
			}
			if(soapHandwashMostlyNone == null) {
				soapHandwashMostlyNone= "0";
			}
			if(soapHandwashHalf == null) {
				soapHandwashHalf = "0";
			}
			if(soapHandwashMostlySome  == null) {
				soapHandwashMostlySome = "0";
			}
			if(soapHandwashAll == null) {
				soapHandwashAll = "0";
			}
			
			
			rs = stmt.executeQuery(queryTissue);
			while(rs.next()){
				tissueNone= rs.getString("tissue_none");
				tissueMostlyNone = rs.getString("tissue_mostly_none");
				tissueHalf = rs.getString("tissue_half");
				tissueMostlySome = rs.getString("tissue_mostly_some");
				tissueAll= rs.getString("tissue_all");
			}
			if(tissueNone == null) {
				tissueNone = "0";
			}
			if(tissueMostlyNone == null) {
				tissueMostlyNone= "0";
			}
			if(tissueHalf == null) {
				tissueHalf= "0";
			}
			if(tissueMostlySome  == null) {
				tissueMostlySome = "0";
			}
			if(tissueAll == null) {
				tissueAll = "0";
			}
			
			
			rs = stmt.executeQuery(queryDustbin);
			while(rs.next()){
				dustbinNone= rs.getString("dustbin_none");
				dustbinMostlyNone = rs.getString("dustbin_mostly_none");
				dustbinHalf = rs.getString("dustbin_half");
				dustbinMostlySome = rs.getString("dustbin_mostly_some");
				dustbinAll= rs.getString("dustbin_all");
			}
			if(dustbinNone == null) {
				dustbinNone = "0";
			}
			if(dustbinMostlyNone == null) {
				dustbinMostlyNone= "0";
			}
			if(dustbinHalf == null) {
				dustbinHalf= "0";
			}
			if(dustbinMostlySome  == null) {
				dustbinMostlySome = "0";
			}
			if(dustbinAll == null) {
				dustbinAll = "0";
			}
			
			
			rs = stmt.executeQuery(querySandal);
			while(rs.next()){
				sandalNone= rs.getString("sandal_none");
				sandalMostlyNone = rs.getString("sandal_mostly_none");
				sandalHalf = rs.getString("sandal_half");
				sandalMostlySome = rs.getString("sandal_mostly_some");
				sandalAll= rs.getString("sandal_all");
			}
			if(sandalNone == null) {
				sandalNone = "0";
			}
			if(sandalMostlyNone == null) {
				sandalMostlyNone= "0";
			}
			if(sandalHalf == null) {
				sandalHalf= "0";
			}
			if(sandalMostlySome  == null) {
				sandalMostlySome = "0";
			}
			if(sandalAll == null) {
				sandalAll = "0";
			}
			
			rs = stmt.executeQuery(queryMug);
			while(rs.next()){
				mugNone= rs.getString("mug_none");
				mugMostlyNone = rs.getString("mug_mostly_none");
				mugHalf = rs.getString("mug_half");
				mugMostlySome = rs.getString("mug_mostly_some");
				mugAll= rs.getString("mug_all");
			}
			if(mugNone == null) {
				mugNone = "0";
			}
			if(mugMostlyNone == null) {
				mugMostlyNone= "0";
			}
			if(mugHalf == null) {
				mugHalf= "0";
			}
			if(mugMostlySome  == null) {
				mugMostlySome = "0";
			}
			if(mugAll == null) {
				mugAll = "0";
			}
			

			rs = stmt.executeQuery(queryInformedWater);
			while(rs.next()){
				waterProbCount = rs.getString("informed_water_prob");
			}
			if(waterProbCount == null) {
				waterProbCount = "0";
			}
			
			
			rs = stmt.executeQuery(queryWaterProbSolved);
			while(rs.next()){
				waterProbSolvedCount = rs.getString("water_prob_solved");
			}
			if(waterProbSolvedCount == null) {
				waterProbSolvedCount = "0";
			}
			
			
			rs = stmt.executeQuery(queryInformedHygene);
			while(rs.next()){
				hygieneProbCount= rs.getString("informed_hygiene_prob");
			}
			if(hygieneProbCount == null) {
				hygieneProbCount = "0";
			}
			
			
			rs = stmt.executeQuery(queryHygeneProbSolved);
			while(rs.next()){
				hygieneProbSolvedCount = rs.getString("hygiene_prob_solved");
			}
			if(hygieneProbSolvedCount == null) {
				hygieneProbSolvedCount = "0";
			}
			
			
			rs = stmt.executeQuery(queryInformedSanitaion);
			while(rs.next()){
				sanitaionProbCount = rs.getString("informed_sanitation_prob");
			}
			if(sanitaionProbCount == null) {
				sanitaionProbCount = "0";
			}
			
			
			rs = stmt.executeQuery(querySanitaionProbSolved);
			while(rs.next()){
				sanitaionProbSolvedCount = rs.getString("sanitation_prob_solved");
			}
			if(sanitaionProbSolvedCount == null) {
				sanitaionProbSolvedCount = "0";
			}
			
			//reason behind unsafe water
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(queryCausesBehindUnsafeWater);
				rs.next();
				badOdor = rs.getString("badOdor");
				iron = rs.getString("iron");
				manganeseOrArsenic = rs.getString("mnOrAs");
				other = rs.getString("other");
				if(badOdor != null){
					JsonObject dataJson = new JsonObject();
					dataJson.addProperty("tag", "Bad Odor");
					dataJson.addProperty("number", badOdor);
					json_dataListReasonUnWater.add(dataJson);
				}
				if(iron != null){
					JsonObject dataJson = new JsonObject();
					dataJson.addProperty("tag", "Iron");
					dataJson.addProperty("number", iron);
					json_dataListReasonUnWater.add(dataJson);
				}
				if(manganeseOrArsenic != null){
					JsonObject dataJson = new JsonObject();
					dataJson.addProperty("tag", "Arsenic & Manganese");
					dataJson.addProperty("number", manganeseOrArsenic);
					json_dataListReasonUnWater.add(dataJson);
				}
				if(other != null){
					JsonObject dataJson = new JsonObject();
					dataJson.addProperty("tag", "Other");
					dataJson.addProperty("number", other);
					json_dataListReasonUnWater.add(dataJson);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			statistics.addProperty("totalSchool", totalSchool);
			Logger.info("Statistics total school:"+statistics.get(totalSchool));
			statistics.addProperty("totalToilet", totalToilet);
			statistics.addProperty("unusableToilet", unusableToilet);
			statistics.addProperty("femaleToiletRatio", femaleToiletRatio);
			statistics.addProperty("disabledFriendly", disabledFriendly);
			statistics.addProperty("soapHandwashNone", soapHandwashNone);
			statistics.addProperty("soapHandwashMostlyNone", soapHandwashMostlyNone);
			statistics.addProperty("soapHandwashHalf", soapHandwashHalf);
			statistics.addProperty("soapHandwashMostlySome", soapHandwashMostlySome);
			statistics.addProperty("soapHandwashAll", soapHandwashAll);
			
			statistics.addProperty("tissueNone", tissueNone);
			statistics.addProperty("tissueMostlyNone", tissueMostlyNone);
			statistics.addProperty("tissueHalf", tissueHalf);
			statistics.addProperty("tissueMostlySome", tissueMostlySome);
			statistics.addProperty("tissueAll", tissueAll);
			
			statistics.addProperty("dustbinNone", dustbinNone);
			statistics.addProperty("dustbinMostlyNone", dustbinMostlyNone);
			statistics.addProperty("dustbinHalf", dustbinHalf);
			statistics.addProperty("dustbinMostlySome", dustbinMostlySome);
			statistics.addProperty("dustbinAll", dustbinAll);

			statistics.addProperty("sandalNone", sandalNone);
			statistics.addProperty("sandalMostlyNone", sandalMostlyNone);
			statistics.addProperty("sandalHalf", sandalHalf);
			statistics.addProperty("sandalMostlySome", sandalMostlySome);
			statistics.addProperty("sandalAll", sandalAll);
			
			statistics.addProperty("mugNone", mugNone);
			statistics.addProperty("mugMostlyNone", mugMostlyNone);
			statistics.addProperty("mugHalf", mugHalf);
			statistics.addProperty("mugMostlySome", mugMostlySome);
			statistics.addProperty("mugAll", mugAll);
			
			statistics.addProperty("waterProbCount", waterProbCount);
			statistics.addProperty("waterProbSolvedCount", waterProbSolvedCount);
			statistics.addProperty("hygieneProbCount", hygieneProbCount);
			statistics.addProperty("hygieneProbSolvedCount", hygieneProbSolvedCount);
			statistics.addProperty("sanitaionProbCount", sanitaionProbCount);
			statistics.addProperty("sanitaionProbSolvedCount", sanitaionProbSolvedCount);
		
			
			JsonArray jsonHygeneArray =new JsonArray();
			JsonArray jsonSanitaitonArray  =new JsonArray();
			JsonArray jsonWaterArray  =new JsonArray();
		
			Date date = new Date();
	    	Logger.info("date:"+date);
			Calendar cal =  Calendar.getInstance();
			cal.setTime(date);
			//cal.add(Calendar.MONTH ,-1);
			int month = cal.get(Calendar.MONTH)+1; //as Calender start index is 0, so plus 1
			int year = cal.get(Calendar.YEAR);
			Logger.info("month:"+month);
			Logger.info("year:"+year);
			//format it to MMM-yyyy // January-2012
			//String previousMonthYear  = new SimpleDateFormat("MMM-yyyy").format(cal.getTime());  //Dec-2005
			String previousMonthYear  = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			String formatMonthLine  = new SimpleDateFormat("MMM").format(cal.getTime());
			String formatDateLine = formatMonthLine+" "+year;
			//String previousMonthYear  = new SimpleDateFormat("MM yyyy").format(cal.getTime());
	    	for(int i = 0 ; i < 12 ; i++){
	    		
				String search_query1 = "SELECT round(avg(rank_hygiene__1),1) as hygene ,round(avg(rank_sanitation__1),1) as sanitation, round(avg(rank_water__1),1) as safe_water FROM tblFlat"
						+ " WHERE MONTH(received)='"+month+"'  AND YEAR(received)='"+year+"' ";
				
				String safewater = null ;
				String hygene = null ;
				String sanitation = null ;
				try {
					stmt = conn.createStatement();
					rs = stmt.executeQuery(search_query1);
					while(rs.next()){
						safewater = rs.getString("safe_water");
						hygene = rs.getString("hygene");
						sanitation = rs.getString("sanitation");
					
					}
					/*if(safewater == null) {
						safewater ="0";
					}
					if(hygene == null) {
						hygene ="0";
					}
					if(sanitation == null) {
						sanitation ="0";
					}*/
					if(hygene == null && sanitation == null && safewater == null){
						
					}
					else{
						JsonObject hygeneJson = new JsonObject();
						hygeneJson.addProperty("symbol", "Hygiene");
						hygeneJson.addProperty("date", formatDateLine);
						hygeneJson.addProperty("y_value", hygene);
						jsonHygeneArray.add(hygeneJson);
						
						JsonObject sanitationJson = new JsonObject();
						sanitationJson.addProperty("symbol", "Sanitation");
						sanitationJson.addProperty("date", formatDateLine);
						sanitationJson.addProperty("y_value", sanitation);
						jsonSanitaitonArray.add(sanitationJson);
						
						JsonObject waterJson = new JsonObject();
						waterJson.addProperty("symbol", "Safe Water");
						waterJson.addProperty("date", formatDateLine);
						waterJson.addProperty("y_value", safewater);
						jsonWaterArray.add(waterJson);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		
				//Get previous month start
				//String myTextDate = "01/01/2006";
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date myDate = null;
				try {
					myDate = sdf.parse(previousMonthYear);
					//Logger.info("Here track:"+myDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Calendar calNext =  Calendar.getInstance();
				calNext.setTime(myDate);
				calNext.add(Calendar.MONTH ,-1);
				month = calNext.get(Calendar.MONTH)+1;   //as Calender start index is 0, so plus 1
				year = calNext.get(Calendar.YEAR);
				previousMonthYear  = new SimpleDateFormat("dd/MM/yyyy").format(calNext.getTime());
				formatMonthLine  = new SimpleDateFormat("MMM").format(calNext.getTime());
				formatDateLine = formatMonthLine+" "+year;
				//Get previous month end
				
			}//end for	
	    	for(JsonElement water: jsonWaterArray){
				json_dataList.add(water);
			}
			
			for(JsonElement sanitaion : jsonSanitaitonArray){
				json_dataList.add(sanitaion);
			}
			for(JsonElement hygene : jsonHygeneArray){
				json_dataList.add(hygene);
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(queryInactiveToiletReason);
			rs.next();
				JsonObject inactiveToiletReasonJson = new JsonObject();
				inactiveToiletReasonJson.addProperty("tag", "Water Tab is Broken");
				inactiveToiletReasonJson.addProperty("number", rs.getInt("WaterTabBroken"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson);
		
				JsonObject inactiveToiletReasonJson1 = new JsonObject();
				inactiveToiletReasonJson1.addProperty("tag", "Water Sources is FarAway");
				inactiveToiletReasonJson1.addProperty("number", rs.getInt("WaterSourcesFarAway"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson1);
				
				JsonObject inactiveToiletReasonJson2 = new JsonObject();
				inactiveToiletReasonJson2.addProperty("tag", "No Water");
				inactiveToiletReasonJson2.addProperty("number", rs.getInt("NoWater"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson2);
				
				JsonObject inactiveToiletReasonJson3 = new JsonObject();
				inactiveToiletReasonJson3.addProperty("tag", "No Lock");
				inactiveToiletReasonJson3.addProperty("number", rs.getInt("NoLock"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson3);
				
				JsonObject inactiveToiletReasonJson4 = new JsonObject();
				inactiveToiletReasonJson4.addProperty("tag", "Broken Door");
				inactiveToiletReasonJson4.addProperty("number", rs.getInt("BrokenDoor"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson4);
				
				JsonObject inactiveToiletReasonJson5 = new JsonObject();
				inactiveToiletReasonJson5.addProperty("tag", "No Water Container");
				inactiveToiletReasonJson5.addProperty("number", rs.getInt("NoWaterContainer"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson5);
				
				JsonObject inactiveToiletReasonJson6 = new JsonObject();
				inactiveToiletReasonJson6.addProperty("tag", "Overflow Water");
				inactiveToiletReasonJson6.addProperty("number", rs.getInt("OverflowWater"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson6);
				
				JsonObject inactiveToiletReasonJson7 = new JsonObject();
				inactiveToiletReasonJson7.addProperty("tag", "Water logged");
				inactiveToiletReasonJson7.addProperty("number", rs.getInt("WaterClogging"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson7);
				
				JsonObject inactiveToiletReasonJson8 = new JsonObject();
				inactiveToiletReasonJson8.addProperty("tag", "No Dustbin");
				inactiveToiletReasonJson8.addProperty("number", rs.getInt("NoDustbin"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson8);
				
				JsonObject inactiveToiletReasonJson9 = new JsonObject();
				inactiveToiletReasonJson9.addProperty("tag", "No Light");
				inactiveToiletReasonJson9.addProperty("number", rs.getInt("NoLight"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson9);
				
				JsonObject inactiveToiletReasonJson10 = new JsonObject();
				inactiveToiletReasonJson10.addProperty("tag", "Insects");
				inactiveToiletReasonJson10.addProperty("number", rs.getInt("Insects"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson10);
				
				JsonObject inactiveToiletReasonJson11 = new JsonObject();
				inactiveToiletReasonJson11.addProperty("tag", "Bad Smell");
				inactiveToiletReasonJson11.addProperty("number", rs.getInt("BadSmell"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson11);
				
				JsonObject inactiveToiletReasonJson12 = new JsonObject();
				inactiveToiletReasonJson12.addProperty("tag", "Other");
				inactiveToiletReasonJson12.addProperty("number", rs.getInt("Other"));
				inactiveToiletReasonJsonArray.add(inactiveToiletReasonJson12);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/* For reporting problem graph*/
		int quarter = 1 ;
		quarter = getQuarter();
		int monthStart = quarter * 3 -2 ;
		String probType = " how_informed_sanitation_prob__1 ";
		String timeType = " took_step_sanitation_prob__1 ";
		JsonArray json_datalist_areaChart =new JsonArray();
		JsonArray json_problemlist =new JsonArray();
		JsonArray json_solvelist =new JsonArray();
		String [] month_names = {"January","February","March","April","May","June",
				"July","August","September","October","November","December"};
		for(int i = 0 ; i < 3 ; i++){
//			String search_query1 = "Select (select count(is_potable__1) from tblFlat where is_potable__1 = '1' AND MONTH(received)='"+(i+monthStart)+"' "+ dataIdCond+ajaxQuery +")"
//					+ " as potable, (select count(is_potable__1) from tblFlat where is_potable__1 = '0'  AND MONTH(received)='"+(i+monthStart)+"' "+ dataIdCond+ajaxQuery +") as non_potable";

			String queryWaterIssue = "SELECT (SELECT count("+probType+") FROM tblFlat where "+probType+" = '1' AND MONTH(received)='"+(i+monthStart)+"' ) as school_authority," +
									"(SELECT count("+probType+") FROM tblFlat where "+probType+" = '2' AND MONTH(received)='"+(i+monthStart)+"' ) as head_teacher,"+
									"(SELECT count("+probType+") FROM tblFlat where "+probType+" = '3' AND MONTH(received)='"+(i+monthStart)+"' ) as class_teacher,"+ 
									"(SELECT count("+probType+") FROM tblFlat where "+probType+" = '4' AND MONTH(received)='"+(i+monthStart)+"' ) as school_mgt,"+
									"(SELECT count("+probType+") FROM tblFlat where "+probType+" = '5' AND MONTH(received)='"+(i+monthStart)+"' )  as others";	
			
			String queryWaterSolve = "SELECT (SELECT count("+timeType+") FROM tblFlat where "+timeType+" = '1' )  as less_than_one, "+
									"(SELECT count("+timeType+") FROM tblFlat where "+timeType+" = '2' AND MONTH(received)='"+(i+monthStart)+"' ) as two_to_three, " + 
									"(SELECT count("+timeType+") FROM tblFlat where "+timeType+" = '3' AND MONTH(received)='"+(i+monthStart)+"' ) as four_to_seven," +  
									"(SELECT count("+timeType+") FROM tblFlat where "+timeType+" = '4' AND MONTH(received)='"+(i+monthStart)+"' ) as eight_to_thirty, "+
									"(SELECT count("+timeType+") FROM tblFlat where "+timeType+" = '5' AND MONTH(received)='"+(i+monthStart)+"' )  as more_than_thirty ,"+
									"(SELECT count("+timeType+") FROM tblFlat where "+timeType+" = '6' AND MONTH(received)='"+(i+monthStart)+"' )  as unsolved, "+
									"(SELECT count("+timeType+") FROM tblFlat where "+timeType+" = '7' AND MONTH(received)='"+(i+monthStart)+"' )  as unknowns";

			String schoolAuthority = null, headTecher = null, classTeacher = null, schoolManagement = null, other_1 = null;
			Double dSchoolAuthority = 0.0, dHeadTecher = 0.0, dClassTeacher = 0.0, dSchoolManagement = 0.0, dOther = 0.0;
			String unknown = null, unresolved = null, lessThanOne = null, twoToThree = null, fourToSeven = null, eightToThirty = null, moreThanThirty = null;
			Double dUnknown = null, dUnresolved = 0.0, dLessThanOne = 0.0, dTwoToThree = 0.0, dFourToSeven = 0.0, dEightToThirty = 0.0, dMoreThanThirty = 0.0;
			double total = 0 ; 
			DecimalFormat df = new DecimalFormat("#.##"); 
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(queryWaterIssue);
				while(rs.next()){
				schoolAuthority = rs.getString("school_authority");
				headTecher = rs.getString("head_teacher");
				classTeacher = rs.getString("class_teacher");
				schoolManagement = rs.getString("school_mgt");
				other_1 = rs.getString("others");
				}
				if(schoolAuthority == null){
					schoolAuthority ="0";
				}
				if(headTecher == null){
					headTecher ="0";
				}
				if(classTeacher == null){
					classTeacher ="0";
				}
				if(schoolManagement == null){
					schoolManagement ="0";
				}
				if(other_1 == null){
					other_1 ="0";
				}
				dSchoolAuthority = Double.parseDouble(schoolAuthority);
				dHeadTecher = Double.parseDouble(headTecher);
				dClassTeacher = Double.parseDouble(classTeacher);
				dSchoolManagement = Double.parseDouble(schoolManagement);
				dOther = Double.parseDouble(other_1);
				total =  dSchoolAuthority + dHeadTecher + dClassTeacher + dSchoolManagement + dOther;
				schoolAuthority = (total > 0.0) ? String.valueOf((dSchoolAuthority / total)*100) : "0";
				headTecher = (total > 0.0) ? String.valueOf((dHeadTecher/ total)*100) : "0";
				classTeacher = (total > 0.0) ? String.valueOf((dClassTeacher / total)*100) : "0";
				schoolManagement = (total > 0.0) ? String.valueOf((dSchoolManagement/ total)*100) : "0";
				other_1 = (total > 0.0) ? String.valueOf((dOther / total)*100) : "0";
				//Logger.info("Param - > " + schoolAuthority +"--" +  headTecher + "--" + classTeacher);
				JsonObject dataJson = new JsonObject();
				dataJson.addProperty("State", month_names[i+monthStart-1]);
				dataJson.addProperty("School Authority", df.format(Double.valueOf(schoolAuthority)));
				dataJson.addProperty("Head Teacher", df.format(Double.valueOf(headTecher)));
				dataJson.addProperty("Class Teacher", df.format(Double.valueOf(classTeacher)));
				dataJson.addProperty("School Management", df.format(Double.valueOf(schoolManagement)));
				dataJson.addProperty("Other", df.format(Double.valueOf(other_1)));
				json_problemlist.add(dataJson);
				
				rs = stmt.executeQuery(queryWaterSolve);
				while(rs.next()){
				unknown = rs.getString("unknowns");
				unresolved = rs.getString("unsolved");
				lessThanOne = rs.getString("less_than_one");
				twoToThree = rs.getString("two_to_three");
				fourToSeven = rs.getString("four_to_seven");
				eightToThirty = rs.getString("eight_to_thirty");
				moreThanThirty = rs.getString("more_than_thirty");
				}
				if(unknown == null){
					unknown ="0";
				}
				if(unresolved == null){
					unresolved ="0";
				}
				if(lessThanOne == null){
					lessThanOne ="0";
				}
				if(twoToThree == null){
					twoToThree ="0";
				}
				if(fourToSeven == null){
					fourToSeven ="0";
				}
				if(eightToThirty == null){
					eightToThirty ="0";
				}
				if(moreThanThirty == null){
					moreThanThirty ="0";
				}
				total = 0.0;
				dUnknown = Double.parseDouble(schoolAuthority);
				dUnresolved= Double.parseDouble(headTecher);
				dLessThanOne= Double.parseDouble(classTeacher);
				dTwoToThree= Double.parseDouble(schoolManagement);
				dFourToSeven = Double.parseDouble(fourToSeven);
				dEightToThirty= Double.parseDouble(eightToThirty);
				dMoreThanThirty= Double.parseDouble(moreThanThirty);
				total =  dUnknown + dUnresolved + dLessThanOne + dTwoToThree + dFourToSeven + dEightToThirty + dMoreThanThirty;
				unknown = (total > 0.0) ? String.valueOf((dUnknown / total)*100) : "0";
				unresolved = (total > 0.0) ? String.valueOf((dUnresolved/ total)*100) : "0";
				lessThanOne = (total > 0.0) ? String.valueOf((dLessThanOne / total)*100) : "0";
				twoToThree = (total > 0.0) ? String.valueOf((dTwoToThree/ total)*100) : "0";
				fourToSeven = (total > 0.0) ? String.valueOf((dFourToSeven / total)*100) : "0";
				eightToThirty = (total > 0.0) ? String.valueOf((dEightToThirty/ total)*100) : "0";
				moreThanThirty = (total > 0.0) ? String.valueOf((dMoreThanThirty/ total)*100) : "0";
				
				JsonObject solutionJson = new JsonObject();
				solutionJson.addProperty("State", month_names[i+monthStart-1]);
				solutionJson.addProperty("Unknowns", df.format(Double.valueOf(unknown)));
				solutionJson.addProperty("Unsolved", df.format(Double.valueOf(unresolved)));
				solutionJson.addProperty("<1 Day", df.format(Double.valueOf(lessThanOne)));
				solutionJson.addProperty("2-3 Days", df.format(Double.valueOf(twoToThree)));
				solutionJson.addProperty("4-7 Days", df.format(Double.valueOf(fourToSeven)));
				solutionJson.addProperty("8-30 Days", df.format(Double.valueOf(eightToThirty)));
				solutionJson.addProperty(">30 Days", df.format(Double.valueOf(moreThanThirty)));
				json_solvelist.add(solutionJson);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}//end for
		json_datalist_areaChart.add(json_problemlist);
		json_datalist_areaChart.add(json_solvelist);

		/* For reporting problem graph*/

		render(statistics,json_dataList,forms,headSelector, json_dataListReasonUnWater,inactiveToiletReasonJsonArray, json_datalist_areaChart,json_problemlist,json_solvelist);
		//return null;

        //render(forms);

	}
	
	
	 public static int getQuarter() {
	 		int quarter = 1;
	 		Date date = new Date();
	 		
	 		Calendar cal = Calendar.getInstance();
	 		cal.setTime(date);
	 		int month = cal.get(Calendar.MONTH);
	 		if(month >=1 && month <=3){
	 			quarter = 1 ;
	 		}else if(month >=4 && month <=6){
	 			quarter = 2 ;	
	 		}else if(month >=7 && month <=9){
	 			quarter = 3 ;
	 		}else if(month >=10 && month <=12){
	 			quarter = 4 ;
	 		}
	 		return quarter;
	 	}
	 	
	    public static void showQuestionnaire(){
	    	
	    	render();
	    }
	    
	   }
