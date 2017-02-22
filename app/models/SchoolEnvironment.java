package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class SchoolEnvironment extends Model {

	public Date date;
	
	@ManyToOne
	public Data data;
	@ManyToOne
	public Form form;
	
	@ManyToOne
	public User sender;
	
	@ManyToOne
	public SchoolInformation school;
	
	public String month = "";
	public String res_type = "";
	public String cleanInterval_SchoolYard = "";
	public String cleanInterval_ClassRoom = "";
	public String StuHearTeacher = "";
	
	public String stuSeatArrange = "";
	public String teacherStage = "";
	public String complained_SchoolEnvironment = "";
	public String how_informed_Environment_prob = "";
	
	public String how_informed_Environment_prob_other = "";
	public String took_step_Environment_prob = "";
	public String rank_Environment = "";
	public String rank_Edu_Quality = "";
	
	public String scareSafe_SchoolWay = "";
	public String yhy_not_feel_safe = "";
	public String schoolCanteen = "";
	
	
	
public static Map<String, Long> getSchoolEnvironmentData(Long divisionId,Long districtId, Long upazillaId, Long schoolId, Long studentType, Date startDate, Date endDate) throws SQLException{
		
	
	Logger.info("StudentType : " + studentType);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String firstDateOfPreviousMonth = null;
		String lastDateOfPreviousMonth = null;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);

		calendar.set(Calendar.DATE, 1);
		if(startDate == null)
			startDate = calendar.getTime();
		calendar.set(Calendar.DATE,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		if(endDate == null)
			endDate = calendar.getTime();

		try {
			firstDateOfPreviousMonth = dateFormat.format(startDate) + " 00:00:00";
			lastDateOfPreviousMonth = dateFormat.format(endDate) + " 23:59:59";

		} catch (Exception e) {
			e.printStackTrace();
		}

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		
		String qString = null;
		String whereClause = " Where SchoolEnvironment.created_at between cast( ? as DateTime) and cast( ?  as DateTime)";
		String table_name = "SchoolEnvironment";
		
		if(divisionId != null || districtId != null && upazillaId != null && schoolId != null && studentType != null){
			table_name = "SchoolEnvironment join SchoolInformation on SchoolEnvironment.school_id = SchoolInformation.id";
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
		}
		
		qString = "select count(SchoolEnvironment.id) as student, " +
				"count(distinct SchoolEnvironment.school_id) as school, " +
				"sum(SchoolEnvironment.stuSeatArrange) as sufficientSeat, " +
				"sum(SchoolEnvironment.scareSafe_SchoolWay) as safe, " +
				"sum(SchoolEnvironment.complained_SchoolEnvironment) as isInformed " +
				"from " + table_name;
		
		qString += whereClause + "  and SchoolEnvironment.res_type = ?";
		
		Logger.info("-----------------------------------------------------------");
		Logger.info("query1 string is : " + qString);
		Logger.info("-----------------------------------------------------------");
		
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		queryForExecution.setString(1, firstDateOfPreviousMonth);
		queryForExecution.setString(2, lastDateOfPreviousMonth);
		
		
		
		long boys = 0, girls = 0,
			boys_school = 0, girls_school = 0,
			sufficient_seat_boys = 0,sufficient_seat_girls = 0,
			scareSafe_boys = 0, scareSafe_girls = 0,
			complained_SchoolEnvironment_boys = 0, complained_SchoolEnvironment_girls = 0;
		
		if(studentType == null || studentType == 1L){
			queryForExecution.setString(3, "1");
			try {
				
				rs = queryForExecution.executeQuery();
				
				while (rs.next()) {
					boys = rs.getInt("student");
					boys_school = rs.getInt("school");
					sufficient_seat_boys = rs.getInt("sufficientSeat");
					scareSafe_boys = rs.getInt("safe");
					complained_SchoolEnvironment_boys = rs.getInt("isInformed");
				}
			} catch (SQLException e1) {
				Logger.info("Boy Exception");
				e1.printStackTrace();
			}
		}
		
		if(studentType == null || studentType == 2L){
			queryForExecution.setString(3, "2");
			try {
				
				rs = queryForExecution.executeQuery();
				
				while (rs.next()) {
					girls = rs.getInt("student");
					girls_school = rs.getInt("school");
					sufficient_seat_girls = rs.getInt("sufficientSeat");
					scareSafe_girls = rs.getLong("safe");
					complained_SchoolEnvironment_girls = rs.getInt("isInformed");
				}
			} catch (SQLException e1) {
				Logger.info("Girls Exception");
				e1.printStackTrace();
			}
		}
		//Cause of not feeling secure/safe -------------------------------------------start
		
		long notSecure1 = 0, notSecure2 = 0, notSecure3 = 0, notSecure4 = 0,notSecure5 = 0, notSecure6 = 0;

		qString = "select count(SchoolEnvironment.yhy_not_feel_safe) as cause from " + table_name;
		qString += whereClause + " and SchoolEnvironment.yhy_not_feel_safe like ? ";
		
		Logger.info("-----------------------------------------------------------");
		Logger.info("query2 string is : " + qString);
		Logger.info("-----------------------------------------------------------");
		
		queryForExecution = conn.prepareStatement(qString);	
		queryForExecution.setString(1, firstDateOfPreviousMonth);
		queryForExecution.setString(2, lastDateOfPreviousMonth);
		
		queryForExecution.setString(3, "%1%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				notSecure1 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%2%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				notSecure2 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%3%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				notSecure3 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%4%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				notSecure4 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%5%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				notSecure5 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		queryForExecution.setString(3, "%6%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				notSecure6 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		long totalCause = notSecure1 + notSecure2 + notSecure3 + notSecure4 + notSecure5 + notSecure6;
		if(totalCause != 0){
			notSecure1 = (notSecure1 * 100) / totalCause; notSecure2 = (notSecure2 * 100) / totalCause; 
			notSecure3 = (notSecure3 * 100) / totalCause; notSecure4 = (notSecure4 * 100) / totalCause;
			notSecure5 = (notSecure5 * 100) / totalCause; notSecure6 = (notSecure6 * 100) / totalCause;
		}
		
		Logger.info("notSecure1 : " + notSecure1 + " notSecure2 : " + notSecure2 + " notSecure3 : " + 
				notSecure3 + " notSecure4 : " + notSecure4 + " notSecure5 : " + notSecure5 + 
				" notSecure6 : " + notSecure6);

		//Cause of not feeling secure/safe -------------------------------------------end
		
		
		
		//Facility Student Canteen & Food Quality -------------------------------------------start
		
				long food1 = 0, food2 = 0, food3 = 0, food4 = 0,food5 = 0;

				qString = "select count(SchoolEnvironment.schoolCanteen) as cause from " + table_name;
				qString += whereClause + " and SchoolEnvironment.schoolCanteen like ? ";
				
				Logger.info("-----------------------------------------------------------");
				Logger.info("query3 string is : " + qString);
				Logger.info("-----------------------------------------------------------");
				
				queryForExecution = conn.prepareStatement(qString);	
				queryForExecution.setString(1, firstDateOfPreviousMonth);
				queryForExecution.setString(2, lastDateOfPreviousMonth);
				
				queryForExecution.setString(3, "%1%");
				try {
					
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						food1 = rs.getInt("cause");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%2%");
				try {
					rs = queryForExecution.executeQuery();
					
					while(rs.next()){
						food2 = rs.getInt("cause");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%3%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						food3 = rs.getInt("cause");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%4%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						food4 = rs.getInt("cause");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%5%");
				try {
					
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						food5 = rs.getInt("cause");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				long totalfood = food1 + food2 + food3 + food4 + food5;
				if(totalfood != 0){
					food1 = (food1 * 100)/ totalfood ; food2 = (food2 * 100) / totalfood; 
					food3 = (food3 * 100) / totalfood; food4 = (food4 * 100)/ totalfood;
					food5 = (food5 * 100)/ totalfood ;
				}
				
				Logger.info("food1 : " + food1 + " food2 : " + food2 + " food3 : " + 
						food3 + " food4 : " + food4 + "food5 : " + food5);

				//Facility Student Canteen & Food Quality -------------------------------------------end
		
		
				//ISSUE RAISING MEDIUM / MECHANISM -------------------------------------------start
				long issue1 = 0, issue2 = 0, issue3 = 0, issue4 = 0, issue5 = 0;
				
				qString = "select count(SchoolEnvironment.how_informed_Environment_prob) as issue from " + table_name;
				qString += whereClause + " and SchoolEnvironment.how_informed_Environment_prob like ?";
				
				Logger.info("-----------------------------------------------------------");
				Logger.info("query4 string is : " + qString);
				Logger.info("-----------------------------------------------------------");
				
				queryForExecution = conn.prepareStatement(qString);	
				queryForExecution.setString(1, firstDateOfPreviousMonth);
				queryForExecution.setString(2, lastDateOfPreviousMonth);
				
				queryForExecution.setString(3, "%1%");
				try {
					
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						issue1 = rs.getInt("issue");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%2%");
				try {
					rs = queryForExecution.executeQuery();
					
					while(rs.next()){
						issue2 = rs.getInt("issue");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%3%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						issue3 = rs.getInt("issue");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%4%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						issue4 = rs.getInt("issue");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				queryForExecution.setString(3, "%5%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						issue5 = rs.getInt("issue");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				long totalIssue = issue1 + issue2 + issue3 + issue4 + issue5;
				if(totalIssue != 0){
					issue1 = (issue1 * 100)/ totalIssue ; issue2 = (issue2 * 100) / totalIssue; 
					issue3 = (issue3 * 100) / totalIssue; issue4 = (issue4 * 100)/ totalIssue;
					issue5 = (issue5 * 100) / totalIssue;
				}
				Logger.info("issue1 : " + issue1 + " issue2 : " + issue2 + " issue3 : " + issue3 + " issue4 : " + issue4 + " issue5 : " + issue5);

				//ISSUE RAISING MEDIUM / MECHANISM -------------------------------------------end
				
				
				
				//SCHOOL AUTHORITY RESPONSE STATUS & TIME -------------------------------------------start
				
				long solved_within_4_7_days = 0, solved_within_2_3_days = 0, solved_within_8_30_days = 0,
						more_than_30_days = 0, no_measure_till_now = 0, instantly_Within_1_day = 0, not_known = 0;
				
				qString = "select count(SchoolEnvironment.took_step_Environment_prob) as authority_solved from " + table_name;
				qString += whereClause + " and SchoolEnvironment.took_step_Environment_prob like ? ";
				
				Logger.info("-----------------------------------------------------------");
				Logger.info("query3 string is : " + qString);
				Logger.info("-----------------------------------------------------------");
				
				queryForExecution = conn.prepareStatement(qString);	
				queryForExecution.setString(1, firstDateOfPreviousMonth);
				queryForExecution.setString(2, lastDateOfPreviousMonth);
				
				queryForExecution.setString(3, "%1%");
				try {
					
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						solved_within_4_7_days = rs.getInt("authority_solved");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%2%");
				try {
					rs = queryForExecution.executeQuery();
					
					while(rs.next()){
						solved_within_2_3_days = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%3%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						solved_within_8_30_days = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%4%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						more_than_30_days = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				queryForExecution.setString(3, "%5%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						no_measure_till_now = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%6%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						instantly_Within_1_day = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%7%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						not_known = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				long totalResponse = solved_within_4_7_days + solved_within_2_3_days + solved_within_8_30_days + 
						more_than_30_days + no_measure_till_now + instantly_Within_1_day + not_known;
				if(totalResponse != 0){
					solved_within_4_7_days = (solved_within_4_7_days * 100)/ totalResponse ; 
					solved_within_2_3_days = (solved_within_2_3_days * 100) / totalResponse; 
					solved_within_8_30_days = (solved_within_8_30_days * 100) / totalResponse; 
					more_than_30_days = (more_than_30_days * 100)/ totalResponse;
					no_measure_till_now = (no_measure_till_now * 100) / totalResponse;
					instantly_Within_1_day = (instantly_Within_1_day * 100) / totalResponse;
					not_known = (not_known * 100) / totalResponse;
				}
				Logger.info("solved_within_4_7_days : " + solved_within_4_7_days + " solved_within_2_3_days : " 
				+ solved_within_2_3_days + " solved_within_8_30_days : " + solved_within_8_30_days
				+ " more_than_30_days : " + more_than_30_days + " no_measure_till_now : " + no_measure_till_now
				+ " instantly_Within_1_day : " + instantly_Within_1_day + " not_known : " + not_known);

				//SCHOOL AUTHORITY RESPONSE STATUS & TIME -------------------------------------------end	
	
		Map<String, Long> mp = new HashMap<String, Long>();
		mp.put("boys", boys);mp.put("girls", girls);
		
		mp.put("total_school", boys_school + girls_school);
		
		mp.put("sufficient_seat_boys", sufficient_seat_boys);
		mp.put("complained_SchoolEnvironment_boys", complained_SchoolEnvironment_boys);
		
		mp.put("sufficient_seat_girls", sufficient_seat_girls);
		mp.put("complained_SchoolEnvironment_girls", complained_SchoolEnvironment_girls);
		
		mp.put("scareSafe_boys", scareSafe_boys);mp.put("scareSafe_girls", scareSafe_girls);
		
		mp.put("notSecure1", notSecure1);mp.put("notSecure2", notSecure2);mp.put("notSecure3", notSecure3);
		mp.put("notSecure4", notSecure4);mp.put("notSecure5", notSecure5);mp.put("notSecure6", notSecure6);
		
		mp.put("food1", food1);mp.put("food2", food2);mp.put("food3", food3);
		mp.put("food4", food4);mp.put("food5", food5);
		
		mp.put("issue1", issue1);mp.put("issue2", issue2);mp.put("issue3", issue3);
		mp.put("issue4", issue4);mp.put("issue5", issue5);
		
		mp.put("solved_within_4_7_days", solved_within_4_7_days);
		mp.put("solved_within_2_3_days", solved_within_2_3_days);
		mp.put("solved_within_8_30_days", solved_within_8_30_days);
		mp.put("more_than_30_days", more_than_30_days);
		mp.put("no_measure_till_now", no_measure_till_now);
		mp.put("instantly_Within_1_day", instantly_Within_1_day);
		mp.put("not_known", not_known);

		
		return mp;
	}

}
