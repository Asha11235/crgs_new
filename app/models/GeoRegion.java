package models;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import interfaces.Assignable;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Length;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class GeoRegion extends Model  {
	
	@Required
	public String name;
	public String nameBn;
	
	@Required
	@Length(min=1, max=2)
	public String code;
	public String codeBn;
	
	
	public static GeoRegion findByRegionCode(String regionCode) {
		return GeoRegion.find("byCode", regionCode).first();
	}
	

    /**
     * This method return list of valid GeoRegion for given 'Role'
     * */
	public static List<GeoRegion> getAssignableGeoRegionList(Role role, Set<GeoRegion> parentGeoRegions){
		List<GeoRegion> assignableGeoRegions = new ArrayList<GeoRegion>();

		if(parentGeoRegions == null  /*&&  (role == Role.getMCRole() || role == Role.getRMRole() || role == Role.getDORole())*/){
			assignableGeoRegions = GeoRegion.findAll();
		}
		else{
			assignableGeoRegions.addAll(parentGeoRegions);
		}		
		
		Set<GeoRegion> assingedGeoRegions = new TreeSet<GeoRegion>();
		assingedGeoRegions.addAll(listGeoRegionAssignedTo(role));
		if (!assingedGeoRegions.isEmpty()) {
			assignableGeoRegions.removeAll(assingedGeoRegions);
		}

		return assignableGeoRegions;
	}
	

	/**
	 * list of GeoRegion assigned to users with given role
	 * */
	public static List<GeoRegion> listGeoRegionAssignedTo(Role role) {
		return User.find("SELECT geoRegions FROM User as u LEFT JOIN u.geoRegionList as geoRegions WHERE u.role = ? AND geoRegions IS NOT NULL", role).fetch();
	}

	/**
	 * list of GeoRegion assigned to user
	 * */
	public static List<GeoRegion> listGeoRegionAssignedTo(User user) {
		return User.find("SELECT geoRegions FROM User as u LEFT JOIN u.geoRegionList as geoRegions WHERE u = ? AND geoRegions IS NOT NULL", user).fetch();
	}
	
	
}
