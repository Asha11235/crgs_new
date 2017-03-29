package models;

import java.util.Date;
import java.util.List;

import interfaces.Assignable;

import javax.annotation.Resource;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Max;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class SchoolInformation extends Model {
	//@Required(message = "validation.required.emphasis")
	public String name;
	
	public String schoolRegNo;
	/*@Required
	public String address;*/
	
	public String totalStudent;
	public String maleStruden;
	public String femailStudent;
	
	public String maleToilets;
	public String femailToilets;
	public String totalToilets;
	
	public String totalTubewells;
	public String femaleToiletRatio;
	
	@ManyToOne
	public GeoDivision geoDivision;
	@ManyToOne
	public GeoDistrict geoDistrict;
	@ManyToOne
	public GeoUpazilla geoUpazilla;
	@ManyToOne
	public GeoUnion geoUnion;
	
	
	public String headSirPhonNumber;
	
	public String classTeacherPhoneNumber;
	
	public String schoolType;
	
	public String activeSchoolStudent;
	
	public String activeStudent;
	
	public String annualSports;
	
	public String countFourm;
	//@Required
	//@Max(11)
	public String schoolHeadSirMobileNumber;
	
	public Date registrationDate;
	
	public String approavedStatus;
	
	@ManyToOne
	public User approvedBy;
	
	public SchoolInformation(){
	}
}
