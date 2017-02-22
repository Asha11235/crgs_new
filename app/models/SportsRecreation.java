package models;

import java.awt.MultipleGradientPaint.CycleMethod;
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
public class SportsRecreation extends Model{

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
	public String facilitiesAvailable = "";
	public String instrumentUsable = "";
	public String instrumentEqualAccess = "";
	
	public String schoolType = "";
	
	public String whyNot_EqualAccess = "";
	public String sportsTeacher = "";
	public String lastMonth_Activity = "";
	public String rank_SportsRecreation = "";
	
	
	
	public static Map<String, Long> getSportsRecreationData(Long divisionId,Long districtId, Long upazillaId, Long schoolId, Long studentType, Date startDate, Date endDate) throws SQLException{
		
		
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
		String whereClause = " Where SportsRecreation.created_at between cast( ? as DateTime) and cast( ? as DateTime)";
		String table_name = "SportsRecreation";
		
		if(divisionId != null || districtId != null && upazillaId != null && schoolId != null && studentType != null){
			table_name = "SportsRecreation join SchoolInformation on SportsRecreation.school_id = SchoolInformation.id";
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
		
		qString = "select count(SportsRecreation.id) as student, " +
				"count(distinct SportsRecreation.school_id) as school, " +
				"sum(SportsRecreation.instrumentUsable) as instrumentUsable, " +
				"sum(SportsRecreation.instrumentEqualAccess) as instrumentEqualAccess " +
				"from " + table_name;
		
		qString += whereClause + "  and SportsRecreation.res_type = ?";
		
		Logger.info("query1 string is : " + qString);
		
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		queryForExecution.setString(1, firstDateOfPreviousMonth);
		queryForExecution.setString(2, lastDateOfPreviousMonth);
		
		
		
		long boys = 0, girls = 0,
			boys_school = 0, girls_school = 0,
			instrumentUsable_boys = 0, instrumentUsable_girls = 0,
			instrumentEqualAccess_boys = 0, instrumentEqualAccess_girls = 0;

		if(studentType == null || studentType == 1L){
			queryForExecution.setString(3, "1");
			try {
				
				rs = queryForExecution.executeQuery();
				
				while (rs.next()) {
					boys = rs.getInt("student");
					boys_school = rs.getInt("school");
					instrumentUsable_boys = rs.getInt("instrumentUsable");
					instrumentUsable_boys = rs.getInt("instrumentEqualAccess");
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
					instrumentUsable_girls = rs.getInt("instrumentUsable");
					instrumentUsable_girls = rs.getInt("instrumentEqualAccess");
				}
			} catch (SQLException e1) {
				Logger.info("Girls Exception");
				e1.printStackTrace();
			}
		}
		//CAUSES OF NOT HAVING EQUAL ACCESS TO THE INSTRUMENTS -------------------------------------------start
		
		long cause1 = 0, cause2 = 0, cause3 = 0, cause4 = 0, cause5 = 0, cause6 = 0, cause7 = 0, cause8 = 0;
		
		qString = "select count(SportsRecreation.whyNot_EqualAccess) as cause from " + table_name;
		qString += whereClause + " and SportsRecreation.whyNot_EqualAccess like ? ";
		
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
		long totalCause = cause1 + cause2 + cause3 + cause4 + cause5 + cause6 + cause7 + cause8;
		if(totalCause != 0){
			cause1 = (cause1 * 100)/ totalCause ; cause2 = (cause2 * 100) / totalCause; 
			cause3 = (cause3 * 100) / totalCause; cause4 = (cause4 * 100)/ totalCause;
			cause5 = (cause5 * 100)/ totalCause ; cause6 = (cause6 * 100) / totalCause; 
			cause7 = (cause7 * 100) / totalCause; cause8 = (cause8 * 100)/ totalCause;
		}
		
		Logger.info("cause1 : " + cause1 + " cause2 : " + cause2 + " cause3 : " + cause3 + " cause4 : " + cause4
				+ " cause5 : " + cause5 + " cause6 : " + cause6 + "cause7 : " + cause7 + " cause8 : " + cause8);

		//CAUSES OF NOT HAVING EQUAL ACCESS TO THE INSTRUMENTS -------------------------------------------end
		
		
		//FACILITIES AVAILABLE AT SCHOOL FOR PHYSICAL & MENTAL HEALTH IMPROVEMENT----------start
				long facility1 = 0, facility2 = 0, facility3 = 0, facility4 = 0, facility5 = 0, facility6 = 0,
						facility7 = 0;
				
				qString = "select count(SportsRecreation.facilitiesAvailable) as facility from " + table_name;
				qString += whereClause + " and SportsRecreation.facilitiesAvailable like ?";
				
				Logger.info("query3 string is : " + qString);
				
				queryForExecution = conn.prepareStatement(qString);	
				queryForExecution.setString(1, firstDateOfPreviousMonth);
				queryForExecution.setString(2, lastDateOfPreviousMonth);
				
				queryForExecution.setString(3, "%1%");
				try {
					
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						facility1 = rs.getInt("facility");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%2%");
				try {
					rs = queryForExecution.executeQuery();
					
					while(rs.next()){
						facility2 = rs.getInt("facility");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%3%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						facility3 = rs.getInt("facility");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%4%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						facility4 = rs.getInt("facility");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				queryForExecution.setString(3, "%5%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						facility5 = rs.getInt("facility");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%6%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						facility6 = rs.getInt("facility");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%7%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						facility7 = rs.getInt("facility");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				long totalfacility = facility1 + facility2 + facility3 + facility4 + facility5 + facility6 + facility7;
				if(totalfacility != 0){
					facility1 = (facility1 * 100) / totalfacility; facility2 = (facility2 * 100) / totalfacility; 
					facility3 = (facility3 * 100) / totalfacility; facility4 = (facility4 * 100) / totalfacility;
					facility5 = (facility5 * 100) / totalfacility; facility6 = (facility6 * 100) / totalfacility;
					facility7 = (facility7 * 100) / totalfacility;
				}
				Logger.info("facility1 : " + facility1 + " facility2 : " + facility2 + " facility3 : " + facility3 + 
						" facility4 : " + facility4 + " facility5 : " + facility5 + " facility6 : " + facility6 + " facility7 : " + facility7);

				//ISSUE RAISING MEDIUM / MECHANISM -------------------------------------------end
				
				
				
				//EVENT / ACTIVITY ORGANIZED IN SCHOOL -------------------------------------------start
				
				long event1 = 0, event2 = 0, event3 = 0, event4 = 0, event5 = 0, event6 = 0, event7 = 0;
				
				qString = "select count(SportsRecreation.lastMonth_Activity) as lastMonth_Activity from " + table_name;
				qString += whereClause + " and SportsRecreation.lastMonth_Activity like ? ";
				
				queryForExecution = conn.prepareStatement(qString);	
				queryForExecution.setString(1, firstDateOfPreviousMonth);
				queryForExecution.setString(2, lastDateOfPreviousMonth);
				
				queryForExecution.setString(3, "%1%");
				try {
					
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						event1 = rs.getInt("authority_solved");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%2%");
				try {
					rs = queryForExecution.executeQuery();
					
					while(rs.next()){
						event2 = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%3%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						event3 = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%4%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						event4 = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				queryForExecution.setString(3, "%5%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						event5 = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%6%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						event6 = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				queryForExecution.setString(3, "%7%");
				try {
					rs = queryForExecution.executeQuery();
					while(rs.next()){
						event7 = rs.getInt("authority_solved");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				long totalEvent = event1 + event2 + event3 + event4 + event5 + event6 + event7;
				if(totalEvent != 0){
					event1 = (event1 * 100)/ totalEvent ; event2 = (event2 * 100) / totalEvent; 
					event3 = (event3 * 100) / totalEvent; event4 = (event4 * 100)/ totalEvent;
					event5 = (event5 * 100) / totalEvent; event6 = (event6 * 100) / totalEvent;
					event7 = (event7 * 100) / totalEvent;
				}
				Logger.info("event1 : " + event1 + " event2 : " + event2 + " event3 : " + event3 + " event4 : " 
				+ event4 + " event5 : " + event5 + " event6 : " + event6 + " event7 : " + event7);

				//EVENT / ACTIVITY ORGANIZED IN SCHOOL -------------------------------------------end
				
	
		Map<String, Long> mp = new HashMap<String, Long>();
		mp.put("boys", boys);mp.put("girls", girls);
		
		mp.put("total_school", boys_school + girls_school);
		
		mp.put("instrumentUsable_boys", instrumentUsable_boys);
		mp.put("instrumentUsable_girls", instrumentUsable_girls);
		mp.put("instrumentEqualAccess_boys", instrumentEqualAccess_boys);
		mp.put("instrumentEqualAccess_girls", instrumentEqualAccess_girls);
		
		mp.put("cause1", cause1);mp.put("cause2", cause2);mp.put("cause3", cause3);mp.put("cause4", cause4);
		mp.put("cause5", cause5);mp.put("cause6", cause6);mp.put("cause7", cause7);mp.put("cause8", cause8);
		
		mp.put("facility1", facility1);mp.put("facility2", facility2);mp.put("facility3", facility3);
		mp.put("facility4", facility4);
		mp.put("facility5", facility5);mp.put("facility6", facility6);mp.put("facility7", facility7);
		
		mp.put("event1", event1);mp.put("event2", event2);mp.put("event3", event3);mp.put("event4", event4);
		mp.put("event5", event5);mp.put("event6", event6);mp.put("event7", event7);
		return mp;
	}

	
}
