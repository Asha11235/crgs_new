package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import models.GeoDistrict;
import models.GeoDivision;
import models.GeoUnion;
import models.GeoUpazilla;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.With;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;

@With(Deadbolt.class)
public class Areas extends Controller {
	
	/**GeoDivision*/
	
	@ExternalRestrictions("Edit Division")
    public static void createGeoDivision() {
		render();
	}

	@ExternalRestrictions("Edit Division")
    public static void editGeoDivision(Long id) {
		GeoDivision geoDivision = GeoDivision.findById(id);
		notFoundIfNull(geoDivision);
		render("@createGeoDivision", geoDivision);
	}
	
	@ExternalRestrictions("View Division")
    public static void listGeoDivision() {
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		render(geoDivisionList);
	}
	
	@ExternalRestrictions("Edit Division")
    public static void submitGeoDivision(@Valid GeoDivision geoDivision) {
		if (validation.hasErrors()) {
			render("@createGeoDivision", geoDivision);
		}
		geoDivision.save();
		flash.success("Record saved successfully.");
		listGeoDivision();
	}
	
	@ExternalRestrictions("Edit Division")
    public static void deleteGeoDivision(Long id) {
		GeoDivision geoDivision = GeoDivision.findById(id);
		notFoundIfNull(geoDivision);
		geoDivision.delete();
		ok();
	}

	/**GeoDistrict*/
	
	@ExternalRestrictions("Edit District")
    public static void createGeoDistrict() {
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		render(geoDivisionList);
	}

	@ExternalRestrictions("Edit District")
    public static void editGeoDistrict(Long id) {
		GeoDistrict geoDistrict = GeoDistrict.findById(id);
		notFoundIfNull(geoDistrict);
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		render("@createGeoDistrict", geoDistrict, geoDivisionList);
	}
	
	@ExternalRestrictions("View District")
    public static void listGeoDistrict() {
		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		render(geoDistrictList);
	}
	
	@ExternalRestrictions("Edit District")
    public static void submitGeoDistrict(@Valid GeoDistrict geoDistrict) {
		if (validation.hasErrors()) {
			List<GeoDivision> geoDivisionList = GeoDivision.findAll();
			render("@createGeoDistrict", geoDistrict, geoDivisionList);
		}
		geoDistrict.save();
		flash.success("Record saved successfully.");
		listGeoDistrict();
	}
	
	@ExternalRestrictions("Edit District")
    public static void deleteGeoDistrict(Long id) {
		GeoDistrict geoDistrict = GeoDistrict.findById(id);
		notFoundIfNull(geoDistrict);
		geoDistrict.delete();
		ok();
	}
	
	@ExternalRestrictions("Edit District")
    public static void loadGeoDistrict(Long id) {
		Logger.info("**Geo Dis " + id );
		
		GeoDivision geoDivision = GeoDivision.findById(id);
		notFoundIfNull(geoDivision);
		List<GeoDistrict> geoDistrictList = GeoDistrict.find("geoDivision = ? ", geoDivision).fetch();
		
		render(geoDistrictList);
	}
	/**GeoUpazilla*/
	
	@ExternalRestrictions("Edit Upazilla")
    public static void createGeoUpazilla() {
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		render(geoDivisionList/*,geoDistrictList*/);
	}

	@ExternalRestrictions("Edit Upazilla")
    public static void editGeoUpazilla(Long id) {
		GeoUpazilla geoUpazilla = GeoUpazilla.findById(id);
		notFoundIfNull(geoUpazilla);
		
		//to set geo-location view
		GeoDivision geoDivision = geoUpazilla.geoDivision;
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		GeoDistrict geoDistrict = geoUpazilla.geoDistrict;
		List<GeoDistrict> geoDistrictList = new ArrayList<GeoDistrict>();
		geoDistrictList.add(geoDistrict);
		
		render("@createGeoUpazilla", geoUpazilla, geoDivision, geoDivisionList, geoDistrict, geoDistrictList);
	}
	
	@ExternalRestrictions("View Upazilla")
    public static void listGeoUpazilla() {
		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
		render(geoUpazillaList);
	}
	
	@ExternalRestrictions("Edit Upazilla")
    public static void submitGeoUpazilla(GeoDivision geoDivision, GeoDistrict geoDistrict, GeoUpazilla geoUpazilla) {
		//set parent geoArea
		geoUpazilla.geoDivision = geoDivision;
		geoUpazilla.geoDistrict = geoDistrict;
		
		validation.valid(geoUpazilla);
		if (validation.hasErrors()) {
			List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
			render("@createGeoUpazilla", geoUpazilla, geoDistrictList);
		}
		
		geoUpazilla.save();
		flash.success("Record saved successfully.");
		listGeoUpazilla();
	}
	
