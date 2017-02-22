package utils;

import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;

import models.Data;
import models.Form;
import models.Sanitation;
import models.SchoolEnvironment;
import models.SchoolInformation;
import models.SportsRecreation;
import models.UnitData;
import models.User;
import models.Water;

public class WaterAdapter {
	
	public  static void insetIntoWater(Data data, User user){
		List<UnitData> listUnitData = UnitData.find(
				"SELECT u from UnitData u where u.data =? order by id", data)
				.fetch();
		
		
		String month = "";
		String res_type = "";
		
		String water_source = "";
		String num_waterSource = "";
		String activeWaterSource = "";
		
		String is_potable = "";
		String why_not_potable = "";
		String why_not_potable_other = "";
		String is_tank_cleaned = "";
		
		String is_informed_authority_water_prob = "";
		String how_informed_water_prob = "";
		String how_informed_water_prob_other = "";
		String water_prob_solved_authority = "";
		String rank_water = "";
		
		for(UnitData unit : listUnitData){
			if(unit.titleVar.equals("data/month")){
				month = unit.valueVar;
			}
			if(unit.titleVar.equals("data/res_type")){
				res_type = unit.valueVar;
			}
			if(unit.titleVar.equals("data/water_source")){
				water_source = unit.valueVar;
			}
			if(unit.titleVar.equals("data/num_waterSource")){
				num_waterSource = unit.valueVar;
			}
			if(unit.titleVar.equals("data/activeWaterSource")){
				activeWaterSource = unit.valueVar;
			}
			if(unit.titleVar.equals("data/is_potable")){
				is_potable = unit.valueVar;
			}
			
			if(unit.titleVar.equals("data/why_not_potable")){
				why_not_potable = unit.valueVar;
			}
			if(unit.titleVar.equals("data/why_not_potable_other")){
				why_not_potable_other = unit.valueVar;
			}
			if(unit.titleVar.equals("data/is_tank_cleaned")){
				is_tank_cleaned = unit.valueVar;
			}
			if(unit.titleVar.equals("data/is_informed_authority_water_prob")){
				is_informed_authority_water_prob = unit.valueVar;
			}
			if(unit.titleVar.equals("data/how_informed_water_prob")){
				how_informed_water_prob = unit.valueVar;
			}
			
			if(unit.titleVar.equals("data/how_informed_water_prob_other")){
				how_informed_water_prob_other = unit.valueVar;
			}
			if(unit.titleVar.equals("data/water_prob_solved_authority")){
				water_prob_solved_authority = unit.valueVar;
			}
			if(unit.titleVar.equals("data/rank_water")){
				rank_water = unit.valueVar;
			}
			
		}
		
		Water water = new Water();
		water.date = new Date();
		water.data = data;
		water.form = data.form;
		water.sender = data.sender;
		water.school = data.schools;
		
		water.month = month;
		water.res_type = res_type;
		water.water_source = water_source;
		water.num_waterSource = num_waterSource;
		water.activeWaterSource = activeWaterSource;
		
		water.is_potable = is_potable;
		water.why_not_potable = why_not_potable;
		water.why_not_potable_other = why_not_potable_other;
		water.is_tank_cleaned = is_tank_cleaned;
		
		water.is_informed_authority_water_prob = is_informed_authority_water_prob;
		water.how_informed_water_prob = how_informed_water_prob;
		water.how_informed_water_prob_other = how_informed_water_prob_other;
		water.water_prob_solved_authority = water_prob_solved_authority;
		water.rank_water = rank_water;
		
		water.save();
	}
	 
