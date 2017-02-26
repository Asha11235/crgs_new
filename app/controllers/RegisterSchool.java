package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import play.Logger;
import play.data.validation.Error;
import play.data.validation.Validation;
import models.CaseReport;
import models.GeoDistrict;
import models.GeoDivision;
import models.GeoUnion;
import models.GeoUpazilla;
import models.MediaArchive;
import models.SchoolInformation;
import models.User;

public class RegisterSchool extends play.mvc.Controller{
	
	public static void schoolregister(){
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
		List<String> schoolType = new ArrayList<String>();
		
		schoolType.add(0, "Girls school");
		schoolType.add(1, "Boys School");
		schoolType.add(2, "Combined School");
		SchoolInformation school = null;
		render(geoDivisionList,geoDistrictList,geoUpazillaList,schoolType);
	}
	
	public static void submit(@Valid SchoolInformation school){
		
		validation.required("School name is required",school.name);
		validation.required("Head sir phone number is required", school.schoolHeadSirMobileNumber);
		
		
		
		if(validation.hasErrors()){
			flash.error("Record can't be saved");
			for (Error error : validation.errors()) {
				flash.error("" + error.getKey());
				break;
			}
			schoolregister();
		}
		if(school.schoolHeadSirMobileNumber.length() < 11){
			flash.error("Mobile number must be 11 digit");
			schoolregister();
		}
		
		validation.valid(school);
		school.registrationDate = new Date();
		school.approavedStatus = "Pending";
		
		
		school.save();
		flash.success("Record saved successfully");
		try {
			Secure.login();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	 public static void loadGeoDistrict(Long id) {
			GeoDivision geoDivision = GeoDivision.findById(id);
			notFoundIfNull(geoDivision);
			List<GeoDistrict> geoDistrictList = GeoDistrict.find("geoDivision = ? ", geoDivision).fetch();
			render(geoDistrictList);
		}
	   
	    public static void loadGeoUpazilla(Long id) {
			GeoDistrict geoDistrict = GeoDistrict.findById(id);
			notFoundIfNull(geoDistrict);
			List<GeoUpazilla> geoUpazillaList = GeoUpazilla.find("geoDistrict = ? ", geoDistrict).fetch();
			render(geoUpazillaList);
		}
	    
	    public static void loadGeoUnion(Long id) {
			GeoUpazilla geoUpazilla = GeoUpazilla.findById(id);
			notFoundIfNull(geoUpazilla);
			List<GeoUnion> geoUnionList = GeoUnion.find("geoUpazilla = ?", geoUpazilla).fetch();
			
			render(geoUnionList);
		}
	    
	    public static String  checkUserName(String username){
	    	Logger.info("Testttt " + username);
	    	User checkUser = User.findByName(username);
	    	String msg=username;
	    	if(checkUser == null){
	    		return "";
	    	}
	    	else{
	    		return "This user is already existed in the system";
	    	}
	    }
	    
	    public static void submitCase(){
	    	List<GeoDivision> geoDivisionList = GeoDivision.findAll();
			List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
			List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
			List<String> gender = new ArrayList<String>();
			List<String> status = new ArrayList<String>();
			
			List<SchoolInformation> schoolList = SchoolInformation.findAll();
			
			
			status.add(0, "Active");
			status.add(1, "Inactive");
			status.add(2, "Other");
			
			gender.add(0, "Male");
			gender.add(1, "Female");
			gender.add(2, "Other");
			CaseReport school = null;
	    	render(geoDivisionList,geoDistrictList,geoUpazillaList,gender,schoolList,school,status);
	    }
	    
	    public static void saveCaseReport(CaseReport school){
	    	validation.valid(school);
			school.save();
			try {
				Secure.login();
			} catch (Throwable e) {
				e.printStackTrace();
			}
	    }
	    
	    public static void resourceList(){
	    	List<MediaArchive> mediaArchive = MediaArchive.findAll();
	    	
	    	render(mediaArchive);
	    }

}
