package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import jobs.CommentSender;
import models.Data;
import models.GeoDistrict;
import models.GeoDivision;
import models.GeoUnion;
import models.GeoUpazilla;
import models.Role;
import models.SchoolInformation;
import models.User;
//import models.User;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.With;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPA;
import javax.persistence.Query;

@With(Deadbolt.class)
public class Schools extends Controller{
	
	@ExternalRestrictions("View School")
	public static void createSchool(){
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
		List<String> schoolType = new ArrayList<String>();
		
		schoolType.add(0, "Girls school");
		schoolType.add(1, "Boys School");
		schoolType.add(2, "Combined School");
		SchoolInformation school = null;
		
		render("@editSchool",geoDivisionList,schoolType,geoDistrictList,geoUpazillaList,school);
	}
	
	public static void submit(@Valid SchoolInformation school){
		
		validation.valid(school);
    	Logger.info("validation  " + validation.hasErrors());
    	Logger.info("flash: " + flash.get("school"));
    	if(validation.hasErrors() && flash.get("school") == null) {
    		List<Role> roles = Role.findAll();
    		Logger.info("hasError");
    		
        	render("@edit", school, roles);
        }
    	if(flash.get("school") != null){
    		
    		SchoolInformation preschool = SchoolInformation.findById(Long.parseLong(flash.get("school")));
    		
    		preschool.activeSchoolStudent = school.activeSchoolStudent;
    		preschool.activeStudent = school.activeStudent;
    		preschool.classTeacherPhoneNumber = school.classTeacherPhoneNumber;
    		preschool.femailStudent = school.femailStudent ;
    		preschool.femailToilets = school.femailToilets ;
    		preschool.geoDistrict = school.geoDistrict ;
    		preschool.geoDivision = school.geoDivision ;
    		preschool.geoUnion = school.geoUnion ;
    		preschool.geoUpazilla = school.geoUpazilla ;
    		preschool.headSirPhonNumber = school.headSirPhonNumber ;
    		preschool.maleStruden = school.maleStruden ;
    		preschool.maleToilets = school.maleToilets ;
    		preschool.name = school.name ;
    		//preschool.registrationDate = school.registrationDate ;
    		preschool.schoolHeadSirMobileNumber = school.schoolHeadSirMobileNumber ;
    		//preschool.schoolRegNo = school.schoolRegNo ;
    		//preschool.approavedStatus = school.approavedStatus; 
    		preschool.schoolType = school.schoolType;
    		preschool.totalStudent = school.totalStudent ;
    		preschool.totalToilets =school.totalToilets;
    		preschool.save();
    		
    		
    	}
    	else{
    		school.save();
    	
    	}
    	
    	//Application.forms();
        flash.success("Record saved successfully.");
      
        schoolList("0");
	}
	@ExternalRestrictions("Edit School")
	public static void editSchool(Long id){
		SchoolInformation school = SchoolInformation.findById(id);
		flash("school", "" + school.id);
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
		List<GeoUnion> geoUnionList = GeoUnion.findAll();
		List<String> schoolType = new ArrayList<String>();
		schoolType.add(0, "Girls school");
		schoolType.add(1, "Boys School");
		schoolType.add(2, "Combined School");
		//render("@createSchool",school);
		render(geoDivisionList,geoDistrictList,geoUpazillaList,geoUnionList ,schoolType,school);
	}
	
	@ExternalRestrictions("Edit School")
	public static int deleteSchool(Long id){
		/*SchoolInformation school = SchoolInformation.findById(id);
		notFoundIfNull(school);
		school.delete();
		ok();*/
		Logger.info("delete method");
		int confirm = 1;
    	if(request.isAjax()) {
    		
    		SchoolInformation school = SchoolInformation.findById(id);
        	
	    	notFoundIfNull(id, "id not provided");
	    	notFoundIfNull(school, "school not found");
	    	/*
	    	JPAQuery q = Data.find("sender = ?",school);
	    	List<Data> d = q.fetch();
	    	*/
	    	try {
	    		school.delete();
	    		render("@schoolList");
	    		
			} catch (Exception e) {
				confirm = 0;
			}
        	
        	render("@schoolList");
    	}
    	
    	return confirm;
	}
	
	@ExternalRestrictions("View School")
	public static void schoolList(String page){
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
        
        String countRow = "select count(*) from SchoolInformation";
        
        query = JPA.em().createQuery(countRow);
        int rowNumber = ( (Number)query.getSingleResult()).intValue();
        
        totalPages = (int) Math.ceil(rowNumber * 1.0 / recordsPerPage);

		List<SchoolInformation> listSchool = SchoolInformation.find("order by id desc").fetch();
		render(listSchool,rowNumber,currentPage,totalPages);
	}
	
	public static void details(Long id){
		SchoolInformation school = SchoolInformation.findById(id);
		render(school);
		
	}
	
	public static void approved(Long id){
		SchoolInformation school = SchoolInformation.findById(id);
		
		school.approavedStatus = "Approved";
		school.approvedBy = User.findByName(session.get("username"));
		school.save();
		schoolList("1");
	}
	
	public static void reject(Long id){
		
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
	    
	   
	
}