	public static void insetIntoSanitation(Data data, User user){
			List<UnitData> listUnitData = UnitData.find(
					"SELECT u from UnitData u where u.data =? order by id", data)
					.fetch();
			
			String res_type = "";
			String month = "";
			String num_toilet_unusable = "";
			String why_toilet_unusable = "";
			String toilet_clean_interval = "";
			
			String harpic = "";
			String is_sanitation_prob_informed = "";
			String how_informed_sanitation_prob = "";
			String how_informed_sanitation_prob_other = "";
			
			String took_step_sanitation_prob = "";
			String rank_sanitation = "";
			
			
			for(UnitData unit : listUnitData){
				if(unit.titleVar.equals("data/month")){
					month = unit.valueVar;
				}
				if(unit.titleVar.equals("data/res_type")){
					res_type = unit.valueVar;
				}
				if(unit.titleVar.equals("data/num_toilet_unusable")){
					num_toilet_unusable = unit.valueVar;
				}
				if(unit.titleVar.equals("data/why_toilet_unusable")){
					why_toilet_unusable = unit.valueVar;
				}
				if(unit.titleVar.equals("data/toilet_clean_interval")){
					toilet_clean_interval = unit.valueVar;
				}
				if(unit.titleVar.equals("data/harpic")){
					harpic = unit.valueVar;
				}
				if(unit.titleVar.equals("data/is_sanitation_prob_informed")){
					is_sanitation_prob_informed = unit.valueVar;
				}
				if(unit.titleVar.equals("data/how_informed_sanitation_prob")){
					how_informed_sanitation_prob = unit.valueVar;
				}
				if(unit.titleVar.equals("data/how_informed_sanitation_prob_other")){
					how_informed_sanitation_prob_other = unit.valueVar;
				}
				if(unit.titleVar.equals("data/took_step_sanitation_prob")){
					took_step_sanitation_prob = unit.valueVar;
				}
				if(unit.titleVar.equals("data/rank_sanitation")){
					rank_sanitation = unit.valueVar;
				}
			}
			
			Sanitation sanitation = new Sanitation();
			sanitation.date = new Date();
			sanitation.data = data;
			sanitation.form = data.form;
			sanitation.sender = data.sender;
			sanitation.school = data.schools;
			
			sanitation.month = month;
			sanitation.res_type = res_type;
			sanitation.num_toilet_unusable = num_toilet_unusable;
			sanitation.why_toilet_unusable = why_toilet_unusable;
			sanitation.toilet_clean_interval = toilet_clean_interval;
			
			sanitation.harpic = harpic;
			sanitation.is_sanitation_prob_informed = is_sanitation_prob_informed;
			sanitation.how_informed_sanitation_prob = how_informed_sanitation_prob;
			sanitation.how_informed_sanitation_prob_other = how_informed_sanitation_prob_other;
			
			sanitation.took_step_sanitation_prob = took_step_sanitation_prob;
			sanitation.rank_sanitation = rank_sanitation;
			
			sanitation.save();
		}
	 
	public static void insetIntoSchoolEnvironment(Data data, User user){
		 List<UnitData> listUnitData = UnitData.find(
					"SELECT u from UnitData u where u.data =? order by id", data)
					.fetch();
		
		 	String month = "";
		 	String res_type = "";
			String cleanInterval_SchoolYard = "";
			String cleanInterval_ClassRoom = "";
			String StuHearTeacher = "";
			
			String stuSeatArrange = "";
			String teacherStage = "";
			String complained_SchoolEnvironment = "";
			String how_informed_Environment_prob = "";
			
			String how_informed_Environment_prob_other = "";
			String took_step_Environment_prob = "";
			String rank_Environment = "";
			String rank_Edu_Quality = "";
			
			String scareSafe_SchoolWay = "";
			String yhy_not_feel_safe = "";
			String schoolCanteen = "";
		 
			for(UnitData unit : listUnitData){
				if(unit.titleVar.equals("data/month")){
					month = unit.valueVar;
				}
				if(unit.titleVar.equals("data/res_type")){
					res_type = unit.valueVar;
				}
				if(unit.titleVar.equals("data/cleanInterval_SchoolYard")){
					cleanInterval_SchoolYard = unit.valueVar;
				}
				if(unit.titleVar.equals("data/cleanInterval_ClassRoom")){
					cleanInterval_ClassRoom = unit.valueVar;
				}
				if(unit.titleVar.equals("data/StuHearTeacher")){
					StuHearTeacher = unit.valueVar;
				}
				if(unit.titleVar.equals("data/stuSeatArrange")){
					stuSeatArrange = unit.valueVar;
				}
				
				if(unit.titleVar.equals("data/teacherStage")){
					teacherStage = unit.valueVar;
				}
				if(unit.titleVar.equals("data/complained_SchoolEnvironment")){
					complained_SchoolEnvironment = unit.valueVar;
				}
				if(unit.titleVar.equals("data/how_informed_Environment_prob")){
					how_informed_Environment_prob = unit.valueVar;
				}
				if(unit.titleVar.equals("data/how_informed_Environment_prob_other")){
					how_informed_Environment_prob_other = unit.valueVar;
				}
				if(unit.titleVar.equals("data/took_step_Environment_prob")){
					took_step_Environment_prob = unit.valueVar;
				}
				
				if(unit.titleVar.equals("data/rank_Environment")){
					rank_Environment = unit.valueVar;
				}
				if(unit.titleVar.equals("data/rank_Edu_Quality")){
					rank_Edu_Quality = unit.valueVar;
				}
				if(unit.titleVar.equals("data/scareSafe_SchoolWay")){
					scareSafe_SchoolWay = unit.valueVar;
				}
				if(unit.titleVar.equals("data/yhy_not_feel_safe")){
					yhy_not_feel_safe = unit.valueVar;
				}
				if(unit.titleVar.equals("data/schoolCanteen")){
					schoolCanteen = unit.valueVar;
				}
			}
			SchoolEnvironment schoolEnvironment = new SchoolEnvironment();
			
			schoolEnvironment.date = new Date();
			schoolEnvironment.data = data;
			schoolEnvironment.form = data.form;
			schoolEnvironment.sender = data.sender;
			schoolEnvironment.school = data.schools;
			
			schoolEnvironment.month = month;
			schoolEnvironment.res_type = res_type;
			schoolEnvironment.cleanInterval_SchoolYard = cleanInterval_SchoolYard;
			schoolEnvironment.cleanInterval_ClassRoom = cleanInterval_ClassRoom;
			schoolEnvironment.StuHearTeacher = StuHearTeacher;
			
			schoolEnvironment.stuSeatArrange = stuSeatArrange;
			schoolEnvironment.teacherStage = teacherStage;
			schoolEnvironment.complained_SchoolEnvironment = complained_SchoolEnvironment;
			schoolEnvironment.how_informed_Environment_prob = how_informed_Environment_prob;
			
			schoolEnvironment.how_informed_Environment_prob_other = how_informed_Environment_prob_other;
			schoolEnvironment.took_step_Environment_prob = took_step_Environment_prob;
			schoolEnvironment.rank_Environment = rank_Environment;
			schoolEnvironment.rank_Edu_Quality = rank_Edu_Quality;
			
			schoolEnvironment.scareSafe_SchoolWay = scareSafe_SchoolWay;
			schoolEnvironment.yhy_not_feel_safe = yhy_not_feel_safe;
			schoolEnvironment.schoolCanteen = schoolCanteen;
			
			schoolEnvironment.save();
		 
	 }
	 
