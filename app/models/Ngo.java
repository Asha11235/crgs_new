package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.Logger;
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
	
	
	public static String getNgoData(Long divisionId, Long districtId, Long upazillaId) throws SQLException {
		
		String qString = null;
		
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		
		qString = "SELECT Ngo.ngoName as ngo_name , GeoDivision.name as geodivision_name, GeoDistrict.name as geodistrict_name, GeoUpazilla.name as geoupazilla_name"+
                  " From Ngo "+
                  " JOIN GeoDivision on Ngo.geoDivision_id = GeoDivision.id "+
                  " JOIN GeoDistrict on Ngo.geoDistrict_id = GeoDistrict.id "+
                  " JOIN GeoUpazilla on Ngo.geoUpazilla_id = GeoUpazilla.id ";
		
		String whereClause = " WHERE ";

		if (divisionId != null) {

			whereClause += " Ngo.geoDivision_id = " + divisionId;

		}
		if (districtId != null) {

			whereClause += " and Ngo.geoDistrict_id = " + districtId;

		}
		if (upazillaId != null) {

			whereClause += " and Ngo.geoUpazilla_id = " + upazillaId;

		}
		
        qString = qString + whereClause;
		
		Logger.info("qString1 : " + qString);

		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		rs = queryForExecution.executeQuery();
		
		String ngoname=null;
		String divisionname=null;
		String districtname=null;
		String upazillaname=null;
		String msg="";
		try {

			while (rs.next()) {

				
				ngoname = rs.getString("ngo_name") ;
				divisionname = rs.getString("geodivision_name") ;
				districtname = rs.getString("geodistrict_name") ;
				upazillaname = rs.getString("geoupazilla_name") ;
				
				msg = msg + ";" + ngoname + ";" + divisionname + ";" + districtname + ";" + upazillaname ;
				//count++;
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		
		return msg;
	}

}
