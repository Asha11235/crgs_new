package models;

import javax.persistence.Entity;

import play.db.jpa.Model;


@Entity
public class DummyMapping extends Model{
	
	public String geoDivision;
	public String geoDivisionEn;
	public String geoDivisionBn;
	
	public String geoDistrict;
	public String geoDistrictEn;
	public String geoDistrictBn;
	
	public String geoUpazilla;
	public String geoUpazillaEn;
	public String geoUpazillaBn;
	
	public String geoUnion;
	public String geoUnionEn;
	public String geoUnionBn;
	
	public String geoMauza;
	public String geoMauzaEn;
	public String geoMauzaBn;
	
	public String geoVillage;
	public String geoVillageEn;
	public String geoVillageBn;
	
	public String enumArea;
	
	public DummyMapping(String geoDivision, String geoDivisionEn, String geoDivisionBn, 
			            String geoDistrict, String geoDistrictEn, String geoDistrictBn,
			            String geoUpazilla, String geoUpazillaEn, String geoUpazillaBn, 
			            String geoUnion,String geoUnionEn, String geoUnionBn,
			            String geoMauza,String geoMauzaEn,String geoMauzaBn,
			            String geoVillage, String geoVillageEn, String geoVillageBn,
			            String enumArea) {
		// TODO Auto-generated constructor stub
		
		this.geoDivision = geoDivision;
		this.geoDivisionEn = geoDivisionEn;
		this.geoDistrictBn = geoDivisionBn;
		
		this.geoDistrict = geoDistrict;
		this.geoDistrictEn = geoDistrictEn;
		this.geoDistrictBn = geoDistrictBn;
		
		this.geoUpazilla = geoUpazilla;
		this.geoUpazillaEn = geoUpazillaEn;
		this.geoUpazillaBn = geoUpazillaBn;
		
		this.geoUnion = geoUnion;
		this.geoUnionEn = geoUnionEn;
		this.geoUnionBn = geoUnionBn;
		
		
		this.geoMauza = geoMauza;
		this.geoMauzaEn = geoMauzaEn;
		this.geoMauzaBn = geoMauzaBn;
		
		this.geoVillage = geoVillage;
		this.geoVillageEn = geoVillageEn;
		this.geoVillageBn = geoVillageBn;
		
		this.enumArea = enumArea;
		
	}

}
