package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.Logger;
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
	
	public String caseType;
	
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

	public static String getCaseReport(Long divisionId,Long districtId, Long upazillaId, Long unionId ,int caseType) throws SQLException {
		
		String qString = null;
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		
		qString = " SELECT CaseReport.informerName , CaseReport.MobNumber, CaseReport.victimSex , CaseReport.caseType, CaseReport.currentStatus, "+
		          " NewCrgs.GeoDivision.name as geodivision_name, NewCrgs.GeoDistrict.name as geodistrict_name, NewCrgs.GeoUpazilla.name as geoupazilla_name, "+
				  " NewCrgs.GeoUnion.name as geounion_name "+
				  " FROM CaseReport " +
		          " JOIN GeoDivision ON NewCrgs.GeoDivision.id = CaseReport.geoDivision_id "+
				  " JOIN GeoDistrict ON NewCrgs.GeoDistrict.id = CaseReport.geoDistrict_id "+
		          " JOIN GeoUpazilla ON NewCrgs.GeoUpazilla.id = CaseReport.geoUpazilla_id "+
				  " JOIN GeoUnion ON NewCrgs.GeoUnion.id = CaseReport.geoUnion_id ";
		
		String whereClause = " ";
		
		String arr[]= {"Rape" , "Mental Torture" , "Physical Assault" , "Sexual Assault", "Forced Prostitution" , "Influenced Suicide" , "Abduction" , "Forced/Underaged Marriage" , "Threat" , "Acid Violence" , "Domestic Violence" , "Physical/Corporal & Mental Punishment" , "Other" };

		if (divisionId != null) {

			whereClause += " where CaseReport.geoDivision_id = " + divisionId;

		}
		if (districtId != null) {

			whereClause += " and CaseReport.geoDistrict_id = " + districtId;

		}
		if (upazillaId != null) {

			whereClause += " and CaseReport.geoUpazilla_id = " + upazillaId;

		}
		
		if (unionId != null) {

			whereClause += " and CaseReport.geoUnion_id = " + unionId;

		}
		
		Logger.info("caseType: " + caseType);
		if (caseType != 20) {

			whereClause += " and CaseReport.caseType = '" + arr[caseType] + "'";

		}
		
        qString = qString + whereClause;
		
		Logger.info("qString1 : " + qString);

		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		rs = queryForExecution.executeQuery();
		
		String informerName=null;
		String victimSex=null;
		String divisionname=null;
		String districtname=null;
		String upazillaname=null;
		String unionname=null;
		String currentstatus=null;
		String mobnumber=null;
		String msg="";
		String casetype=null;
		
		try {

			while (rs.next()) {

				informerName = rs.getString("informerName");
				victimSex = rs.getString("victimSex");
				casetype = rs.getString("caseType");
				mobnumber = rs.getString("mobnumber") ;
				divisionname = rs.getString("geodivision_name") ;
				districtname = rs.getString("geodistrict_name") ;
				upazillaname = rs.getString("geoupazilla_name") ;
				unionname = rs.getString("geounion_name") ;
				currentstatus = rs.getString("currentStatus");
				
				msg = msg + ";" + informerName + ";" + victimSex +  ";" + casetype + ";" + mobnumber + ";" +
				divisionname + ";" + districtname + ";" + upazillaname + ";" + unionname + ";" + currentstatus  ;
				//count++;
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		
		return msg;
	
	}


}
