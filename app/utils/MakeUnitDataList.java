package utils;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
import models.FormDefination;
import models.Sanitation;
import models.SchoolEnvironment;
import models.SportsRecreation;
import models.UnitData;
import models.Water;

public class MakeUnitDataList {
	public static List<UnitData> unitDataForWaterForm(Water water,List<FormDefination> fromDef){
		List<UnitData> waterList = new ArrayList<UnitData>();
		Boolean num_waterSource = true;
		Boolean why_not_potable = true;
		Boolean how_informed_water_prob = true;
		
		for(FormDefination fdef : fromDef){
			UnitData uniD = new UnitData();
			if(!water.month.equalsIgnoreCase("") && fdef.titleVar.equalsIgnoreCase("month")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.value = water.month;
			}
			if(!water.res_type.equalsIgnoreCase("") && water.res_type.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("res_type")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!water.water_source.equalsIgnoreCase("") && num_waterSource && fdef.titleVar.equalsIgnoreCase("water_source")){
				//String [] valuArray = water.water_source.split(",");
				String s = water.water_source.replace(" ","");
				String valus = "";
				String [] sArray = s.split(",");
				String inQs = "";
				for(int k = 0; k<sArray.length;k++){
					if(k != sArray.length - 1){
						inQs =inQs + "'"+sArray[k]+"',";
					}
					else{
						inQs =inQs + "'"+sArray[k]+"'";
					}
				}
				List<FormDefination> valueList = FormDefination.find("titleVar = ? and valueVar in ("+inQs+")","water_source").fetch();
				for(FormDefination f : valueList){
					valus = valus + f.value+",";
				}
				
				uniD.value = valus;
				num_waterSource = false;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
				
			}
			
			if(!water.num_waterSource.equalsIgnoreCase("")  && fdef.titleVar.equalsIgnoreCase("num_waterSource")){
				uniD.value = water.num_waterSource;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!water.activeWaterSource.equalsIgnoreCase("") && fdef.titleVar.equalsIgnoreCase("activeWaterSource")){
				uniD.value = water.activeWaterSource;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
				
			}
			if(!water.is_potable.equalsIgnoreCase("") && water.is_potable.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("is_potable")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			
			if(!water.why_not_potable.equalsIgnoreCase("") && why_not_potable && fdef.titleVar.equalsIgnoreCase("why_not_potable")){
				String [] valuArray = water.why_not_potable.split(",");
				String valus = "";
				for(int i = 0; i < valuArray.length; i++){
					for(FormDefination fd : fromDef){
						if(fd.titleVar.equalsIgnoreCase("why_not_potable") && valuArray[i].equalsIgnoreCase(fd.valueVar)){
							valus = valus + fd.value + ",";
						}
					}
				}
				
				uniD.value = valus;
				why_not_potable = false;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!water.why_not_potable_other.equalsIgnoreCase("")  && fdef.titleVar.equalsIgnoreCase("why_not_potable_other")){
				uniD.value = water.why_not_potable_other;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!water.is_tank_cleaned.equalsIgnoreCase("") && water.is_tank_cleaned.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("is_tank_cleaned")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			
			if(!water.is_informed_authority_water_prob.equalsIgnoreCase("") && water.is_informed_authority_water_prob.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("is_informed_authority_water_prob")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!water.how_informed_water_prob.equalsIgnoreCase("") && how_informed_water_prob && fdef.titleVar.equalsIgnoreCase("how_informed_water_prob")){
				String s = water.how_informed_water_prob.replace(" ","");
				String valus = "";
				String [] sArray = s.split(",");
				String inQs = "";
				for(int k = 0; k<sArray.length;k++){
					if(k != sArray.length - 1){
						inQs =inQs + "'"+sArray[k]+"',";
					}
					else{
						inQs =inQs + "'"+sArray[k]+"'";
					}
				}
				List<FormDefination> valueList = FormDefination.find("titleVar = ? and valueVar in ("+inQs+")","water_source").fetch();
				for(FormDefination f : valueList){
					valus = valus + f.value+",";
				}
				uniD.value = valus;
				how_informed_water_prob = false;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			
			if(!water.how_informed_water_prob_other.equalsIgnoreCase("") && fdef.titleVar.equalsIgnoreCase("how_informed_water_prob_other")){
				uniD.value = water.how_informed_water_prob_other;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			
			if(!water.water_prob_solved_authority.equalsIgnoreCase("") && water.water_prob_solved_authority.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("water_prob_solved_authority")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			
			if(!water.rank_water.equalsIgnoreCase("") && water.rank_water.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("rank_water")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
				waterList.add(uniD);
		}
		
		return waterList;
	}
	
	
	public static List<UnitData> unitDataForSanitationForm(Sanitation sanitation,List<FormDefination> fromDef){
		List<UnitData> sanitationList = new ArrayList<UnitData>();
		Boolean why_toilet_unusable = true;
		for(FormDefination fdef : fromDef){
			UnitData uniD = new UnitData();
			if(!sanitation.month.equalsIgnoreCase("") && fdef.titleVar.equalsIgnoreCase("month")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.value = sanitation.month;
			}
			if(!sanitation.res_type.equalsIgnoreCase("") && sanitation.res_type.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("res_type")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sanitation.num_toilet_unusable.equalsIgnoreCase("")  && fdef.titleVar.equalsIgnoreCase("num_toilet_unusable")){
				uniD.value = sanitation.num_toilet_unusable;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			
			if(!sanitation.why_toilet_unusable.equalsIgnoreCase("") && why_toilet_unusable && fdef.titleVar.equalsIgnoreCase("why_toilet_unusable")){
				String s = sanitation.why_toilet_unusable.replace(" ","");
				String valus = "";
				String [] sArray = s.split(",");
				String inQs = "";
				for(int k = 0; k<sArray.length;k++){
					if(k != sArray.length - 1){
						inQs =inQs + "'"+sArray[k]+"',";
					}
					else{
						inQs =inQs + "'"+sArray[k]+"'";
					}
				}
				List<FormDefination> valueList = FormDefination.find("titleVar = ? and valueVar in ("+inQs+")","water_source").fetch();
				for(FormDefination f : valueList){
					valus = valus + f.value+",";
				}
				uniD.value = valus;
				why_toilet_unusable = false;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			
			if(!sanitation.toilet_clean_interval.equalsIgnoreCase("") && sanitation.toilet_clean_interval.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("toilet_clean_interval")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sanitation.harpic.equalsIgnoreCase("") && sanitation.harpic.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("harpic")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sanitation.is_sanitation_prob_informed.equalsIgnoreCase("") && sanitation.is_sanitation_prob_informed.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("is_sanitation_prob_informed")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sanitation.how_informed_sanitation_prob.equalsIgnoreCase("") && sanitation.how_informed_sanitation_prob.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("how_informed_sanitation_prob")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sanitation.how_informed_sanitation_prob_other.equalsIgnoreCase("")  && fdef.titleVar.equalsIgnoreCase("how_informed_sanitation_prob_other")){
				uniD.value = sanitation.how_informed_sanitation_prob_other;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sanitation.took_step_sanitation_prob.equalsIgnoreCase("") && sanitation.took_step_sanitation_prob.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("took_step_sanitation_prob")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sanitation.rank_sanitation.equalsIgnoreCase("") && sanitation.rank_sanitation.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("rank_sanitation")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			sanitationList.add(uniD);
		}
		
		return sanitationList;
	}
	
	public static List<UnitData> unitDataForEnvironmentForm(SchoolEnvironment schoolEnvironment,List<FormDefination> fromDef){
		List<UnitData> environmentList = new ArrayList<UnitData>();
		Boolean yhy_not_feel_safe =true;
		for(FormDefination fdef : fromDef){
			UnitData uniD = new UnitData();
			if(!schoolEnvironment.month.equalsIgnoreCase("") && fdef.titleVar.equalsIgnoreCase("month")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.value = schoolEnvironment.month;
			}
			if(!schoolEnvironment.res_type.equalsIgnoreCase("") && schoolEnvironment.res_type.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("res_type")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			
			if(!schoolEnvironment.cleanInterval_SchoolYard.equalsIgnoreCase("") && schoolEnvironment.cleanInterval_SchoolYard.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("cleanInterval_SchoolYard")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.cleanInterval_ClassRoom.equalsIgnoreCase("") && schoolEnvironment.cleanInterval_ClassRoom.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("cleanInterval_ClassRoom")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.StuHearTeacher.equalsIgnoreCase("") && schoolEnvironment.StuHearTeacher.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("StuHearTeacher")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.stuSeatArrange.equalsIgnoreCase("") && schoolEnvironment.stuSeatArrange.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("stuSeatArrange")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.teacherStage.equalsIgnoreCase("") && schoolEnvironment.teacherStage.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("teacherStage")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.complained_SchoolEnvironment.equalsIgnoreCase("") && schoolEnvironment.complained_SchoolEnvironment.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("complained_SchoolEnvironment")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			
			if(!schoolEnvironment.how_informed_Environment_prob.equalsIgnoreCase("") && schoolEnvironment.how_informed_Environment_prob.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("how_informed_Environment_prob")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.how_informed_Environment_prob_other.equalsIgnoreCase("")  && fdef.titleVar.equalsIgnoreCase("how_informed_Environment_prob_other")){
				uniD.value = schoolEnvironment.how_informed_Environment_prob_other;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.took_step_Environment_prob.equalsIgnoreCase("") && schoolEnvironment.took_step_Environment_prob.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("took_step_Environment_prob")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.rank_Environment.equalsIgnoreCase("") && schoolEnvironment.rank_Environment.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("rank_Environment")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.rank_Edu_Quality.equalsIgnoreCase("") && schoolEnvironment.rank_Edu_Quality.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("rank_Edu_Quality")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.scareSafe_SchoolWay.equalsIgnoreCase("") && schoolEnvironment.scareSafe_SchoolWay.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("scareSafe_SchoolWay")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.yhy_not_feel_safe.equalsIgnoreCase("") && yhy_not_feel_safe && fdef.titleVar.equalsIgnoreCase("yhy_not_feel_safe")){
				String s = schoolEnvironment.yhy_not_feel_safe.replace(" ","");
				String valus = "";
				String [] sArray = s.split(",");
				String inQs = "";
				for(int k = 0; k<sArray.length;k++){
					if(k != sArray.length - 1){
						inQs =inQs + "'"+sArray[k]+"',";
					}
					else{
						inQs =inQs + "'"+sArray[k]+"'";
					}
				}
				List<FormDefination> valueList = FormDefination.find("titleVar = ? and valueVar in ("+inQs+")","water_source").fetch();
				for(FormDefination f : valueList){
					valus = valus + f.value+",";
				}
				uniD.value = valus;
				yhy_not_feel_safe = false;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!schoolEnvironment.schoolCanteen.equalsIgnoreCase("") && schoolEnvironment.schoolCanteen.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("schoolCanteen")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			environmentList.add(uniD);
		}
		
		return environmentList;
	}

	
	public String whyNot_EqualAccess = "";
	public String sportsTeacher = "";
	public String lastMonth_Activity = "";
	public String rank_SportsRecreation = "";
	
	public static List<UnitData> unitDataForSportsForm(SportsRecreation sporth,List<FormDefination> fromDef){
		List<UnitData> sportsList = new ArrayList<UnitData>();
		Boolean facilitiesAvailable = true;
		Boolean lastMonth_Activity = true;
		for(FormDefination fdef : fromDef){
			UnitData uniD = new UnitData();
			if(!sporth.month.equalsIgnoreCase("") && fdef.titleVar.equalsIgnoreCase("month")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.value = sporth.month;
			}
			if(!sporth.res_type.equalsIgnoreCase("") && sporth.res_type.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("res_type")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sporth.facilitiesAvailable.equalsIgnoreCase("") && facilitiesAvailable && fdef.titleVar.equalsIgnoreCase("yhy_not_feel_safe")){
				String s = sporth.facilitiesAvailable.replace(" ","");
				String valus = "";
				String [] sArray = s.split(",");
				String inQs = "";
				for(int k = 0; k<sArray.length;k++){
					if(k != sArray.length - 1){
						inQs =inQs + "'"+sArray[k]+"',";
					}
					else{
						inQs =inQs + "'"+sArray[k]+"'";
					}
				}
				List<FormDefination> valueList = FormDefination.find("titleVar = ? and valueVar in ("+inQs+")","water_source").fetch();
				for(FormDefination f : valueList){
					valus = valus + f.value+",";
				}
				uniD.value = valus;
				facilitiesAvailable = false;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sporth.instrumentUsable.equalsIgnoreCase("") && sporth.instrumentUsable.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("instrumentUsable")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sporth.instrumentEqualAccess.equalsIgnoreCase("") && sporth.instrumentEqualAccess.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("instrumentEqualAccess")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sporth.schoolType.equalsIgnoreCase("") && sporth.schoolType.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("schoolType")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sporth.sportsTeacher.equalsIgnoreCase("") && sporth.sportsTeacher.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("sportsTeacher")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sporth.lastMonth_Activity.equalsIgnoreCase("") && lastMonth_Activity && fdef.titleVar.equalsIgnoreCase("lastMonth_Activity")){
				String s = sporth.lastMonth_Activity.replace(" ","");
				String valus = "";
				String [] sArray = s.split(",");
				String inQs = "";
				for(int k = 0; k<sArray.length;k++){
					if(k != sArray.length - 1){
						inQs =inQs + "'"+sArray[k]+"',";
					}
					else{
						inQs =inQs + "'"+sArray[k]+"'";
					}
				}
				List<FormDefination> valueList = FormDefination.find("titleVar = ? and valueVar in ("+inQs+")","water_source").fetch();
				for(FormDefination f : valueList){
					valus = valus + f.value+",";
				}
				uniD.value = valus;
				lastMonth_Activity = false;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			if(!sporth.rank_SportsRecreation.equalsIgnoreCase("") && sporth.rank_SportsRecreation.equalsIgnoreCase(fdef.valueVar) && fdef.titleVar.equalsIgnoreCase("rank_SportsRecreation")){
				uniD.value = fdef.value;
				uniD.titleVar = fdef.titleVar;
				uniD.title = fdef.title;
				uniD.type = fdef.type;
				uniD.valueVar = fdef.valueVar;
			}
			sportsList.add(uniD);
		}
		return sportsList;
	}

}
