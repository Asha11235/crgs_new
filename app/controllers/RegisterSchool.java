package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.validation.Valid;

import controllers.deadbolt.ExternalRestrictions;
import play.Logger;
import play.data.validation.Error;
import play.data.validation.Validation;
import play.db.jpa.JPA;
import models.CaseReport;
import models.GeoDistrict;
import models.GeoDivision;
import models.GeoUnion;
import models.GeoUpazilla;
import models.MediaArchive;
import models.Ngo;
import models.Role;
import models.SchoolInformation;
import models.User;

public class RegisterSchool extends play.mvc.Controller{
	
	
	public static void schoolregister(){
		
		
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
	//	List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		//List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
	//	List<GeoUnion> geoUnionList = GeoUnion.findAll();
		List<String> schoolType = new ArrayList<String>();
		
		schoolType.add(0, "Girls school");
		schoolType.add(1, "Boys School");
		schoolType.add(2, "Combined School");
		//SchoolInformation school = null;
		//render(geoDivisionList,geoDistrictList,geoUpazillaList,schoolType,geoUnionList);
		render(geoDivisionList,schoolType);
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
		
		Logger.info("annualsports: " + school.annualSports);
		school.save();
		flash.success("Record saved successfully");
		//render();
		try {
			if(session.get("username") != null){
				
				Forms.landingPage();
			}
			else{
				
				Secure.login();
			}
			//
			//Application.index();
			
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
	    
	   
	    
	    public static void resourceList(){
	    	List<MediaArchive> mediaArchive = MediaArchive.findAll();
	    	
	    	render(mediaArchive);
	    }
	    
	    
	    public static void submitCase(){
	    	List<GeoDivision> geoDivisionList = GeoDivision.findAll();
			List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
			List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
			List<GeoUnion> geoUnionList = GeoUnion.findAll();
			List<String> gender = new ArrayList<String>();
			List<String> caseType = new ArrayList<String>();
			List<SchoolInformation> schoolList = SchoolInformation.findAll();
			
			List<String> currentStatus = new ArrayList<String>();
			currentStatus.add(0, "Active");
			currentStatus.add(1, "Inactive");
			currentStatus.add(2, "Other");
			
			gender.add(0, "Male");
			gender.add(1, "Female");
			gender.add(2, "Other");
			
			
			caseType.add( 0,"Rape");
			caseType.add( 1,"Mental Torture");
			caseType.add( 2,"Physical Assault");
			caseType.add( 3,"Sexual Assault");
			caseType.add( 4,"Forced Prostitution");
			caseType.add( 5,"Influenced Suicide");
			caseType.add( 6,"Abduction");
			caseType.add( 7,"Forced/Underaged Marriage");
			caseType.add( 8,"Threat");
			caseType.add( 9,"Acid Violence");
			caseType.add( 10,"Domestic Violence");
			caseType.add( 11,"Physical/Corporal & Mental Punishment");
			caseType.add( 12,"Other");
			CaseReport caseReport = null;
	    	render(geoDivisionList,geoDistrictList,geoUpazillaList,gender,schoolList,caseReport,currentStatus,caseType,geoUnionList);
	    }
	    
	    public static void saveCaseReport(@Valid CaseReport caseReport){
	    	validation.valid(caseReport);
	    	
	    	
			if(validation.hasErrors()){
				flash.error("Record can't be saved");
				for (Error error : validation.errors()) {
					flash.error("" + error.getKey());
					break;
				}
				submitCase();
			}
	    	caseReport.save();
			try {
				if(session.get("username") != null){
					
					Forms.landingPage();
					
				}
				else{
					
					Secure.login();
					
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
	    }
	    
	
		
	    
	 /*   @ExternalRestrictions("View Case")
	    public static void caseReport(String page) {
	        int currentPage = 1;
	        int recordsPerPage = 20;
	        int totalPages = 0;
	        
	        if (request.params.get("page") != null) {
	            currentPage = Integer.parseInt(request.params.get("page"));     
	        }
	        
	        if(request.params.get("page").equals(0)){
	            currentPage = 1;
	        }

	        Query query = null;
	        
	        String countRow = "select count(*) from CaseReport";
	        List<GeoDivision> geoDivisionList = GeoDivision.findAll();
	        List<GeoUnion>geoUnionList = GeoUnion.findAll();
	        Logger.info("union: " + geoUnionList.size());
	        query = JPA.em().createQuery(countRow);
	        int rowNumber = ( (Number)query.getSingleResult()).intValue();
	        
	        totalPages = (int) Math.ceil(rowNumber * 1.0 / recordsPerPage);

			List<CaseReport> caseReports = CaseReport.find("id <> 1").fetch();
			
			renderArgs.put("geoUnionList", geoUnionList);
	        render(caseReports,rowNumber,currentPage,totalPages,geoUnionList);
			
	    }
	    
	    public static void caseReport() {
	    	
			List<GeoDivision> geoDivisionList = GeoDivision.findAll();	
			List<GeoUnion>geoUnionList = GeoUnion.findAll();
			List<String> caseType = new ArrayList<String>();
			
			caseType.add(0,"Case Type");
			caseType.add( 1,"Rape");
			caseType.add( 2,"Mental Torture");
			caseType.add( 3,"Physical Assault");
			caseType.add( 4,"Sexual Assault");
			caseType.add( 5,"Forced Prostitution");
			caseType.add( 6,"Influenced Suicide");
			caseType.add( 7,"Abduction");
			caseType.add( 8,"Forced/Underaged Marriage");
			caseType.add( 9,"Threat");
			caseType.add( 10,"Acid Violence");
			caseType.add( 11,"Domestic Violence");
			caseType.add( 12,"Physical/Corporal & Mental Punishment");
			caseType.add( 13,"Other");
			
			renderArgs.put("geoDivisionList", geoDivisionList);
			renderArgs.put("geoUnionList", geoUnionList);
           // renderArgs.put("caseType", caseType);
            
			List<CaseReport> caseList = CaseReport.findAll();
			renderArgs.put("caseList", caseList);
			render();
		}*/
	   
	    @ExternalRestrictions("View Case")
	    public static void details(Long id){
			CaseReport caseReport = CaseReport.findById(id);
			notFoundIfNull(caseReport, "caseReport not found");
			
			render(caseReport);
	    }
	    
	    @ExternalRestrictions("Edit Case")
	    public static void editCase(Long id) {
	 		
	     	CaseReport caseReport = CaseReport.findById(id);
	     	flash("caseReport", "" + caseReport.id);
	     	notFoundIfNull(caseReport, "caseReport not found");
	     	
	     	List<String> gender = new ArrayList<String>();
	     	gender.add(0, "Male");
			gender.add(1, "Female");
			gender.add(2, "Other");
			
			List<String> currentStatus = new ArrayList<String>();
			currentStatus.add(0, "Active");
			currentStatus.add(1, "Inactive");
			currentStatus.add(2, "Other");
			
            List<String> caseType = new ArrayList<String>();
			
			//caseType.add(0,"Case Type");
			caseType.add( 0,"Rape");
			caseType.add( 1,"Mental Torture");
			caseType.add( 2,"Physical Assault");
			caseType.add( 3,"Sexual Assault");
			caseType.add( 4,"Forced Prostitution");
			caseType.add( 5,"Influenced Suicide");
			caseType.add( 6,"Abduction");
			caseType.add( 7,"Forced/Underaged Marriage");
			caseType.add( 8,"Threat");
			caseType.add( 9,"Acid Violence");
			caseType.add( 10,"Domestic Violence");
			caseType.add( 11,"Physical/Corporal & Mental Punishment");
			caseType.add( 12,"Other");
			
	     	
	     	List<SchoolInformation> schoolList = SchoolInformation.find("approavedStatus = ? ", "Approved").fetch();
	     	List<Ngo> ngoList = Ngo.findAll();
	     	
	     	List<GeoDivision> geoDivisionList = GeoDivision.findAll();
	 		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
	 		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
	 		List<GeoUnion> geoUnionList = GeoUnion.findAll();
	     	render(caseReport,schoolList,ngoList, gender,geoDivisionList,geoDistrictList,geoUpazillaList , geoUnionList,currentStatus,caseType);
	     }
	    
	    
	    @ExternalRestrictions("Edit Case")
	    public static void submitEditCase(@Valid models.CaseReport caseReport) {
	     	
	     	
	     	Logger.info("flashUserId in Submit: " + flash.get("user") + " and user id is : " + caseReport.id);
	     	validation.valid(caseReport);
	     	if(validation.hasErrors() && flash.get("caseReport") == null) {
	     		
	     		Logger.info("hasError");
	     		
	         	render("@edit", caseReport);
	         }
	     	  
	     	if(flash.get("caseReport") != null){
	     		 
	     		CaseReport precaseReports = CaseReport.findById(Long.parseLong(flash.get("caseReport")));
	     		
	     		precaseReports.informerName = caseReport.informerName;
	     		precaseReports.mobNumber = caseReport.mobNumber;
	     		precaseReports.victimName = caseReport.victimName;
	     		precaseReports.victimAge = caseReport.victimAge;
	     		precaseReports.victimSex= caseReport.victimSex;
	     		precaseReports.description = caseReport.description;
	     		precaseReports.geoDivision = caseReport.geoDivision;
	     		precaseReports.geoDistrict = caseReport.geoDistrict;
	     		precaseReports.geoUpazilla = caseReport.geoUpazilla;
	     		precaseReports.geoUnion = caseReport.geoUnion ;
	     		precaseReports.associateSchool = caseReport.associateSchool;
	     		precaseReports.currentStatus = caseReport.currentStatus ; 
	     		precaseReports.caseType = caseReport.caseType ;
	     		
	     		precaseReports.save();
	     	    Reports.caseReport();
	     		
	     	}
	     	else {
	     		caseReport.save();
	     		Reports.caseReport();
	 		}
	         
	         flash.success("Record saved successfully.");
	         //caseReport("0");
	     }
	     

		@ExternalRestrictions("Edit Case")
		public static int deleteCase(Long id){
			/*SchoolInformation school = SchoolInformation.findById(id);
			notFoundIfNull(school);
			school.delete();
			ok();*/
			Logger.info("delete method");
			int confirm = 1;
	    	if(request.isAjax()) {
	    		
	    		CaseReport caseReport = CaseReport.findById(id);
	        	
		    	notFoundIfNull(id, "id not provided");
		    	notFoundIfNull(caseReport, "case not found");
		    	/*
		    	JPAQuery q = Data.find("sender = ?",school);
		    	List<Data> d = q.fetch();
		    	*/
		    	try {
		    		caseReport.delete();
		    		Reports.caseReport();
		    		
				} catch (Exception e) {
					confirm = 0;
				}
	        	
		    	Reports.caseReport();
	    	}
	    	
	    	return confirm;
		}

}
