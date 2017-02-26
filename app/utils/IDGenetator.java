package utils;

import play.Logger;
import models.GeoDistrict;
import models.GeoDivision;
import models.GeoUnion;
import models.GeoUpazilla;
import models.User;

public class IDGenetator {
	
	public static String getGeneratedId(Long userId)
	{
		String generatedId = "";
		
		User user = User.findById(userId);
		
		//code of Mobinul Islam - GeoUnion union = GeoUnion.findById(user.geoUnion.id);
		/*GeoUnion union = user.geoUnionList.iterator().next();//code of Muzahidul Islam
		Logger.info("Union ID :"+ union.code);
		
		GeoUpazilla upazilla = GeoUpazilla.findById(union.geoUpazilla.id);
		Logger.info("upazilla ID :"+ upazilla.code);
		
		GeoDistrict district = GeoDistrict.findById(upazilla.geoDistrict.id);
		Logger.info("district ID :"+ district.code);
		
		GeoDivision division = GeoDivision.findById(district.geoDivision.id);
		Logger.info("division ID :"+ division.code);
		
		
		generatedId = division.code + district.code + upazilla.code + union.code;*/
		
		return generatedId;
	}
	public  String generateID (String getIdBy){
		String ID = null;
		if ("DB".equals(getIdBy)){
			
		}else if ("AUTOSEQUENCE".equals(getIdBy)){
			
		}else if ("GEO".equals(getIdBy)){
			
		}else if ("SUBMISSION".equals(getIdBy)){
			
		}else if ("REGEX".equals(getIdBy)){
			
		}else if ("MIXED".equals(getIdBy)){
			
		}		
	return ID;	
	}
	public  String generateLinkedID (String getIdBy){
		String ID = null;
		if ("GEO".equals(getIdBy)){
			
		}else if ("SUBMISSION".equals(getIdBy)){
			
		}else if ("REGEX".equals(getIdBy)){
			
		}else if ("MIXED".equals(getIdBy)){
			
		}		
	return ID;	
	}
}
