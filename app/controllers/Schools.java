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
    	
    	if(validation.hasErrors()) {
    		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
    		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
    		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
    		List<String> schoolType = new ArrayList<String>();
    		schoolType.add(0, "Girls school");
    		schoolType.add(1, "Boys School");
    		schoolType.add(2, "Combined School");
        	render("@editSchool", geoDivisionList, geoDistrictList, geoUpazillaList, schoolType);    
        }
    	
    	school.save();
    	//schoolList();
    	Application.forms();
        //flash.success("Record saved successfully.");
	}
	@ExternalRestrictions("Edit School")
	public static void editSchool(Long id){
		SchoolInformation school = SchoolInformation.findById(id);
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
		List<String> schoolType = new ArrayList<String>();
		schoolType.add(0, "Girls school");
		schoolType.add(1, "Boys School");
		schoolType.add(2, "Combined School");
		//render("@createSchool",school);
		render(geoDivisionList,geoDistrictList,geoUpazillaList,schoolType,school);
	}
	
	@ExternalRestrictions("Edit School")
	public static int deleteSchool(Long id){
		/*SchoolInformation school = SchoolInformation.findById(id);
		notFoundIfNull(school);
		school.delete();
		ok();*/
		
		int confirm = 1;
    	if(request.isAjax()) {
    		//Long id = Long.valueOf(request.params.get("userId"));
    		SchoolInformation school = SchoolInformation.findById(id);
        	
	    	notFoundIfNull(id, "id not provided");
	    	notFoundIfNull(school, "school not found");
	    	
	    	JPAQuery q = Data.find("sender = ?",school);
	    	List<Data> d = q.fetch();
	    	
	    	try {
	    		school.delete();
			} catch (Exception e) {
				confirm = 0;
			}
        	
        	
	    	
	    	//Set<GeoPSU> geoPSUAssign = user.geoPSUs;
	        /*Logger.info("geoPSUAssign:"+geoPSUAssign.size());
	    	
	    	if((d.size() <= 0) && (geoPSUAssign.size() <= 0)){
	    		user.delete();
		    	confirm = 1;
	    	}
	    	else{
		    	//forbidden(); 
	    		confirm = 0;
	    	}*/
        	
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
        
        String countRow = "select count(*) from User";
        
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
