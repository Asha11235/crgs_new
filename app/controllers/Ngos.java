package controllers;


import java.util.List;

import models.Data;
import models.GeoDistrict;
import models.GeoDivision;
import models.GeoUnion;
import models.GeoUpazilla;
import models.Ngo;
import models.Role;
import models.SchoolInformation;
import models.User;
import play.Logger;
import play.data.validation.Valid;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.With;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;


@With(Deadbolt.class)
public class Ngos extends Controller{
	@ExternalRestrictions("View NGO")
	public static void list(){
		List<Ngo> ngoList = Ngo.findAll();
		render(ngoList);
	}
	
	 @ExternalRestrictions("Edit NGO")
	 public static void create() {
	 	List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
		List<Ngo> ngoList = Ngo.findAll();
		
		if(flash.get("userId") != null){
			Logger.info("flash exist");
			Ngo ngo = Ngo.findById(Long.parseLong(flash.get("userId")));
			renderArgs.put("ngo", ngo);
		}
		
    	render("@edit", ngoList,geoDivisionList, geoDistrictList,geoUpazillaList);
	  }
	 
	 @ExternalRestrictions("Edit NGO")
	 public static int delete(Long id) {
		/*Ngo ngo = Ngo.findById(id);
		ngo.delete();
	 	List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
		List<Ngo> ngoList = Ngo.findAll();
    	render("@edit", ngoList,geoDivisionList, geoDistrictList,geoUpazillaList);*/
		 session.remove("userId");
		 Logger.info("ngoId is : " + id);
		 int confirm = 0;
	    	if(request.isAjax()) {
	    		Ngo ngo = Ngo.findById(id);	        	
		    	notFoundIfNull(id, "id not provided");
		    	notFoundIfNull(ngo, "user not found");
		    	try {
		    		ngo.delete();
		    		confirm = 1;
				} catch (Exception e) {
					// TODO: handle exception
				}
		    	
	        	
	    	}
	    	
	    	return confirm;
	  }
	 
	 	@ExternalRestrictions("Edit NGO")
	 	public static void edit(Long id) {
	    	Ngo ngo = Ngo.findById(id);
	    	render(ngo);
	    }
	 
	 	public static void submit(@Valid Ngo ngo) {
	    	validation.valid(ngo);
	    	if(validation.hasErrors()) {
	        	render("@edit", ngo);
	        }
	    	ngo.save();
	        flash.success("Record saved successfully.");
	        create();
	    }
	 	
	 	public static void editSubmit(@Valid Ngo ngo) {
	 		Long id = Long.parseLong(session.get("userId"));
	 		Ngo pre_ngo = Ngo.findById(id);
	    	pre_ngo.ngoName = ngo.ngoName;
	    	pre_ngo.geoDivision = ngo.geoDivision;
	    	pre_ngo.geoDistrict = ngo.geoDistrict;
	    	pre_ngo.geoUpazilla = ngo.geoUpazilla;
	    	pre_ngo.save();
	    	session.remove("userId");
	    	create();
	    }
	 
		 public static void details(Long id){
			 flash("userId",id);
			 session.put("userId", id);
			 flash("details", "details");
			 create();
		 }
		 
		 public static void initEdit(Long id){
			 flash.remove("details");
			 flash("userId",id);
			 session.put("userId", id);
			 create();
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
	    
	    public static void loadGeoSchool(Long id) {
	    	
			GeoUpazilla geoUpazilla = GeoUpazilla.findById(id);
			notFoundIfNull(geoUpazilla);
			List<SchoolInformation> schoolList = SchoolInformation.find("geoUpazilla = ?", geoUpazilla).fetch();
			
			render(schoolList);
		}
	    
	    public static void loadRole(Long id) {
			Role role = Role.findById(id);
			notFoundIfNull(role);
			List<Role> roleList = role.find("role = ? ", role).fetch();
			render(roleList);
		}
	
}
