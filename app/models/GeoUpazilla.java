package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class GeoUpazilla extends Model {
	@Required
	@ManyToOne
	public GeoDistrict geoDistrict;
	
	@Required
	@ManyToOne
	public GeoDivision geoDivision;
	
	@Required
	public String name;	
	public String nameBn;
	
	@Required
	//@Length(max=2, min=1)
//	@Match("[0-9][0-9]")
	public String code;
	public String codeBn;
	
	public static GeoUpazilla findByUpazillaCode(String upazillaCode, GeoDistrict geoDistrict) {
		return GeoUpazilla.find("byCodeAndgeoDistrict", upazillaCode, geoDistrict).first();
	}
}
