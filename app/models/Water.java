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
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.bouncycastle.jce.provider.JDKDSASigner.noneDSA;

import com.google.gson.Gson;

import play.Logger;
import play.data.validation.Min;
import play.db.jpa.Model;
@Entity
public class Water extends Model{
	
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
	public String water_source = "";
	
	
	public String num_waterSource;
	
	public String activeWaterSource;
	
	public String is_potable = "";
	public String why_not_potable = "";
	public String why_not_potable_other = "";
	public String is_tank_cleaned = "";
	
	public String is_informed_authority_water_prob = "";
	public String how_informed_water_prob = "";
	public String how_informed_water_prob_other = "";
	public String water_prob_solved_authority = "";
	public String rank_water = "";
	
	
	public static Map<String, Long> getWaterData(Long divisionId,Long districtId, Long upazillaId, Long schoolId, Long studentType, Date startDate, Date endDate) throws SQLException{
		
		
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
		String whereClause = " Where Water.created_at between cast( ? as DateTime) and cast( ?  as DateTime)";
		String table_name = "Water";
		
		if(divisionId != null || districtId != null && upazillaId != null && schoolId != null && studentType != null){
			table_name = "Water join SchoolInformation on Water.school_id = SchoolInformation.id";
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
		
		qString = "select count(Water.id) as student, " +
				"count(distinct Water.school_id) as school, " +
				"sum(Water.num_waterSource) as total, " +
				"sum(Water.activeWaterSource) as active, " +
				"sum(Water.is_potable) as potable, " +
				"sum(Water.is_informed_authority_water_prob) as isInformed " +
				"from " + table_name;
		
		qString += whereClause + "  and Water.res_type = ?";
		
		Logger.info("query1 string is : " + qString);
		
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		queryForExecution.setString(1, firstDateOfPreviousMonth);
		queryForExecution.setString(2, lastDateOfPreviousMonth);
		
		
		
		long boys = 0, girls = 0,
			boys_school = 0, girls_school = 0,
			total_water_source_boys = 0,total_water_source_girls = 0,
			total_active_water_boys = 0, total_active_water_girls = 0,
			total_potable_water_boys = 0, total_potable_water_girls = 0,
			is_informed_authority_water_prob_boys = 0, is_informed_authority_water_prob_girls = 0;
		
		if(studentType == null || studentType == 1L){
			queryForExecution.setString(3, "1");
			try {
				rs = queryForExecution.executeQuery();
				while (rs.next()) {
					boys = rs.getInt("student");
					boys_school = rs.getInt("school");
					total_water_source_boys = rs.getInt("total");
					total_active_water_boys = rs.getInt("active");
					total_potable_water_boys = rs.getInt("potable");
					is_informed_authority_water_prob_boys = rs.getInt("isInformed");
				}
			} catch (SQLException e1) {
				Logger.info("Boy Exception");
				e1.printStackTrace();
			}
		}
		
		if(studentType == null || studentType == 1L){
			queryForExecution.setString(3, "2");
			try {
				rs = queryForExecution.executeQuery();
				while (rs.next()) {
					girls = rs.getInt("student");
					girls_school = rs.getLong("school");
					total_water_source_girls = rs.getInt("total");
					total_active_water_girls = rs.getInt("active");
					total_potable_water_girls = rs.getInt("potable");
					is_informed_authority_water_prob_girls = rs.getInt("isInformed");
				}
			} catch (SQLException e1) {
				Logger.info("Girls Exception");
				e1.printStackTrace();
			}
		}
		//Cause of Unsafe Dingking Water -------------------------------------------start
		
		long causeOne = 0, causeTwo = 0, causeThree = 0, causeFour = 0;
		
		qString = "select count(Water.why_not_potable) as cause from " + table_name;
		qString += whereClause + " and Water.why_not_potable like ? ";
		
		Logger.info("query2 string is : " + qString);
		
		queryForExecution = conn.prepareStatement(qString);	
		queryForExecution.setString(1, firstDateOfPreviousMonth);
		queryForExecution.setString(2, lastDateOfPreviousMonth);
		
		queryForExecution.setString(3, "%1%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				causeOne = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%2%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				causeTwo = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%3%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				causeThree = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%4%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				causeFour = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		long totalCause = causeOne + causeTwo + causeThree + causeFour;
		if(totalCause != 0){
			causeOne = (causeOne * 100)/ totalCause ; causeTwo = (causeTwo * 100) / totalCause; 
			causeThree = (causeThree * 100) / totalCause; causeFour = (causeFour * 100)/ totalCause;
		}
		
		Logger.info("causeOne : " + causeOne + " causeTwo : " + causeTwo + " causeThree : " + causeThree + " causeFour : " + causeFour);

		//Cause of Unsafe Dingking Water -------------------------------------------end
		
		
		//ISSUE RAISING MEDIUM / MECHANISM -------------------------------------------start
				long issueOne = 0, issueTwo = 0, issueThree = 0, issueFour = 0, issueFive = 0;
				
				qString = "select count(Water.how_informed_water_prob) as issue from " + table_name;
				qString += whereClause + " and Water.how_informed_water_prob like ?";
				
				Logger.info("query3 string is : " + qString);
				
				queryForExecution = conn.prepareStatement(qString);	
				queryForExecution.setString(1, firstDateOfPreviousMonth);
				queryForExecution.setString(2, lastDateOfPreviousMonth);
				
				queryForExecution.setString(3, "%1%");
				try {
					
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						issueOne = rs.getInt("issue");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%2%");
				try {
					rs = queryForExecution.executeQuery();
					
					while(rs.next()){
						issueTwo = rs.getInt("issue");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%3%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						issueThree = rs.getInt("issue");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%4%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						issueFour = rs.getInt("issue");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				queryForExecution.setString(3, "%5%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						issueFive = rs.getInt("issue");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				long totalIssue = issueOne + issueTwo + issueThree + issueFour + issueFive;
				if(totalIssue != 0){
					issueOne = (issueOne * 100)/ totalIssue ; issueTwo = (issueTwo * 100) / totalIssue; 
					issueThree = (issueThree * 100) / totalIssue; issueFour = (issueFour * 100)/ totalIssue;
					issueFive = (issueFive * 100) / totalIssue;
				}
				Logger.info("issueOne : " + issueOne + " issueTwo : " + issueTwo + " issueThree : " + issueThree + " issueFour : " + issueFour + " issueFive : " + issueFive);

				//ISSUE RAISING MEDIUM / MECHANISM -------------------------------------------end
				
				
				
				//SCHOOL AUTHORITY RESPONSE STATUS & TIME -------------------------------------------start
				
				long solved_within_4_7_days = 0, solved_within_2_3_days = 0, solved_within_8_30_days = 0,
						more_than_30_days = 0, no_measure_till_now = 0, instantly_Within_1_day = 0, not_known = 0;
				
				qString = "select count(Water.water_prob_solved_authority) as authority_solved from " + table_name;
				qString += whereClause + " and Water.water_prob_solved_authority like ? ";
				
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
				Logger.info("solved_within_4_7_days : " + solved_within_4_7_days + " solved_within_2_3_days : " + solved_within_2_3_days 
						+ " solved_within_8_30_days : " + solved_within_8_30_days + " more_than_30_days : " + more_than_30_days + 
						" no_measure_till_now : " + no_measure_till_now + "instantly_Within_1_day : " + instantly_Within_1_day +
						"not_known : " + not_known);

				//SCHOOL AUTHORITY RESPONSE STATUS & TIME -------------------------------------------end
				
	
		Map<String, Long> mp = new HashMap<String, Long>();
		
		mp.put("boys", boys);mp.put("girls", girls);
		
		mp.put("total_school", boys_school + girls_school);
		
		mp.put("total_water_source_boys", total_water_source_boys);mp.put("total_water_source_girls", total_water_source_girls);
		mp.put("total_active_water_boys", total_active_water_boys);mp.put("total_active_water_girls", total_active_water_girls);
		mp.put("total_potable_water_boys", total_potable_water_boys);mp.put("total_potable_water_girls", total_potable_water_girls);
		
		mp.put("is_informed_authority_water_prob_boys", is_informed_authority_water_prob_boys);mp.put("is_informed_authority_water_prob_girls", is_informed_authority_water_prob_girls);
		
		mp.put("causeOne", causeOne);mp.put("causeTwo", causeTwo);mp.put("causeThree", causeThree);mp.put("causeFour", causeFour);
		
		mp.put("issueOne", issueOne);mp.put("issueTwo", issueTwo);mp.put("issueThree", issueThree);mp.put("issueFour", issueFour);mp.put("issueFive", issueFive);
		
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