	public static void insetIntoSchoolSportsRecreation(Data data, User user){
		 List<UnitData> listUnitData = UnitData.find(
					"SELECT u from UnitData u where u.data =? order by id", data)
					.fetch();
		    String month = "";
		 	String res_type = "";
			String facilitiesAvailable = "";
			String instrumentUsable = "";
			String instrumentEqualAccess = "";
			String schoolType = "";
			
			String whyNot_EqualAccess = "";
			String sportsTeacher = "";
			String lastMonth_Activity = "";
			String rank_SportsRecreation = "";
			
			for(UnitData unit : listUnitData){
				if(unit.titleVar.equals("data/month")){
					month = unit.valueVar;
				}
				if(unit.titleVar.equals("data/res_type")){
					res_type = unit.valueVar;
				}
				if(unit.titleVar.equals("data/facilitiesAvailable")){
					facilitiesAvailable = unit.valueVar;
				}
				if(unit.titleVar.equals("data/instrumentUsable")){
					instrumentUsable = unit.valueVar;
				}
				
				if(unit.titleVar.equals("data/schoolType")){
					schoolType = unit.valueVar;
				}
				if(unit.titleVar.equals("data/instrumentEqualAccess")){
					instrumentEqualAccess = unit.valueVar;
				}
				if(unit.titleVar.equals("data/whyNot_EqualAccess")){
					whyNot_EqualAccess = unit.valueVar;
				}
				
				if(unit.titleVar.equals("data/sportsTeacher")){
					sportsTeacher = unit.valueVar;
				}
				if(unit.titleVar.equals("data/lastMonth_Activity")){
					lastMonth_Activity = unit.valueVar;
				}
				if(unit.titleVar.equals("data/rank_SportsRecreation")){
					rank_SportsRecreation = unit.valueVar;
				}
				
			}
			
			SportsRecreation sportsRecreation = new SportsRecreation();
			sportsRecreation.date = new Date();
			sportsRecreation.data = data;
			sportsRecreation.form = data.form;
			sportsRecreation.sender = data.sender;
			sportsRecreation.school = data.schools;
			
			sportsRecreation.month = month;
			sportsRecreation.res_type = res_type;
			sportsRecreation.facilitiesAvailable = facilitiesAvailable;
			sportsRecreation.instrumentUsable = instrumentUsable;
			sportsRecreation.instrumentEqualAccess = instrumentEqualAccess;
			sportsRecreation.schoolType = schoolType;
			
			sportsRecreation.whyNot_EqualAccess = whyNot_EqualAccess;
			sportsRecreation.sportsTeacher = sportsTeacher;
			sportsRecreation.lastMonth_Activity = lastMonth_Activity;
			sportsRecreation.rank_SportsRecreation = rank_SportsRecreation;
			
			sportsRecreation.save();
			
	 }
	  
}
