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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import antlr.Lookahead;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class Sanitation extends Model{
	public Date date;
	
	
	@ManyToOne()
	public Data data;
	@ManyToOne()
	public Form form;
	
	@ManyToOne()
	public User sender;
	
	@ManyToOne()
	public SchoolInformation school;
	
	public String month = "";
	public String res_type = "";
	public String num_toilet_unusable = "";
	public String why_toilet_unusable = "";
	public String toilet_clean_interval = "";
	
	public String harpic = "";
	public String is_sanitation_prob_informed = "";
	public String how_informed_sanitation_prob = "";
	public String how_informed_sanitation_prob_other = "";
	
	public String took_step_sanitation_prob = "";
	public String rank_sanitation = "";
	
	
	public static Map<String, Long> getSanitationData(Long divisionId,Long districtId, Long upazillaId, Long schoolId, Long studentType, Date startDate, Date endDate) throws SQLException{
		
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
		String whereClause = " Where Sanitation.created_at between cast( ? as DateTime) and cast( ?  as DateTime)";
		String table_name = "Sanitation";
		//String table_name = "Sanitation join SchoolInformation on Sanitation.school_id = SchoolInformation.id";
		if(divisionId != null || districtId != null && upazillaId != null && schoolId != null && studentType != null){
			table_name = "Sanitation join SchoolInformation on Sanitation.school_id = SchoolInformation.id";
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
		
		qString = "select count(Sanitation.id) as student, " +
				"count(distinct Sanitation.school_id) as school, " +
				"sum(Sanitation.num_toilet_unusable) as inActive, " +
				"sum(Sanitation.is_sanitation_prob_informed) as isInformed " +
				"from " + table_name;
		
		qString += whereClause + " and Sanitation.res_type = ?";
		
		Logger.info("query1 string is : " + qString);
		
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		queryForExecution.setString(1, firstDateOfPreviousMonth);
		queryForExecution.setString(2, lastDateOfPreviousMonth);
		
		
		
		long boys = 0, girls = 0,
			boys_school = 0, girls_school = 0,
			total_activeOrInAcitve_toilet_boys = 0,total_activeOrInAcitve_toilet_girls = 0,
			is_sanitation_prob_informed_boys = 0, is_sanitation_prob_informed_girls = 0;
		
		if(studentType == null || studentType == 1L){
			queryForExecution.setString(3, "1");
			Logger.info("*****************************boys data *****************************************");
			try {
				
				rs = queryForExecution.executeQuery();
				
				while (rs.next()) {
					boys = rs.getInt("student");
					boys_school = rs.getInt("school");
					total_activeOrInAcitve_toilet_boys = rs.getInt("inActive");
					is_sanitation_prob_informed_boys = rs.getInt("isInformed");
				}
			} catch (SQLException e1) {
				Logger.info("Boy Exception");
				e1.printStackTrace();
			}
		}
		
		
		if(studentType == null || studentType == 2L){
			queryForExecution.setString(3, "2");
			Logger.info("*****************************girls data *****************************************");
			try {
				
				rs = queryForExecution.executeQuery();
				
				while (rs.next()) {
					girls = rs.getInt("student");
					girls_school = rs.getInt("school");
					total_activeOrInAcitve_toilet_girls = rs.getInt("inActive");
					is_sanitation_prob_informed_girls = rs.getInt("isInformed");
				}
			} catch (SQLException e1) {
				Logger.info("Girls Exception");
				e1.printStackTrace();
			}
		}
		//CAUSES OF UNUSABLE TOILETS -------------------------------------------start
		
		long cause1 = 0, cause2 = 0, cause3 = 0, cause4 = 0,cause5 = 0, cause6 = 0,
				cause7 = 0,cause8 = 0, cause9 = 0, cause10 = 0, cause11 = 0,cause12 = 0, cause13 = 0;
		
		qString = "select count(Sanitation.why_toilet_unusable) as cause from " + table_name;
		qString += whereClause + " and Sanitation.why_toilet_unusable like ? ";
		
		Logger.info("query2 string is : " + qString);
		
		queryForExecution = conn.prepareStatement(qString);	
		queryForExecution.setString(1, firstDateOfPreviousMonth);
		queryForExecution.setString(2, lastDateOfPreviousMonth);
		
		queryForExecution.setString(3, "%1%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause1 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%2%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				cause2 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%3%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause3 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%4%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause4 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%5%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause5 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%6%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				cause6 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%7%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause7 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%8%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause8 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%9%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause9 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%10%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				cause10 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%11%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause11 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%12%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause12 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(3, "%13%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause13 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		long totalCause = cause1 + cause2 + cause3 + cause4 + cause5 + cause6 + cause7 + cause8 + cause9 +
				cause10 + cause11 + cause12 + cause13;
		if(totalCause != 0){
			cause1 = (cause1 * 100)/ totalCause ; cause2 = (cause2 * 100) / totalCause; 
			cause3 = (cause3 * 100) / totalCause; cause4 = (cause4 * 100)/ totalCause;
			cause5 = (cause5 * 100)/ totalCause ; cause6 = (cause6 * 100) / totalCause; 
			cause7 = (cause7 * 100) / totalCause; cause8 = (cause8 * 100)/ totalCause;
			cause9 = (cause9 * 100)/ totalCause ; cause10 = (cause10 * 100) / totalCause; 
			cause11 = (cause11 * 100) / totalCause; cause12 = (cause12 * 100)/ totalCause;
			cause13 = (cause13 * 100) / totalCause;
		}
		
		Logger.info("cause1 : " + cause1 + " cause2 : " + cause2 + " cause3 : " + 
				cause3 + " cause4 : " + cause4 + "cause5 : " + cause5 + " cause6 : " + cause6 + " cause7 : " + 
				cause7 + " cause8 : " + cause8 + "cause9 : " + cause9 + " cause10 : " + cause10 + " cause11 : " + 
				cause11 + " cause12 : " + cause12 + "cause13 : " + cause13);

		//Cause of Unsafe Dingking Sanitation -------------------------------------------end
		
		
		//ISSUE RAISING MEDIUM / MECHANISM -------------------------------------------start
				long issue1 = 0, issue2 = 0, issue3 = 0, issue4 = 0, issue5 = 0;
				
				qString = "select count(Sanitation.how_informed_sanitation_prob) as issue from " + table_name;
				qString += whereClause + " and Sanitation.how_informed_sanitation_prob like ?";
				
				Logger.info("query3 string is : " + qString);
				
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
				
				qString = "select count(Sanitation.took_step_sanitation_prob) as authority_solved from " + table_name;
				qString += whereClause + " and Sanitation.took_step_sanitation_prob like ? ";
				
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
		
		mp.put("total_activeOrInAcitve_toilet_boys", total_activeOrInAcitve_toilet_boys);
		mp.put("total_activeOrInAcitve_toilet_girls", total_activeOrInAcitve_toilet_girls);
		
		mp.put("is_sanitation_prob_informed_boys", is_sanitation_prob_informed_boys);
		mp.put("is_sanitation_prob_informed_girls", is_sanitation_prob_informed_girls);
		
		mp.put("cause1", cause1);mp.put("cause2", cause2);mp.put("cause3", cause3);
		mp.put("cause4", cause4);mp.put("cause5", cause5);mp.put("cause6", cause6);
		mp.put("cause7", cause7);mp.put("cause8", cause8);mp.put("cause9", cause9);
		mp.put("cause10", cause10);mp.put("cause11", cause11);mp.put("cause12", cause12);
		mp.put("cause13", cause13);
		
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
