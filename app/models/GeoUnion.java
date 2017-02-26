package models;

import interfaces.Assignable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Constraint;
import org.hibernate.validator.constraints.Length;
import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "geoUpazilla_id", "code" }))
public class GeoUnion extends Model {

	@Required
	@ManyToOne
	public GeoDistrict geoDistrict;
	@Required
	@ManyToOne
	public GeoDivision geoDivision;
	@Required
	@ManyToOne
	public GeoUpazilla geoUpazilla;
	@Required
	public String name;
	public String nameBn;
	@Required
	@Unique
	//@Length(max=2, min=1)
//	@Match("[0-9][0-9]")
	public String code;
	public String codeBn;

	public static GeoUnion findByUnionCode(String unionCode, GeoUpazilla geoUpazilla) {
		return GeoUnion.find("byCodeAndgeoUpazilla", unionCode, geoUpazilla).first();
	}

    /**
     * This method return list of valid GeoUnion for given 'Role'
     * */
	public static List<GeoUnion> getAssignableGeoUnionList(Role role, Set<GeoUnion> parentGeoUnions){
		List<GeoUnion> assignableGeoUnions = new ArrayList<GeoUnion>();

		if (parentGeoUnions != null) {
			assignableGeoUnions.addAll(parentGeoUnions);
		}
		
		Set<GeoUnion> assingedGeoUnions = new TreeSet<GeoUnion>();
		assingedGeoUnions.addAll(listGeoUnionAssignedTo(role));
		if (!assingedGeoUnions.isEmpty()) {
			assignableGeoUnions.removeAll(assingedGeoUnions);
		}
		
		return assignableGeoUnions;
	}	

	/**
	 * list of GeoUnion assigned to users with given role
	 * */
	public static List<GeoUnion> listGeoUnionAssignedTo(Role fsRole) {
		return User.find("SELECT geoUnions FROM User as u LEFT JOIN u.geoUnionList as geoUnions WHERE u.role = ? AND geoUnions IS NOT NULL", fsRole).fetch();
	}

	/**
	 * list of GeoUnion assigned to user
	 * */
	public static List<GeoUnion> listGeoUnionAssignedTo(User user) {
		return User.find("SELECT geoUnions FROM User as u LEFT JOIN u.geoUnionList as geoUnions WHERE u = ? AND geoUnions IS NOT NULL", user).fetch();
	}
	
	/**
	 * list of GeoUnion child of GeoDistricts
	 * */
	public static List<GeoUnion> listGeoUnionChildOf(List<GeoDistrict> geoDistricts) {
		if (geoDistricts == null || geoDistricts.isEmpty()) {
			return new ArrayList<GeoUnion>();
		}
		return GeoUnion.find("geoDistrict in (:geoDistricts)").setParameter("geoDistricts", geoDistricts).fetch();	
	}
	
	
    @Override
    public String toString() {
    	return this.name+ "-" + this.code;
    }

	
    
}
 