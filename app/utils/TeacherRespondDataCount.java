package utils;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import models.SchoolInformation;
import models.TeacherRespond;
import play.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



public class TeacherRespondDataCount {
	
	/* Water Tank Status (গত এক মাসে খাবার পানির ট্যাংকি পরিষ্কার করা হয়েছিল কি? )
	 * Resolved By using this Method
	 */
	public static JsonArray waterTankStatus(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int lackOfHR = 0;
		int LimitedBudget = 0;
		int noAssignedPerson = 0;
		int negligentAttitude = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] isTankCleaned = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				isTankCleaned = tr.isTankCleaned.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < isTankCleaned.length; i++){
					if(isTankCleaned[i].equalsIgnoreCase("Shortage of manpower")){
						lackOfHR = lackOfHR + 1;
					}
					if(isTankCleaned[i].equalsIgnoreCase("Shortage of budget")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(isTankCleaned[i].equalsIgnoreCase("Not assigned any person for it")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(isTankCleaned[i].equalsIgnoreCase("Negligence by the responsible person")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(isTankCleaned[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				lackOfHR = 0;
				LimitedBudget = 0;
				noAssignedPerson = 0;
				negligentAttitude = 0;
				other = 0;
				for(int i = 0; i < isTankCleaned.length; i++){
					if(isTankCleaned[i].equalsIgnoreCase("Shortage of manpower")){
						lackOfHR = lackOfHR + 1;
					}
					if(isTankCleaned[i].equalsIgnoreCase("Shortage of budget")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(isTankCleaned[i].equalsIgnoreCase("Not assigned any person for it")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(isTankCleaned[i].equalsIgnoreCase("Negligence by the responsible person")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(isTankCleaned[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("NegligentAttitude",df.format(na));
			inactiveToiletReasonJson.addProperty("NoAssignedPerson", df.format(np));
			inactiveToiletReasonJson.addProperty("LimitedBudget",df.format(lb));
			inactiveToiletReasonJson.addProperty("LackOfHR", df.format(lh));
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	/* Unusable Toilet Status (কি কারনে অনুপযোগী ) 
	 * Resolved By using this Method
	 */
	public static JsonArray unusableToiletStatus(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int notInformAboutTheProblem = 0;
		int sortageOfBudget = 0;
		int shortageOfManpower = 0;
		int negligentResponsiblePerson = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] whyToiletUnusable = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				whyToiletUnusable = tr.whyToiletUnusable.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < whyToiletUnusable.length; i++){
					if(whyToiletUnusable[i].equalsIgnoreCase("Not informed about the problem")){
						notInformAboutTheProblem = notInformAboutTheProblem + 1;
					}
					if(whyToiletUnusable[i].equalsIgnoreCase("Shortage of budget for maintenance purpose")){
						sortageOfBudget = sortageOfBudget + 1;
					}
					if(whyToiletUnusable[i].equalsIgnoreCase("Shortage of manpower")){
						shortageOfManpower = shortageOfManpower + 1;
					}
					if(whyToiletUnusable[i].equalsIgnoreCase("Negligence by the responsible person")){
						negligentResponsiblePerson = negligentResponsiblePerson + 1;
					}
					if(whyToiletUnusable[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(notInformAboutTheProblem) + "," + String.valueOf(sortageOfBudget) + "," + String.valueOf(shortageOfManpower) + "," +String.valueOf(negligentResponsiblePerson) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				notInformAboutTheProblem = 0;
				sortageOfBudget = 0;
				shortageOfManpower = 0;
				negligentResponsiblePerson = 0;
				other = 0;
				for(int i = 0; i < whyToiletUnusable.length; i++){
					if(whyToiletUnusable[i].equalsIgnoreCase("Not informed about the problem")){
						notInformAboutTheProblem = notInformAboutTheProblem + 1;
					}
					if(whyToiletUnusable[i].equalsIgnoreCase("Shortage of budget for maintenance purpose")){
						sortageOfBudget = sortageOfBudget + 1;
					}
					if(whyToiletUnusable[i].equalsIgnoreCase("Shortage of manpower")){
						shortageOfManpower = shortageOfManpower + 1;
					}
					if(whyToiletUnusable[i].equalsIgnoreCase("Negligence by the responsible person")){
						negligentResponsiblePerson = negligentResponsiblePerson + 1;
					}
					if(whyToiletUnusable[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(notInformAboutTheProblem) + "," + String.valueOf(sortageOfBudget) + "," + String.valueOf(shortageOfManpower) + "," +String.valueOf(negligentResponsiblePerson) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("NegligenceByTheResponsiblePerson",df.format(na));
			inactiveToiletReasonJson.addProperty("ShortageOfManpower", df.format(np));
			inactiveToiletReasonJson.addProperty("ShortageOfBudgetForMaintenancePurpose",df.format(lb));
			inactiveToiletReasonJson.addProperty("NotInformedAboutTheProblem", df.format(lh));
			
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	/*
	 *  টয়লেটে নিয়মিত পয়:নিষ্কাশনের উদ্যোগ গ্রহণ করা হয় কিনা? data/is_take_regular_initiative_clean_toilet
	 * Resolved By using this Method
	 */
	public static JsonArray actionStatusRegardingWashroom(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int lackOfHR = 0;
		int LimitedBudget = 0;
		int noAssignedPerson = 0;
		int negligentAttitude = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] isTakeRegularInitiativeCleanToilet = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				isTakeRegularInitiativeCleanToilet = tr.isTakeRegularInitiativeCleanToilet.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < isTakeRegularInitiativeCleanToilet.length; i++){
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("Negligence by the respective cleaner")){
						lackOfHR = lackOfHR + 1;
					}
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("Necessary materials not had been bought")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("No budget allocation")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("Negligence by the responsible teacher")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				lackOfHR = 0;
				LimitedBudget = 0;
				noAssignedPerson = 0;
				negligentAttitude = 0;
				other = 0;
				for(int i = 0; i < isTakeRegularInitiativeCleanToilet.length; i++){
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("Negligence by the respective cleaner")){
						lackOfHR = lackOfHR + 1;
					}
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("Necessary materials not had been bought")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("No budget allocation")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("Negligence by the responsible teacher")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(isTakeRegularInitiativeCleanToilet[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("NegligenceByTheResponsibleTeacher",df.format(na));
			inactiveToiletReasonJson.addProperty("NoBudgetAllocation", df.format(np));
			inactiveToiletReasonJson.addProperty("NecessaryMaterialsNotHadBeenBought",df.format(lb));
			inactiveToiletReasonJson.addProperty("NegligenceByTheRespectiveCleaner", df.format(lh));
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	/*
	 *  স্কুলের টয়লেট প্রতিবন্ধি শিশুদের ব্যবহার উপযোগী কি না? data/is_toilet_usable_differentlyAble
	 * Resolved By using this Method
	 */
	public static JsonArray disableChildrenWashroomStatus(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int lackOfHR = 0;
		int LimitedBudget = 0;
		int noAssignedPerson = 0;
		int negligentAttitude = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] isToiletUsableDifferentlyAble = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				isToiletUsableDifferentlyAble = tr.isToiletUsableDifferentlyAble.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < isToiletUsableDifferentlyAble.length; i++){
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("Constructed of the toilet wasn’t disable friendly")){
						lackOfHR = lackOfHR + 1;
					}
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("Construction of disable friendly toilet is under process")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("No available budget allocation")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("Not clear about the issue")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				lackOfHR = 0;
				LimitedBudget = 0;
				noAssignedPerson = 0;
				negligentAttitude = 0;
				other = 0;
				for(int i = 0; i < isToiletUsableDifferentlyAble.length; i++){
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("Constructed of the toilet wasn’t disable friendly")){
						lackOfHR = lackOfHR + 1;
					}
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("Construction of disable friendly toilet is under process")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("No available budget allocation")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("Not clear about the issue")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(isToiletUsableDifferentlyAble[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("NotClearAboutTheIssue",df.format(na));
			inactiveToiletReasonJson.addProperty("NoAvailableBudgetAllocation", df.format(np));
			inactiveToiletReasonJson.addProperty("ConstructionOfDisableFriendlyToiletIsUnderProcess",df.format(lb));
			inactiveToiletReasonJson.addProperty("ConstructedOfTheToiletWasnDisableFriendly", df.format(lh));
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	
	/*
	 * সব টয়লেটে বদনা/মগ থাকা data/mug
	 * Resolved By using this Method
	 */
	public static JsonArray presenceofMugStatus(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int lackOfHR = 0;
		int LimitedBudget = 0;
		int noAssignedPerson = 0;
		int negligentAttitude = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] mug = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				mug = tr.mug.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < mug.length; i++){
					if(mug[i].equalsIgnoreCase("Student demolish these utensils")){
						lackOfHR = lackOfHR + 1;
					}
					if(mug[i].equalsIgnoreCase("Didn’t bought due to unavailability of budget")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(mug[i].equalsIgnoreCase("SMC have been informed but no decision made yet")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(mug[i].equalsIgnoreCase("Negligence by the responsible person")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(mug[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				lackOfHR = 0;
				LimitedBudget = 0;
				noAssignedPerson = 0;
				negligentAttitude = 0;
				other = 0;
				for(int i = 0; i < mug.length; i++){
					if(mug[i].equalsIgnoreCase("Student demolish these utensils")){
						lackOfHR = lackOfHR + 1;
					}
					if(mug[i].equalsIgnoreCase("Didn’t bought due to unavailability of budget")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(mug[i].equalsIgnoreCase("SMC have been informed but no decision made yet")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(mug[i].equalsIgnoreCase("Negligence by the responsible person")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(mug[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("NegligenceByTheResponsiblePerson",df.format(na));
			inactiveToiletReasonJson.addProperty("SMChaveBeenInformedButNoDecisionMadeYet", df.format(np));
			inactiveToiletReasonJson.addProperty("DidnBoughtDueToUnavailabilityOfBudget",df.format(lb));
			inactiveToiletReasonJson.addProperty("StudentDemolishTheseUtensils", df.format(lh));
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	/*
	 * সব টয়লেটে সাবান বা হ্যান্ড ওয়াশ থাকা data/soap_handwash
	 * Resolved By using this Method
	 */
	public static JsonArray presenceofHandWashStatus(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int lackOfHR = 0;
		int LimitedBudget = 0;
		int noAssignedPerson = 0;
		int negligentAttitude = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] soapHandwash = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				soapHandwash = tr.soapHandwash.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < soapHandwash.length; i++){
					if(soapHandwash[i].equalsIgnoreCase("Finished earlier")){
						lackOfHR = lackOfHR + 1;
					}
					if(soapHandwash[i].equalsIgnoreCase("Student demolish these utensils")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(soapHandwash[i].equalsIgnoreCase("Not had been bought due to unavailability of budget")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(soapHandwash[i].equalsIgnoreCase("Negligence by the responsible person")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(soapHandwash[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				lackOfHR = 0;
				LimitedBudget = 0;
				noAssignedPerson = 0;
				negligentAttitude = 0;
				other = 0;
				for(int i = 0; i < soapHandwash.length; i++){
					if(soapHandwash[i].equalsIgnoreCase("Finished earlier")){
						lackOfHR = lackOfHR + 1;
					}
					if(soapHandwash[i].equalsIgnoreCase("Student demolish these utensils")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(soapHandwash[i].equalsIgnoreCase("Not had been bought due to unavailability of budget")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(soapHandwash[i].equalsIgnoreCase("Negligence by the responsible person")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(soapHandwash[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("NegligenceByTheResponsiblePerson",df.format(na));
			inactiveToiletReasonJson.addProperty("NotHadBeenBoughtDueToUnavailabilityOfBudget", df.format(np));
			inactiveToiletReasonJson.addProperty("StudentDemolishTheseUtensils",df.format(lb));
			inactiveToiletReasonJson.addProperty("FinishedEarlier", df.format(lh));
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	/*
	 * ঋতু কালীন স্বাস্থ্য পরিচর্যার পর্যাপ্ত ব্যবস্থা আছে কি না?data/menstrul_facility
	 * Resolved By using this Method
	 */
	public static JsonArray menstrualHygieneStatus(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int lackOfHR = 0;
		int LimitedBudget = 0;
		int noAssignedPerson = 0;
		int negligentAttitude = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] menstrulFacility = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				menstrulFacility = tr.menstrulFacility.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < menstrulFacility.length; i++){
					if(menstrulFacility[i].equalsIgnoreCase("Necessary materials are not available")){
						lackOfHR = lackOfHR + 1;
					}
					if(menstrulFacility[i].equalsIgnoreCase("Absence of responsible teacher")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(menstrulFacility[i].equalsIgnoreCase("SMC have been informed but they are not interested")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(menstrulFacility[i].equalsIgnoreCase("Not clear about the issue")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(menstrulFacility[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				lackOfHR = 0;
				LimitedBudget = 0;
				noAssignedPerson = 0;
				negligentAttitude = 0;
				other = 0;
				for(int i = 0; i < menstrulFacility.length; i++){
					if(menstrulFacility[i].equalsIgnoreCase("Necessary materials are not available")){
						lackOfHR = lackOfHR + 1;
					}
					if(menstrulFacility[i].equalsIgnoreCase("Absence of responsible teacher")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(menstrulFacility[i].equalsIgnoreCase("SMC have been informed but they are not interested")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(menstrulFacility[i].equalsIgnoreCase("Not clear about the issue")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(menstrulFacility[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("NotClearAboutTheIssue",df.format(na));
			inactiveToiletReasonJson.addProperty("SMChaveBeenInformedButTheyAreNotInterested", df.format(np));
			inactiveToiletReasonJson.addProperty("AbsenceOfResponsibleTeacher",df.format(lb));
			inactiveToiletReasonJson.addProperty("NecessaryMaterialsAreNotAvailable", df.format(lh));
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	/*
	 * টয়লেট প্রতিদিন বা নিয়মিত পরিষ্কার করা data/freq_clean_toilet
	 * Resolved By using this Method
	 */
	public static JsonArray uncleanedWashroomStatus(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int lackOfHR = 0;
		int LimitedBudget = 0;
		int noAssignedPerson = 0;
		int negligentAttitude = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] freqCleanToilet = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				freqCleanToilet = tr.freqCleanToilet.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < freqCleanToilet.length; i++){
					if(freqCleanToilet[i].equalsIgnoreCase("No WASH worker available")){
						lackOfHR = lackOfHR + 1;
					}
					if(freqCleanToilet[i].equalsIgnoreCase("No water supply")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(freqCleanToilet[i].equalsIgnoreCase("Not assigned any person for it")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(freqCleanToilet[i].equalsIgnoreCase("Shortage of necessary materials")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(freqCleanToilet[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				lackOfHR = 0;
				LimitedBudget = 0;
				noAssignedPerson = 0;
				negligentAttitude = 0;
				other = 0;
				for(int i = 0; i < freqCleanToilet.length; i++){
					if(freqCleanToilet[i].equalsIgnoreCase("No WASH worker available")){
						lackOfHR = lackOfHR + 1;
					}
					if(freqCleanToilet[i].equalsIgnoreCase("No water supply")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(freqCleanToilet[i].equalsIgnoreCase("Not assigned any person for it")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(freqCleanToilet[i].equalsIgnoreCase("Shortage of necessary materials")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(freqCleanToilet[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("ShortageOfNecessaryMaterials",df.format(na));
			inactiveToiletReasonJson.addProperty("NotAssignedAnnyPersonForIt", df.format(np));
			inactiveToiletReasonJson.addProperty("NoWaterSupply",df.format(lb));
			inactiveToiletReasonJson.addProperty("NoWASHWorkerAvailable", df.format(lh));
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	
	/*
	 * গত এক মাসে পরিষ্কার পরিচ্ছন্নতা সংক্রান্ত সচেতনতা বৃদ্ধিমুলক কোন আলোচনা ক্লাশে হয়েছিল কি না? ata/disscusion_clean_classroom
	 * Resolved By using this Method
	 */
	public static JsonArray awarenessStatusRegardingHygiene(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int lackOfHR = 0;
		int LimitedBudget = 0;
		int noAssignedPerson = 0;
		int negligentAttitude = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] disscusionCleanClassroom = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				disscusionCleanClassroom = tr.disscusionCleanClassroom.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < disscusionCleanClassroom.length; i++){
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Absence of responsible teacher")){
						lackOfHR = lackOfHR + 1;
					}
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Busy due to running examination")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Negligence of responsible parson")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Not clear concept about the discussion points")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				lackOfHR = 0;
				LimitedBudget = 0;
				noAssignedPerson = 0;
				negligentAttitude = 0;
				other = 0;
				for(int i = 0; i < disscusionCleanClassroom.length; i++){
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Absence of responsible teacher")){
						lackOfHR = lackOfHR + 1;
					}
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Busy due to running examination")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Negligence of responsible parson")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Not clear concept about the discussion points")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(disscusionCleanClassroom[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("NotClearConceptAboutTheDiscussionPoints",df.format(na));
			inactiveToiletReasonJson.addProperty("NegligenceOfResponsibleParson", df.format(np));
			inactiveToiletReasonJson.addProperty("BusyDueToRunningExamination",df.format(lb));
			inactiveToiletReasonJson.addProperty("AbsenceOfResponsibleTeacher", df.format(lh));
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	
	/*
	 * গত এক মাসে অনুষ্ঠিত মিটিং/সভায় শিক্ষার্থী প্রতিনিধি অংশগ্রহন করেছে কি না?data/is_stu_present_meeting
	 * Resolved By using this Method
	 */
	public static JsonArray studentParticipationStatus(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		JsonArray json_dataList =new JsonArray();
		JsonObject inactiveToiletReasonJson = new JsonObject();
		int lackOfHR = 0;
		int LimitedBudget = 0;
		int noAssignedPerson = 0;
		int negligentAttitude = 0;
		int other = 0;
		List <HashMap<String,String>> monthWiseResponse = new ArrayList<HashMap<String,String>>();
		List<TeacherRespond> listTeacherRespond = teacherRespondDataList(schoolid,gender,startDate,endDate,info);
		Logger.info("Data List " + listTeacherRespond.size());
		
		String monthCheker = "";
		String month = "";
		int datacount = 1;
		for(TeacherRespond tr : listTeacherRespond){
			String [] isStuPresentMeeting = {}; 
			month = tr.date.toString().substring(5, 7);
			try{
				isStuPresentMeeting = tr.isStuPresentMeeting.split(",");
			}catch(Exception e){}
			if(monthCheker.equalsIgnoreCase("")){
				monthCheker = month;
			}
			else if(!monthCheker.equalsIgnoreCase("") && monthCheker.equalsIgnoreCase(month)){
				for(int i = 0; i < isStuPresentMeeting.length; i++){
					if(isStuPresentMeeting[i].equalsIgnoreCase("Student are not eligible to participate")){
						lackOfHR = lackOfHR + 1;
					}
					if(isStuPresentMeeting[i].equalsIgnoreCase("Student opinion have taken before the meeting")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(isStuPresentMeeting[i].equalsIgnoreCase("Student’s participation is not needed in these meeting")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(isStuPresentMeeting[i].equalsIgnoreCase("The meeting held at vacation/after close of school")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(isStuPresentMeeting[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
			
			else if(!monthCheker.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase(month)){
				HashMap< String, String> ansMap = new HashMap<String, String>();
				String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);;
				ansMap.put(monthCheker, getVal);
				monthWiseResponse.add(ansMap);
				monthCheker = month;
				datacount = 0;
				lackOfHR = 0;
				LimitedBudget = 0;
				noAssignedPerson = 0;
				negligentAttitude = 0;
				other = 0;
				for(int i = 0; i < isStuPresentMeeting.length; i++){
					if(isStuPresentMeeting[i].equalsIgnoreCase("Student are not eligible to participate")){
						lackOfHR = lackOfHR + 1;
					}
					if(isStuPresentMeeting[i].equalsIgnoreCase("Student opinion have taken before the meeting")){
						LimitedBudget = LimitedBudget + 1;
					}
					if(isStuPresentMeeting[i].equalsIgnoreCase("Student’s participation is not needed in these meeting")){
						noAssignedPerson = noAssignedPerson + 1;
					}
					if(isStuPresentMeeting[i].equalsIgnoreCase("The meeting held at vacation/after close of school")){
						negligentAttitude = negligentAttitude + 1;
					}
					if(isStuPresentMeeting[i].equalsIgnoreCase("Others")){
						other = other + 1;
					}
				}
				datacount = datacount + 1;
			}
		}
		if(!month.equalsIgnoreCase("") && !monthCheker.equalsIgnoreCase("") && month.equalsIgnoreCase(monthCheker)){
			HashMap< String, String> ansMap = new HashMap<String, String>();
			String getVal = String.valueOf(lackOfHR) + "," + String.valueOf(LimitedBudget) + "," + String.valueOf(noAssignedPerson) + "," +String.valueOf(negligentAttitude) + "," +String.valueOf(other) + "," +String.valueOf(datacount);
			ansMap.put(monthCheker, getVal);
			monthWiseResponse.add(ansMap);
		}
		
		for(HashMap<String , String> hMap : monthWiseResponse ){
			Logger.info("Value "+ hMap.values());
			
			if(hMap.keySet().toArray()[0].equals("02")){
				inactiveToiletReasonJson.addProperty("State", "February");
			}
			if(hMap.keySet().toArray()[0].equals("01")){
				inactiveToiletReasonJson.addProperty("State", "January");
			}
			if(hMap.keySet().toArray()[0].equals("03")){
				inactiveToiletReasonJson.addProperty("State", "March");
			}
			if(hMap.keySet().toArray()[0].equals("04")){
				inactiveToiletReasonJson.addProperty("State", "April");
			}
			if(hMap.keySet().toArray()[0].equals("05")){
				inactiveToiletReasonJson.addProperty("State", "May");
			}
			if(hMap.keySet().toArray()[0].equals("06")){
				inactiveToiletReasonJson.addProperty("State", "June");
			}
			if(hMap.keySet().toArray()[0].equals("07")){
				inactiveToiletReasonJson.addProperty("State", "July");
			}
			double lh =(Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[0])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double lb = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[1])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double np = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[2])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double na = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[3])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			double o = (Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[4])/Double.parseDouble(hMap.get(hMap.keySet().toArray()[0]).split(",")[5]))*100;
			DecimalFormat df = new DecimalFormat("#.##");  
			inactiveToiletReasonJson.addProperty("Other",df.format(o));
			inactiveToiletReasonJson.addProperty("TheMeetingHeldAtVacation",df.format(na));
			inactiveToiletReasonJson.addProperty("StudentParticipationIsNotNeededInTheseMeeting", df.format(np));
			inactiveToiletReasonJson.addProperty("StudentOpinionHaveTakenBeforeTheMeeting",df.format(lb));
			inactiveToiletReasonJson.addProperty("StudentAreNotEligibleToParticipate", df.format(lh));
			json_dataList.add(inactiveToiletReasonJson);
			inactiveToiletReasonJson = new JsonObject();
		}
		
		//Logger.info("Json " +  json_dataList.toString());
		return json_dataList;
	}
	
	
	
	
	
	/*
	 * Search TeacherResponse according to schoolId,Gender and date range 
	 * for supplying the data into above upper 10 Method  
	 */
	public static List<TeacherRespond> teacherRespondDataList(String schoolid, String gender, String startDate, String endDate,ArrayList<SchoolInformation> info){
		List<TeacherRespond> listTeacherRespond = new ArrayList<TeacherRespond>();
		if(schoolid != null && !schoolid.equalsIgnoreCase("school_id") && gender != null 
			&& gender.equalsIgnoreCase("gender") && startDate == null && endDate == null){
			
			//Logger.info("Position -- 1");
			SchoolInformation scholl = SchoolInformation.findById(Long.parseLong(schoolid));
			listTeacherRespond = TeacherRespond.find("school = ?", scholl).fetch();
		}
		if(schoolid != null && schoolid.equalsIgnoreCase("school_id") && gender != null 
			&& !gender.equalsIgnoreCase("gender") && startDate == null && endDate == null){
			
			//Logger.info("Position -- 2");
			listTeacherRespond = TeacherRespond.find("senderGender = ?", gender).fetch();
		}
		if(schoolid != null && schoolid.equalsIgnoreCase("school_id") && gender != null 
				&& gender.equalsIgnoreCase("gender") && startDate != null && endDate != null){
			
			 //Logger.info("Position -- 3");
			 java.sql.Date sqlDateSt = java.sql.Date.valueOf(startDate);
			 java.sql.Date sqlDateEn = java.sql.Date.valueOf(endDate);
			 listTeacherRespond = TeacherRespond.find("date between :startDate and :endDate").setParameter("startDate", sqlDateSt).setParameter("endDate", sqlDateEn).fetch();
			 Logger.info(""+listTeacherRespond.size());
	        
		}
		if(schoolid != null && !schoolid.equalsIgnoreCase("school_id") 
				&& gender != null && gender.equalsIgnoreCase("gender") && startDate != null && endDate != null){
			//Logger.info("Position -- 4");
			SchoolInformation scholl = SchoolInformation.findById(Long.parseLong(schoolid));
			java.sql.Date sqlDateSt = java.sql.Date.valueOf(startDate);
			java.sql.Date sqlDateEn = java.sql.Date.valueOf(endDate);
			listTeacherRespond = TeacherRespond.find("school = ? and date between :startDate and :endDate", scholl).setParameter("startDate", sqlDateSt).setParameter("endDate", sqlDateEn).fetch();
		}
		if(schoolid != null && schoolid.equalsIgnoreCase("school_id") 
				&& gender != null && !gender.equalsIgnoreCase("gender") && startDate != null && endDate != null){
			//Logger.info("Position -- 5");
			java.sql.Date sqlDateSt = java.sql.Date.valueOf(startDate);
			java.sql.Date sqlDateEn = java.sql.Date.valueOf(endDate);
			listTeacherRespond = TeacherRespond.find("senderGender = ? and date between :startDate and :endDate", gender).setParameter("startDate", sqlDateSt).setParameter("endDate", sqlDateEn).fetch();
		}
		if(schoolid != null&& !schoolid.equalsIgnoreCase("school_id") && gender != null 
				&& !gender.equalsIgnoreCase("gender") &&  startDate != null && endDate != null){
			//Logger.info("Position -- 6");
			SchoolInformation scholl = SchoolInformation.findById(Long.parseLong(schoolid));
			java.sql.Date sqlDateSt = java.sql.Date.valueOf(startDate);
			java.sql.Date sqlDateEn = java.sql.Date.valueOf(endDate);
			listTeacherRespond = TeacherRespond.find("school = ? and senderGender= ? and date between :startDate and :endDate", scholl,gender).setParameter("startDate", sqlDateSt).setParameter("endDate", sqlDateEn).fetch();
		}
		if(schoolid != null && !schoolid.equalsIgnoreCase("school_id") && gender != null 
				&& !gender.equalsIgnoreCase("gender") && startDate == null && endDate == null){
			//Logger.info("Position -- 7");
			SchoolInformation scholl = SchoolInformation.findById(Long.parseLong(schoolid));
			listTeacherRespond = TeacherRespond.find("school = ? and senderGender= ?", scholl,gender).fetch();
		}
		
		if(schoolid == null && gender == null && startDate == null && endDate == null){
			//Logger.info("Position -- 8");
			//listTeacherRespond = TeacherRespond.findAll();
			listTeacherRespond = TeacherRespond.find("school in (:schoolList)").setParameter("schoolList", info).fetch();
		}
		
		return listTeacherRespond;
	}

}
