package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.persistence.PersistenceException;

import models.GeoDistrict;
import models.GeoDivision;
import models.GeoRegion;
import models.GeoUnion;
import models.GeoUpazilla;
import models.SchoolInformation;
import play.mvc.Util;

public class Import extends Controller{
	
	public static void importCSV() {
		render("@geoImport");
	}

	public static void submit() {

		//File mwra_csv = params.get("mwra_csv", File.class);
		File geo_location_csv = params.get("geo_location_csv", File.class);
//		File child_uid_csv = params.get("child_uid_csv", File.class);
//		File hh_csv = params.get("hh_csv", File.class);

		if (geo_location_csv != null){
			//readGEO_LocationCSV(geo_location_csv);
		    readSchool_InformationCSV(geo_location_csv);
		}
/*
		if (child_uid_csv != null)
			readChild_UIDCSV(child_uid_csv);
		if (hh_csv != null)
			readHH_CSV(hh_csv);*/

		flash.success("Record saved successfully.");
		render("@geoImport");
		
		

	}
	
	@Util
	public static void readGEO_LocationCSV(File geo_location_csv) {

		BufferedReader br = null;
		String line = "";
		String splitBy = ",";

		try {

			br = new BufferedReader(new FileReader(geo_location_csv));
			line = br.readLine();

			while ((line = br.readLine()) != null) {

				String[] beneficiary = line.split(splitBy);
				
				play.Logger.info("(Import.java ) line : " + line + " beneficiary num = " + beneficiary.length);
				play.Logger.info("(Import.java ) divBangla : " + beneficiary[0]);

				String regCode = beneficiary[0];
				String regEng = beneficiary[1];
				String regBangla = beneficiary[2];
				
				String divCode = beneficiary[3];
				String divEng = beneficiary[4];
				String divBangla = beneficiary[5];
				
				String distCode = beneficiary[6];
				String distEng = beneficiary[7];
				String distBangla = beneficiary[8];
				
				String upazillaCode = beneficiary[9];
				String upazillaEng = beneficiary[10];
				String upazillaBangla = beneficiary[11];
				
				String unionCode = beneficiary[12];
				String unionEng = beneficiary[13];
				String unionBangla = "" ; //beneficiary[14]; //TODO
				
				
				/*String mauzaCode = beneficiary[15];
				String mauzaEng = beneficiary[16];
				String mauzaBangla = beneficiary[17];
				
				
				String villageCode = beneficiary[18];
				String villageEng = beneficiary[19];
				String villageBangla = beneficiary[20];
				
				String enumArea = beneficiary[21];*/
				
			//	DummyMapping dummyMapping = new DummyMapping(divCode, , geoDivisionBn, geoDistrict, geoDistrictEn, geoDistrictBn, geoUpazilla, geoUpazillaEn, geoUpazillaBn, geoUnion, geoUnionEn, geoUnionBn, geoMauza, geoMauzaEn, geoMauzaBn, geoVillage, geoVillageEn, geoVillageBn, enumArea)
				
				/*DummyMapping dummy = new DummyMapping(divCode, divEng, divBangla, distCode, distEng, distBangla, upazillaCode, upazillaEng, upazillaBangla, unionCode, unionEng, unionBangla, mauzaCode, mauzaEng, mauzaBangla, villageCode, villageEng, villageBangla, enumArea);
				dummy.save();
				*/
				//play.Logger.info("div code " + divCode + " dist code " + distCode + " union english "+ unionEng);

				GeoRegion geoRegion = GeoRegion.findByRegionCode(regCode);

				if (geoRegion == null) {

					GeoRegion newGeoReg = new GeoRegion();
					newGeoReg.name = regEng;
					newGeoReg.code = regCode.trim();
					newGeoReg.nameBn = regBangla;
					
					play.Logger.info("reg code " + regCode); 
					newGeoReg.save();
					geoRegion = newGeoReg;
				}
				
				
				GeoDivision geoDivision = GeoDivision.findByDivisionCode(divCode);

				if (geoDivision == null) {

					GeoDivision newGeoDiv = new GeoDivision();
					newGeoDiv.name = divEng;
					newGeoDiv.code = divCode.trim();
				
					play.Logger.info("div code " + divCode); 
					newGeoDiv.save();
					geoDivision = newGeoDiv;
				}
				
				
				GeoDistrict geoDistrict = GeoDistrict.findByDistrictCode(distCode, geoDivision);
				
				if(geoDistrict == null) {
					
					GeoDistrict newGeoDist = new GeoDistrict();
					newGeoDist.name = distEng;
					newGeoDist.code = distCode;
				    newGeoDist.nameBn = distBangla;
					
					newGeoDist.geoDivision = geoDivision;
					
					newGeoDist.save();
					geoDistrict = newGeoDist;
				}
				
				GeoUpazilla geoUpazilla = GeoUpazilla.findByUpazillaCode(upazillaCode, geoDistrict);
				
				if(geoUpazilla == null) {
					
					GeoUpazilla newGeoUpazilla = new GeoUpazilla();
					newGeoUpazilla.name = upazillaEng;
					newGeoUpazilla.code = upazillaCode;
					newGeoUpazilla.nameBn = upazillaBangla;
					
					newGeoUpazilla.geoDistrict = geoDistrict;
					newGeoUpazilla.geoDivision = geoDivision;
					
					newGeoUpazilla.save();
					geoUpazilla = newGeoUpazilla;
				}
				
				GeoUnion geoUnion = GeoUnion.findByUnionCode(unionCode, geoUpazilla);
				
				if(geoUnion == null) {
					
					GeoUnion newGeoUnion = new GeoUnion();
					newGeoUnion.name = unionEng;
					newGeoUnion.code = unionCode;
					newGeoUnion.nameBn = unionBangla;
					
					newGeoUnion.geoUpazilla = geoUpazilla;
					newGeoUnion.geoDistrict = geoDistrict;
					newGeoUnion.geoDivision = geoDivision;
					
	
					newGeoUnion.save();
					geoUnion = newGeoUnion;
					
				}
				/*
				GeoMauza geoMauza = GeoMauza.findByMauzaCode(mauzaCode, geoUnion);
				
				
				if(geoMauza == null) {
					
					GeoMauza newGeoMauza = new GeoMauza();
					newGeoMauza.name = mauzaEng;
					newGeoMauza.code = mauzaCode;
					newGeoMauza.nameBn = mauzaBangla;
					
					newGeoMauza.geoUnion = geoUnion;
					newGeoMauza.geoUpazilla = geoUpazilla;
					newGeoMauza.geoDistrict = geoDistrict;
					newGeoMauza.geoDivision = geoDivision;
	
					newGeoMauza.save();
					geoMauza = newGeoMauza;
				}
				
				Logger.logInfo("Village code: " + villageCode);
				
				GeoVillage geoVillage = GeoVillage.findByVillageCode(villageCode, geoMauza);
				
				if(geoVillage == null) {
					
					GeoVillage newGeoVillage = new GeoVillage();
					newGeoVillage.name = villageEng;
					newGeoVillage.code = villageCode;
					newGeoVillage.nameBn = villageBangla;
					
					newGeoVillage.geoMauza = geoMauza;
					newGeoVillage.geoUnion = geoUnion;
					newGeoVillage.geoUpazilla = geoUpazilla;
					newGeoVillage.geoDistrict = geoDistrict;
					newGeoVillage.geoDivision = geoDivision;
	
					newGeoVillage.save();
					geoVillage = newGeoVillage;
				}
				
				Logger.logInfo("Enum Area: " + enumArea);
				
				GeoEnArea geoEnArea = GeoEnArea.findGeoEnArea(enumArea, geoVillage);
				
				if(geoEnArea == null) {
					
					GeoEnArea newGeoEnArea = new GeoEnArea();
					//newGeoEnArea.name = unionEng;
					newGeoEnArea.code = enumArea;
					//newGeoEnArea.nameBn = unionBangla;
					
					newGeoEnArea.geoVillage = geoVillage;
					newGeoEnArea.geoMauza = geoMauza;
					newGeoEnArea.geoUnion = geoUnion;
					newGeoEnArea.geoUpazilla = geoUpazilla;
					newGeoEnArea.geoDistrict = geoDistrict;
					newGeoEnArea.geoDivision = geoDivision;
	
					newGeoEnArea.save();
					geoEnArea = newGeoEnArea;
				}
				
				
*/
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done with reading CSV");
	}
	
	@Util
	public static void readSchool_InformationCSV(File schoole_location_information){
		BufferedReader br = null;
		String line = "";
		String splitBy = ",";

		try {

			br = new BufferedReader(new FileReader(schoole_location_information));
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] schooles = line.split(splitBy);
				String name = schooles[0];
				String address = schooles[1];
				String totalStudent = schooles[2];
				String maleStruden = schooles[3];
				String femailStudent = schooles[4];
				
				String totalToilets = schooles[5];
				String totalTubewells = schooles[6];
				SchoolInformation schoolInformation = new SchoolInformation();
				schoolInformation.name = name;
				//schoolInformation.address = address;
				schoolInformation.totalStudent = totalStudent;
				schoolInformation.maleStruden = maleStruden;
				schoolInformation.femailStudent = femailStudent;
				schoolInformation.totalToilets = totalToilets;
				schoolInformation.totalTubewells = totalTubewells;
				try{
					schoolInformation.save();
				}catch(PersistenceException p){
					p.printStackTrace();
				}
			}
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
