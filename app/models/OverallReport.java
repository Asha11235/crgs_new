package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;

public class OverallReport {
	
	public static Map<String, String> firstChartInOverallReport(Long divisionId,Long districtId,Long upazillaId,Long schoolId,Long studentType, Date startDate, Date endDate) throws ParseException, SQLException{
		
		
		Logger.info("divisionId : " + divisionId + " districtId : " + districtId + " upazillaId : " + upazillaId + " schoolId : " + schoolId + " studentType : " + studentType);

		List<Date> dates = getDates();
		
		String[] monthName = getMonthName(dates);
		
		Map<String, String> mp = new HashMap<String, String>();
		
		List<Float> datas = getData("Water", "rank_water", dates, divisionId, 
				districtId, upazillaId, schoolId, studentType, startDate, endDate);
		
		int k = 0;
		for(int i = 0; i < datas.size(); i++){
			mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
		}
		

		datas = getData("Sanitation", "rank_sanitation", dates, divisionId, 
				districtId, upazillaId, schoolId, studentType, startDate, endDate);
		for(int i = 0; i < datas.size(); i++){
			mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
		}
		

		datas = getData("SchoolEnvironment", "rank_Environment", dates, divisionId, 
				districtId, upazillaId, schoolId, studentType, startDate, endDate);
		for(int i = 0; i < datas.size(); i++){
			mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
		}
		

		datas = getData("SportsRecreation", "rank_SportsRecreation", dates, divisionId, 
				districtId, upazillaId, schoolId, studentType, startDate, endDate);
		for(int i = 0; i < datas.size(); i++){
			mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
		}
		
		
		return mp;
	}
	
	public static Map<String, String> secondChartInOverallReport(Long divisionId,Long districtId,Long upazillaId,Long schoolId,Long studentType,  Date startDate, Date endDate,
			 Long formId,Boolean divisionFilter,Boolean districtFilter,Boolean upazillaFilter,Boolean schoolFilter) throws ParseException, SQLException{

		Logger.info("divisionId:" + divisionId + " districtId:" + districtId + " upazillaId:" + upazillaId+ 
				" schoolId:" + schoolId + " formId:"+ formId +" divisionFilter:" + divisionFilter + 
				" districtFilter:" + districtFilter + " upazillaFilter:" + upazillaFilter + " schoolFilter:" + schoolFilter);
		
		List<Date> dates = getDates();
		String[] monthName = getMonthName(dates);
		
		Map<String, String> mp = new HashMap<String, String>();
		int k = 0;
		List<Float> datas = new ArrayList<Float>();
		
		if(formId == 1){
			datas = getData("Water", "rank_water", dates, divisionId, 
					districtId, upazillaId, schoolId, studentType, startDate, endDate);
			for(int i = 0; i < datas.size(); i++){
				mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
			}
			if(divisionFilter != false){
				datas = getData("Water", "rank_water", dates, divisionId, 
						null, null, null, null, startDate, endDate);
				for(int i = 0; i < datas.size(); i++){
					mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
				}
			}
			if(districtFilter != false){
				datas = getData("Water", "rank_water", dates, null, 
						districtId, null, null, null, startDate, endDate);
				for(int i = 0; i < datas.size(); i++){
					mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
				}
			}
			if(upazillaFilter != false){
				datas = getData("Water", "rank_water", dates, null, 
						null, upazillaId, null, null, startDate, endDate);
				for(int i = 0; i < datas.size(); i++){
					mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
				}
			}
			/*if(schoolFilter != false){
				datas = getData("Water", "rank_water", dates, null, 
						null, upazillaId, null, null, startDate, endDate);
				for(int i = 0; i < datas.size(); i++){
					mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
				}
			}*/
		}
		
		
		if (formId == 2L) {
			datas = getData("Sanitation", "rank_sanitation", dates, divisionId, 
					districtId, upazillaId, schoolId, studentType, startDate, endDate);
			for(int i = 0; i < datas.size(); i++){
				mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
			}
		}
		
		if(formId == 3L){
			datas = getData("SchoolEnvironment", "rank_Environment", dates, divisionId, 
					districtId, upazillaId, schoolId, studentType, startDate, endDate);
			for(int i = 0; i < datas.size(); i++){
				mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
			}
		}
		
		if(formId == 4L){
			datas = getData("SportsRecreation", "rank_SportsRecreation", dates, divisionId, 
					districtId, upazillaId, schoolId, studentType, startDate, endDate);
			for(int i = 0; i < datas.size(); i++){
				mp.put("" + k++, monthName[i]);       mp.put("" + k++, datas.get(i) + "");
			}
		}
		return mp;
	}
	
	
	public static List<Date> getDates() throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		
		Date toDay = calendar.getTime();
		List<Date> dates = new ArrayList<Date>();
		
