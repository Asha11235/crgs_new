package models;

import interfaces.Assignable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class GeoDistrict extends Model  {
	
	@Required
	@ManyToOne
	public GeoDivision geoDivision;
	
	@Required
	public String name;
	public String nameBn;

	@Required
	//@Length(max=2, min=1,message="length Should be two digits")
//	@Match("[0-9][0-9]")
	public String code;
	public String codeBn;
	
	public static GeoDistrict findByDistrictCode(String distCode, GeoDivision geoDivision) {
		return GeoDistrict.find("byCodeAndgeoDivision", distCode, geoDivision).first();
	}
	
    /**
     * This method return list of valid GeoDistrict for given 'Role'
     * */
	public static List<GeoDistrict> getAssignableGeoDistrictList(Role role, Set<GeoDistrict> parentGeoDistrics){
		List<GeoDistrict> assignableGeoDistricts = new ArrayList<GeoDistrict>();

		if(parentGeoDistrics == null /* &&  role == Role.getDMRole()*/){
			assignableGeoDistricts = GeoDistrict.findAll();
		}
		else{
			assignableGeoDistricts.addAll(parentGeoDistrics);
		}		
		
		Set<GeoDistrict> assingedGeoDistricts = new TreeSet<GeoDistrict>();
		assingedGeoDistricts.addAll(listGeoDistrictAssignedTo(role));
		if (!assingedGeoDistricts.isEmpty()) {
			assignableGeoDistricts.removeAll(assingedGeoDistricts);
		}

		return assignableGeoDistricts;
	}
	
	/**
	 * list of GeoDistrict child of GeoRegions
	 * */
	public static List<GeoDistrict> listGeoDistrictChildOf(List<GeoRegion> geoRegions) {
		if (geoRegions == null || geoRegions.isEmpty()) {
			return new ArrayList<GeoDistrict>();
		}
		return GeoDistrict.find("geoRegion in (:geoRegions)").setParameter("geoRegions", geoRegions).fetch();	
	}
	
	/**
	 * list of GeoDistrict assigned to users with given role
	 * */
	public static List<GeoDistrict> listGeoDistrictAssignedTo(Role role) {
		return User.find("SELECT geoDistricts FROM User as u LEFT JOIN u.geoDistrictList as geoDistricts WHERE u.role = ? AND geoDistricts IS NOT NULL", role).fetch();
	}

	/**
	 * list of GeoDistrict assigned to user
	 * */
	public static List<GeoDistrict> listGeoDistrictAssignedTo(User user) {
		return User.find("SELECT geoDistricts FROM User as u LEFT JOIN u.geoDistrictList as geoDistricts WHERE u = ? AND geoDistricts IS NOT NULL", user).fetch();
	}
	
	
	
}
