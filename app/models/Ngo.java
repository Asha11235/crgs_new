package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class Ngo extends Model{
	
	@Unique
	@Required
	public String ngoName;
	
	@Required
	@ManyToOne
	public GeoDivision geoDivision;
	
	@Required
	@ManyToOne
	public GeoDistrict geoDistrict;
	
	@Required
	@ManyToOne
	public GeoUpazilla geoUpazilla;
	
	
	

}