		if (toDay.compareTo(dateFormat.parse("2017-07-01")) < 0) { //july month check
			dates.add(dateFormat.parse("2017-01-01")); dates.add(dateFormat.parse("2017-02-01"));
			dates.add(dateFormat.parse("2017-03-01"));dates.add(dateFormat.parse("2017-04-01"));
			dates.add(dateFormat.parse("2017-05-01"));dates.add(dateFormat.parse("2017-06-01"));
		}else {
			calendar.add(Calendar.MONTH, -6); dates.add(calendar.getTime());
			
			calendar.add(Calendar.MONTH, 1); dates.add(calendar.getTime());
			
			calendar.add(Calendar.MONTH, 1); dates.add(calendar.getTime());
			
			calendar.add(Calendar.MONTH, 1); dates.add(calendar.getTime());
			
			calendar.add(Calendar.MONTH, 1); dates.add(calendar.getTime());
			
			calendar.add(Calendar.MONTH, 1); dates.add(calendar.getTime());
		
		}
		return dates;
	}
	
	public static String[] getMonthName(List<Date> dates){
		String[] monthName = new String[6];
		
		int index = 0;
		for(Date date : dates) {
			switch (getMonthNumber(date)) {
			case 1:
				monthName[index++] = "jan'" + getYearNumber(date) % 2000;
				break;
			case 2:
				monthName[index++] = "feb'" + getYearNumber(date) % 2000;
				break;
			case 3:
				monthName[index++] = "march'" + getYearNumber(date) % 2000;
				break;
			case 4:
				monthName[index++] = "april'" + getYearNumber(date) % 2000;
				break;
			case 5:
				monthName[index++] = "may'" + getYearNumber(date) % 2000;
				break;
			case 6:
				monthName[index++] = "jun'" + getYearNumber(date) % 2000;
				break;
			case 7:
				monthName[index++] = "jul'" + getYearNumber(date) % 2000;
				break;
			case 8:
				monthName[index++] = "aug'" + getYearNumber(date) % 2000;
				break;
			case 9:
				monthName[index++] = "sep'" + getYearNumber(date) % 2000;
				break;
			case 10:
				monthName[index++] = "oct'" + getYearNumber(date) % 2000;
				break;
			case 11:
				monthName[index++] = "nov'" + getYearNumber(date) % 2000;
				break;
			case 12:
				monthName[index++] = "dec'" + getYearNumber(date) % 2000;
				break;
	
			default:
				break;
			}
		}
		return monthName;
	}
	
	public static int getMonthNumber(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	public static int getYearNumber(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	public static List<Float> getData(String tableName, String fieldName, List<Date> dates,Long divisionId,Long districtId,
			Long upazillaId,Long schoolId,Long studentType, Date startDate, Date endDate) throws SQLException {

		
		String previousTableName = tableName;
		Calendar calendar = Calendar.getInstance();
		
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		PreparedStatement queryForExecution = null;
		
		String whereClause = "";
		whereClause = " Where "+ previousTableName + ".created_at between cast( ? as DateTime) and cast( ? as DateTime)";
		
		if(divisionId != null || districtId != null && upazillaId != null && schoolId != null && studentType != null){
			
			tableName =  tableName + " join SchoolInformation on " + tableName + ".school_id = SchoolInformation.id";
			if(divisionId != null){
				whereClause += " and SchoolInformation.geoDivision_id = " + divisionId;
			}
			if(districtId != null){
				whereClause += " and SchoolInformation.geoDistrict_id = " + districtId;
			}
			if(upazillaId != null){
				whereClause += " and SchoolInformation.geoUpazilla_id = " + upazillaId;
			}
			if(schoolId != null){
				whereClause += " and SchoolInformation.id = " + schoolId;
			}
			if(studentType != null){
				whereClause += " and " + previousTableName + ".res_type = " + studentType;
			}
		}
		
		List<Float> data = new ArrayList<Float>();
		for (int i = 0; i < dates.size(); i++) {
			Date date = dates.get(i);
			calendar.setTime(date);
			String first = getYearNumber(date)+"-" + getMonthNumber(date) + "-"+ calendar.getActualMinimum(Calendar.DAY_OF_MONTH) + " 00:00:00";
			String last = getYearNumber(date)+"-" + getMonthNumber(date) + "-"+ calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + " 23:59:59";
			
			String sumQueryString = "select sum("+ fieldName +") as ranksum from " + tableName + whereClause;

			
			queryForExecution = conn.prepareStatement(sumQueryString);
			queryForExecution.setString(1, first);
			queryForExecution.setString(2, last);
			//Logger.info("table name : "+ previousTableName);
			//Logger.info("sumQueryString : "+ sumQueryString + " first : " + first + " last " + last);
			long sum = 0, count = 0;
			try {
				rs = queryForExecution.executeQuery();
				while(rs.next()){
					sum = rs.getInt("ranksum");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			whereClause += " and " + previousTableName + "." + fieldName + " > 0";
			String countQueryString = "select count("+ fieldName +") as rankcount from " + tableName + whereClause;
			
			
			queryForExecution = conn.prepareStatement(countQueryString);
			queryForExecution.setString(1, first);
			queryForExecution.setString(2, last);
			
			//Logger.info("table name : "+ previousTableName);
			//Logger.info("countQueryString : "+ countQueryString + " first : " + first + " last " + last);
			try {
				rs = queryForExecution.executeQuery();
				while(rs.next()){
					count = rs.getInt("rankcount");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			//Logger.info(previousTableName + " sum : " + sum + " count : " + count);
			Float ans = 0f;
			if(count != 0){ ans = Float.parseFloat(sum / count + "");}
			data.add(ans);
		}
		return data;
	}
}
