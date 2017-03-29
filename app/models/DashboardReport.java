package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;

import com.sun.org.apache.bcel.internal.generic.NEW;

import play.Logger;
import play.Play;

/**
 * Status of comment
 * */
public class DashboardReport {
	
	public static Map<String, Long> mp = new HashMap<String, Long>();

	public static Map<String, Long> getDashboardData() throws SQLException {
		Logger.info("DasBoardReport");
		long total_school = 0, total_boys = 0, total_girls = 0, total_Toilets = 0, male_toilet = 0, female_toilet = 0,
				boys_toilet_ratio = 0, girls_toilet_ratio = 0, school_forum = 0, organized_annual_sports = 0;
		
		String table_name = "SchoolInformation";
		
		String qString = " select count(id) as totalSchool, "+
		                 " sum(maleStruden) as maleStudent, "+
				         " sum(femailStudent) as femaleStudent, "+
				         " sum(maleToilets) as maleToilet, "+
				         " sum(femailToilets) as femaleToilet, "+
				         " sum(countForum) as countForum, "+
				         " (select count(annualSports) as annualSports from SchoolInformation WHERE annualSports='1') as annualSports "+
				         " from ";
		
		
		qString += table_name ;
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		
		Logger.info("query: " + qString);
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				total_school = rs.getInt("totalSchool");
				total_boys = rs.getInt("maleStudent");
				total_girls = rs.getInt("femaleStudent");
				male_toilet = rs.getInt("maleToilet");
				female_toilet = rs.getInt("femaleToilet");
				organized_annual_sports= rs.getInt("annualSports");
				school_forum = rs.getInt("countForum");
			}
		} catch (Exception e) {
			//Logger.info("whole lot of exception");
		}
		Logger.info("organized_annual_sports: " + organized_annual_sports);
		total_Toilets = male_toilet	+ female_toilet;
		//Logger.info("----------------------------------------------------");
		//Logger.info("total_School : " + total_school + " male toilet : " + male_toilet + " female toilet : " + female_toilet + " total toilet : " + total_Toilets);
		//Logger.info("----------------------------------------------------");
		if(total_boys != 0L)
			boys_toilet_ratio = (male_toilet * 100) / total_boys;
		
		if(total_girls != 0L)
			girls_toilet_ratio = (female_toilet * 100) / total_girls;
		
		
		
		mp.put("total_school", total_school);
		mp.put("total_boys", total_boys);
		mp.put("total_girls", total_girls);
		mp.put("total_Toilets", total_Toilets);
		mp.put("boys_toilet_ratio", boys_toilet_ratio);
		mp.put("girls_toilet_ratio", girls_toilet_ratio);
		mp.put("school_forum", school_forum);
		mp.put("organized_annual_sports", organized_annual_sports);
		
		
		getWaterCauses();
		getSanitationCauses();
		getSchoolEnvironmentCauses();
		getSportsAndRecreationCauses();
		
		waterIssue();
		sanitationIssue();
		schoolEnvironmentIssue();
		return mp;
	}
	
	
	
	public static void getWaterCauses() throws SQLException {
		long causeOne = 0, causeTwo = 0, causeThree = 0, causeFour = 0;
		String table_name = "Water";
		
		String qString = "select count(Water.why_not_potable) as cause from " + table_name;
		qString += " Where Water.why_not_potable like ? ";
		
		//Logger.info("----------------------------------------------------");
		//Logger.info("query2 string is : " + qString);
		
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		queryForExecution = conn.prepareStatement(qString);	

		
		queryForExecution.setString(1, "%1%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				causeOne = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%2%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				causeTwo = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%3%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				causeThree = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%4%");
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
		
		mp.put("waterCause1", causeOne);mp.put("waterCause2", causeTwo);mp.put("waterCause3", causeThree);
		mp.put("waterCause4", causeFour);
		
		//Logger.info("causeOne : " + causeOne + " causeTwo : " + causeTwo + " causeThree : " + causeThree + " causeFour : " + causeFour);
		//Logger.info("----------------------------------------------------");
	}
	
	
	public static void getSanitationCauses() throws SQLException {
		long cause1 = 0, cause2 = 0, cause3 = 0, cause4 = 0,cause5 = 0, cause6 = 0,
				cause7 = 0,cause8 = 0, cause9 = 0, cause10 = 0, cause11 = 0,cause12 = 0, cause13 = 0;
		String table_name = "Sanitation";

		String qString = "select count(Sanitation.why_toilet_unusable) as cause from " + table_name;
		qString += " where Sanitation.why_toilet_unusable like ? ";
		
		//Logger.info("----------------------------------------------------");
		//Logger.info("query2 string is : " + qString);

		
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		queryForExecution = conn.prepareStatement(qString);	
		
		queryForExecution.setString(1, "%1%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause1 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%2%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				cause2 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%3%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause3 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%4%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause4 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%5%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause5 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%6%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				cause6 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%7%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause7 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%8%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause8 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%9%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause9 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%10%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				cause10 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%11%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause11 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%12%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause12 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%13%");
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
		
		List<Long> causes = new ArrayList<>();
		causes.add(cause1);causes.add(cause2);causes.add(cause3);causes.add(cause4);causes.add(cause5);
		causes.add(cause6);causes.add(cause7);causes.add(cause8);causes.add(cause9);causes.add(cause10);
		causes.add(cause11);causes.add(cause12);causes.add(cause13);
		
		Map<String, Long> data = getTopFive(causes);
		
		for(String key : data.keySet()){
			mp.put("sanitationCause" + key, data.get(key));
		}
		
		//Logger.info("cause1 : " + cause1 + " cause2 : " + cause2 + " cause3 : " + 
				//cause3 + " cause4 : " + cause4 + "cause5 : " + cause5 + " cause6 : " + cause6 + " cause7 : " + 
				//cause7 + " cause8 : " + cause8 + "cause9 : " + cause9 + " cause10 : " + cause10 + " cause11 : " + 
				//cause11 + " cause12 : " + cause12 + "cause13 : " + cause13);

		//Logger.info("----------------------------------------------------");
	}
	
	
	
	public static void getSchoolEnvironmentCauses() throws SQLException {
		long scare1 = 0, scare2 = 0, scare3 = 0, scare4 = 0, scare5 = 0, scare6 = 0;

		String table_name = "SchoolEnvironment";
		
		String qString = "select count(SchoolEnvironment.scareSafe_SchoolWay) as scareCause from " + table_name;
		qString += " where SchoolEnvironment.scareSafe_SchoolWay like ? ";
		
		//Logger.info("-----------------------------------------------------------");
		//Logger.info("query3 string is : " + qString);

		
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		
		queryForExecution.setString(1, "%1%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				scare1 = rs.getInt("scareCause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%2%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				scare2 = rs.getInt("scareCause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%3%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				scare3 = rs.getInt("scareCause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%4%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				scare4 = rs.getInt("scareCause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%5%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				scare5 = rs.getInt("scareCause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		queryForExecution.setString(1, "%6%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				scare6 = rs.getInt("scareCause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		long totalScare = scare1 + scare2 + scare3 + scare4 + scare5 + scare6;
		if(totalScare != 0){
			scare1 = (scare1 * 100)/ totalScare ; scare2 = (scare2 * 100) / totalScare; 
			scare3 = (scare3 * 100) / totalScare; scare4 = (scare4 * 100)/ totalScare;
			scare5 = (scare5 * 100)/ totalScare; scare6 = (scare6 * 100) / totalScare; 
		}
		
		List<Long> causes = new ArrayList<>();
		causes.add(scare1);causes.add(scare2);causes.add(scare3);causes.add(scare4);causes.add(scare5);
		causes.add(scare6);
		
		Map<String, Long> data = getTopFive(causes);
		
		for(String key : data.keySet()){
			mp.put("schoolEnvironmentCause" + key, data.get(key));
		}
		
		/*mp.put("schoolEnvironmentCause1", scare1);mp.put("schoolEnvironmentCause2", scare2);
		mp.put("schoolEnvironmentCause3", scare3);mp.put("schoolEnvironmentCause4", scare4);
		mp.put("schoolEnvironmentCause5", scare5);mp.put("schoolEnvironmentCause6", scare6);*/
		
		//Logger.info("scare1 : " + scare1 + " scare2 : " + scare2 + " scare3 : " + 
				//scare3 + " scare4 : " + scare4 + " scare5 : " + scare5 + " scare6 : " + scare6);
		
		//Logger.info("----------------------------------------------------");
	}
	
	
	
	public static void getSportsAndRecreationCauses() throws SQLException {
		
		long cause1 = 0, cause2 = 0, cause3 = 0, cause4 = 0, cause5 = 0, cause6 = 0, cause7 = 0, cause8 = 0;
		String table_name = "SportsRecreation";
		
		String qString = "select count(SportsRecreation.whyNot_EqualAccess) as cause from " + table_name;
		qString += " where SportsRecreation.whyNot_EqualAccess like ? ";
		
		
		//Logger.info("----------------------------------------------------");
		//Logger.info("query2 string is : " + qString);

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		
		queryForExecution.setString(1, "%1%");
		try {
			
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause1 = rs.getInt("cause");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%2%");
		try {
			rs = queryForExecution.executeQuery();
			
			while(rs.next()){
				cause2 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%3%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause3 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%4%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause4 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%5%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause5 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%6%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause6 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%7%");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				cause7 = rs.getInt("cause");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		queryForExecution.setString(1, "%8%");
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
			cause3 = (cause3 * 100) / totalCause; cause4 = (cause4 * 100) / totalCause;
			cause5 = (cause5 * 100)/ totalCause ; cause6 = (cause6 * 100) / totalCause; 
			cause7 = (cause7 * 100) / totalCause; cause8 = (cause8 * 100) / totalCause;
		}
		
		List<Long> causes = new ArrayList<>();
		causes.add(cause1);causes.add(cause2);causes.add(cause3);causes.add(cause4);causes.add(cause5);
		causes.add(cause6);causes.add(cause7);causes.add(cause8);
		
		Map<String, Long> data = getTopFive(causes);
		
		for(String key : data.keySet()){
			mp.put("sportsAndRecreationCause" + key, data.get(key));
		}
		
		//Logger.info("cause1 : " + cause1 + " cause2 : " + cause2 + " cause3 : " + cause3 + " cause4 : " + cause4
				//+ " cause5 : " + cause5 + " cause6 : " + cause6 + "cause7 : " + cause7 + " cause8 : " + cause8);
		//Logger.info("----------------------------------------------------");
	}
	
	
	public static Map<String, Long> getTopFive(List<Long> data){
		
		List<Integer> keys = new ArrayList<>();
		List<Long> values = new ArrayList<>();
		
		int taken = 0;
		
		while(taken < 5){
			int index = 0;
			long value = data.get(index);
			
			for (int j = 0; j < data.size(); j++) {
				if(value < data.get(j)){
					value = data.get(j);
					index = j;
				}
			}
			taken++;
			keys.add(index+1);
			data.set(index, -1L);
			values.add(value);
		}
		
		Map<String, Long> result = new HashMap<>();

		for(int i = 0; i < 5; i++){
			result.put("" + keys.get(i), values.get(i));
		}
		return result;
	}
	
	public static void waterIssue() throws SQLException{
		String table_name = "Water";
		
		String qString = "select count(*) as waterIssueUnsolved from " + table_name;
		qString += " where Water.water_prob_solved_authority > ?";
		
		Connection conn = play.db.DB.getConnection();
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		ResultSet rs = null;
		
		long waterIssueResolved = 0, waterIssueUnsolved = 0;
		queryForExecution.setString(1, "5");
		try {
			rs = queryForExecution.executeQuery();
			if(rs.next()){
				waterIssueUnsolved = rs.getInt("waterIssueUnsolved");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		qString = "select count(*) as waterIssueResolved from " + table_name;
		qString += " where Water.water_prob_solved_authority < ?";
		queryForExecution = conn.prepareStatement(qString);
		
		queryForExecution.setString(1, "6");
		try {
			rs = queryForExecution.executeQuery();
			if(rs.next()){
				waterIssueResolved = rs.getInt("waterIssueResolved");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		//Logger.info("**********************************************************");
		//Logger.info("waterIssueResolve : " + waterIssueResolved + " waterIssueUnsolved : " + waterIssueUnsolved);
		//Logger.info("**********************************************************");
		
		waterIssueResolved = (waterIssueResolved * 100) / (waterIssueResolved + waterIssueUnsolved);
		//Logger.info("tessttttttttt : " + waterIssueResolved);
		mp.put("waterResolved", waterIssueResolved);
	}
	
	public static void sanitationIssue() throws SQLException{
		String table_name = "Sanitation";
		
		String qString = "select count(*) as sanitationIssueUnresolved from " + table_name;
		qString += " where Sanitation.took_step_sanitation_prob > ?";
		
		
		Connection conn = play.db.DB.getConnection();
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		ResultSet rs = null;
		
		long sanitationIssueResolved = 0, sanitationIssueUnresolved = 0;
		queryForExecution.setString(1, "5");
		try {
			rs = queryForExecution.executeQuery();
			if(rs.next()){
				sanitationIssueUnresolved = rs.getInt("sanitationIssueUnresolved");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		qString = "select count(*) as sanitationIssueResolved from " + table_name;
		qString += " where Sanitation.took_step_sanitation_prob < ?";
		
		queryForExecution = conn.prepareStatement(qString);
		queryForExecution.setString(1, "6");
		try {
			rs = queryForExecution.executeQuery();
			if(rs.next()){
				sanitationIssueResolved = rs.getInt("sanitationIssueResolved");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//Logger.info("**********************************************************");
		//Logger.info("sanitationIssueResolved : " + sanitationIssueResolved + " sanitationIssueUnresolved : " + sanitationIssueUnresolved);
		//Logger.info("**********************************************************");
		if((sanitationIssueResolved + sanitationIssueUnresolved)!=0){
		sanitationIssueResolved = (sanitationIssueResolved * 100) / (sanitationIssueResolved + sanitationIssueUnresolved);
		//Logger.info("tessttttttttt : " + sanitationIssueResolved);
		mp.put("sanitationIssueResolved", sanitationIssueResolved);
		}
	}
	
	public static void schoolEnvironmentIssue() throws SQLException{
		String table_name = "SchoolEnvironment";
		
		String qString = "select count(*) as schoolEnvironmentIssueUnresolved from " + table_name;
		qString += " where SchoolEnvironment.took_step_Environment_prob > ?";
		
		Connection conn = play.db.DB.getConnection();
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		ResultSet rs = null;
		
		long schoolEnvironmentIssueResolved = 0, schoolEnvironmentIssueUnresolved = 0;
		
		queryForExecution.setString(1, "5");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				schoolEnvironmentIssueUnresolved = rs.getInt("schoolEnvironmentIssueUnresolved");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		qString = "select count(*) as schoolEnvironmentIssueResolved from " + table_name;
		qString += " where SchoolEnvironment.took_step_Environment_prob < ?";
		
		queryForExecution = conn.prepareStatement(qString);
		
		
	
		queryForExecution.setString(1, "6");
		try {
			rs = queryForExecution.executeQuery();
			while(rs.next()){
				schoolEnvironmentIssueResolved = rs.getInt("schoolEnvironmentIssueResolved");
			}
		} catch (Exception e) {
			
		}
		
		//Logger.info("**********************************************************");
		//Logger.info("schoolEnvironmentIssueResolved : " + schoolEnvironmentIssueResolved + " schoolEnvironmentIssueUnresolved : " + schoolEnvironmentIssueUnresolved);
		//Logger.info("**********************************************************");
		
		if((schoolEnvironmentIssueResolved + schoolEnvironmentIssueUnresolved)!=0){
		schoolEnvironmentIssueResolved = (schoolEnvironmentIssueResolved * 100) / (schoolEnvironmentIssueResolved + schoolEnvironmentIssueUnresolved);
		//Logger.info("tessttttttttt : " + schoolEnvironmentIssueResolved);
		mp.put("schoolEnvironmentIssueResolved", schoolEnvironmentIssueResolved);
		}
	}
	public static void TotalIssue(){}
	
	
	public static Map<String, Long> getAllIssueStatus() {
		return null;
	}
}
