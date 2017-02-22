package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class CaseReport extends Model {
	
	@Required
	public String informerName;
	
	public String mobNumber;
	
	public String victimName;
	
	public String victimAge;
	
	public String victimSex;
	
	@ManyToOne
	@Required
	public GeoDivision geoDivision;
	
	@ManyToOne
	@Required
	public GeoDistrict geoDistrict;
	
	@ManyToOne
	@Required
	public GeoUpazilla geoUpazilla;
						
	@ManyToOne
	@Required
	public GeoUnion geoUnion;
	
	@Column(columnDefinition = "TEXT")
	public String description;
	
	@ManyToOne
	@Required
	public SchoolInformation associateSchool;
	
	public String currentStatus;
	
	
	

}