	@ExternalRestrictions("Edit Upazilla")
    public static void deleteGeoUpazilla(Long id) {
		GeoUpazilla geoUpazilla = GeoUpazilla.findById(id);
		notFoundIfNull(geoUpazilla);
		geoUpazilla.delete();
		ok();
	}

	@ExternalRestrictions("Edit Upazilla")
    public static void loadGeoUpazilla(Long id) {
		Logger.info("**++Geo Upozilla " + id);
		
		GeoDistrict geoDistrict = GeoDistrict.findById(id);
		notFoundIfNull(geoDistrict);
		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.find("geoDistrict = ? ", geoDistrict).fetch();
		
		render(geoUpazillaList);
	}
	
	/**GeoUnion*/
	
	@ExternalRestrictions("Edit Union")
    public static void createGeoUnion() {
		
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		List<GeoDistrict> geoDistrictList = new ArrayList<GeoDistrict>();//GeoDistrict.findAll();
		List<GeoUpazilla> geoUpazillaList = new ArrayList<GeoUpazilla>();//GeoUpazilla.findAll();
		
		render(geoDivisionList, geoDistrictList, geoUpazillaList);
	}

	@ExternalRestrictions("Edit Union")
    public static void editGeoUnion(Long id) {
		
		GeoUnion geoUnion = GeoUnion.findById(id);
		notFoundIfNull(geoUnion);

		//to set geo-location view
		GeoDivision geoDivision = geoUnion.geoUpazilla.geoDivision;
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		GeoDistrict geoDistrict = geoUnion.geoUpazilla.geoDistrict;
		List<GeoDistrict> geoDistrictList = new ArrayList<GeoDistrict>();
		geoDistrictList.add(geoDistrict);
		GeoUpazilla geoUpazilla = geoUnion.geoUpazilla;
		List<GeoUpazilla> geoUpazillaList = new ArrayList<GeoUpazilla>();
		geoUpazillaList.add(geoUpazilla);
		
		render("@createGeoUnion", geoUnion, geoDivision, geoDivisionList, geoDistrict, geoDistrictList, geoUpazilla, geoUpazillaList);
	}
	
	@ExternalRestrictions("View Union")
    public static void listGeoUnion() {
		List<GeoUnion> geoUnionList = GeoUnion.findAll();
		render(geoUnionList);
	}
	
	@ExternalRestrictions("Edit Union")
    public static void submitGeoUnion(GeoDivision geoDivision, GeoDistrict geoDistrict, GeoUpazilla geoUpazilla, GeoUnion geoUnion) {
		geoUnion.geoDivision = geoDivision;
		geoUnion.geoDistrict = geoDistrict;
		geoUnion.geoUpazilla = geoUpazilla;
		
		validation.valid(geoUnion);
		if (validation.hasErrors()) {
			List<GeoDivision> geoDivisionList = GeoDivision.findAll();
			List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
			List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
			render("@createGeoUnion", geoUnion,geoDivisionList,geoDistrictList, geoUpazillaList);
		}
		
		try {
			geoUnion.save();
		}
		catch (PersistenceException pe) {
			List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
			String tmp = geoUnion.code;
			geoUnion.code = null;
			validation.required(geoUnion.code).key("geoUnion.geoUnionId").message("Union ID Upazilla wise unique");
			geoUnion.code = tmp;
			render("@createGeoUnion", geoUnion, geoUpazillaList);
		}		
		flash.success("Record saved successfully.");
		listGeoUnion();
	}
	
	@ExternalRestrictions("Edit Union")
    public static void deleteGeoUnion(Long id) {
		GeoUnion geoUnion = GeoUnion.findById(id);
		notFoundIfNull(geoUnion);
		geoUnion.delete();
		ok();
	}
	
	@ExternalRestrictions("Edit Union")
    public static void loadGeoUnion(Long id) {
		Logger.info(" *** Geo Union " + id);
		
		GeoUpazilla geoUpazilla = GeoUpazilla.findById(id);
		notFoundIfNull(geoUpazilla);
		List<GeoUnion> geoUnionList = GeoUnion.find("geoUpazilla = ?", geoUpazilla).fetch();
		
		render(geoUnionList);
	}	

}
