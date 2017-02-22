package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.SchoolInformation;
import models.TeacherRespond;
import models.User;
import play.Logger;
import play.mvc.With;
import utils.TeacherRespondDataCount;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
@With(Deadbolt.class)
public class ReportonTeacherResponse extends Controller{
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray waterTankStatus(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.waterTankStatus(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray unusableToiletStatus(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.unusableToiletStatus(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray actionStatusRegardingWashroom(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.actionStatusRegardingWashroom(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray disableChildrenWashroomStatus(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.disableChildrenWashroomStatus(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray presenceofMugStatus(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.presenceofMugStatus(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray presenceofHandWashStatus(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.presenceofHandWashStatus(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray menstrualHygieneStatus(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.menstrualHygieneStatus(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray uncleanedWashroomStatus(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.uncleanedWashroomStatus(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray awarenessStatusRegardingHygiene(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.awarenessStatusRegardingHygiene(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
	@ExternalRestrictions("Management Response") 
	public static JsonArray studentParticipationStatus(){
		int headSelector = 6;
		boolean ajaxFlag = false;
		String ajaxSchoolId = request.params.get("school_id");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate");
		String ajaxEndDate = request.params.get("endDate");
		if(ajaxSchoolId != null){
			ajaxFlag = true;
		}
		
		Logger.info(ajaxSchoolId + ajaxGender + ajaxStartDate + ajaxEndDate);
		
		ArrayList<SchoolInformation> info = getSchoolsInfo();
		JsonArray json_dataList =new JsonArray();
		
		json_dataList = TeacherRespondDataCount.studentParticipationStatus(ajaxSchoolId,ajaxGender,ajaxStartDate,ajaxEndDate,info);
		if(ajaxFlag){
			return json_dataList;
		}
		
		render(headSelector,info,json_dataList);
		return json_dataList;
	}
	
/*	public static ArrayList<schoolInfo> getSchoolsInfo() {//SchoolInformation
		ArrayList<schoolInfo> info = new ArrayList<schoolInfo>();
		User webUser = User.findByName(session.get("username"));
		Set<SchoolInformation> schools = webUser.schoolInformation;
		List<SchoolInformation> listSchool = new ArrayList<SchoolInformation>(schools);

		if((webUser.role.id == 1) || (webUser.role.id == 4)){
			listSchool = SchoolInformation.findAll();
		}
		ArrayList<Long>school = new ArrayList<Long>();
		for (SchoolInformation schoolInformation : listSchool) {
			Long school_id = schoolInformation.id;
				school.add(school_id);
				Reports reports = new Reports();
				info.add(reports.new schoolInfo(school_id,schoolInformation.name));
			}
		return info;
	}*/
	
	public static ArrayList<SchoolInformation> getSchoolsInfo() {//SchoolInformation
		User webUser = User.findByName(session.get("username"));
		SchoolInformation schools = webUser.school;
		List<SchoolInformation> listSchool = new ArrayList<SchoolInformation>();

		if((webUser.role.id == 1) || (webUser.role.id == 4)){
			listSchool = SchoolInformation.findAll();
		}
		/*ArrayList<Long>school = new ArrayList<Long>();
		for (SchoolInformation schoolInformation : listSchool) {
			Long school_id = schoolInformation.id;
				school.add(school_id);
				Reports reports = new Reports();
				info.add(reports.new schoolInfo(school_id,schoolInformation.name));
			}*/
		return (ArrayList<SchoolInformation>) listSchool;
	}

}
