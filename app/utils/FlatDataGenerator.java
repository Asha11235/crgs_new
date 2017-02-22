package utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.Query;

import org.w3c.dom.Document;

import models.Data;
import models.FlatData;
import models.TrainingInformation;
import models.TrainingMemberInformation;
import models.UnitData;
import play.Play;
import play.db.jpa.JPA;
import play.libs.XML;
import play.libs.XPath;

import com.jamonapi.utils.Logger;

public class FlatDataGenerator {

	public static void generateFlat(Data data) {

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		String search_query = "";
		Statement stmt = null;
		Query query = null;
		TrainingMemberInformation t_information = null;
		
	/*	java.sql.Timestamp startTime = new java.sql.Timestamp(data.startTime.getTime());
		java.sql.Timestamp endTime = new java.sql.Timestamp(data.endTime.getTime());*/
		java.sql.Timestamp received = new java.sql.Timestamp(data.received.getTime());
		
		if(data.form.shortName.equalsIgnoreCase(("FPGTRR"))) {
			saveTrainingBasicInfo(data);
		}		
		
		query = JPA
				.em()
				.createNativeQuery(
						//"create table if not exists tblRegistrationnew (data_id bigint(50) not null, user_ID varchar(50), startTime datetime, endTime datetime, received datetime, status int(10), approved_by varchar(50), approved_date datetime, PRIMARY KEY (data_id))");
						"create table if not exists tblFlat (data_id bigint(50) not null, form_id bigint(50) not null, user_ID varchar(50), startTime datetime, endTime datetime, received datetime, status int(10), approved_by varchar(50), approved_date datetime, latitude varchar(50), longitude varchar(50), hhreg_location varchar(255), PRIMARY KEY (data_id))");

		query.executeUpdate();

		/*query = JPA
				.em()
				.createNativeQuery(
						"create table if not exists tblFollowupnew (visit_ID bigint(50) not null, user_ID varchar(50), startTime datetime, endTime datetime, received datetime, HH_ID varchar(50), PRIMARY KEY (visit_ID))");

		query.executeUpdate();
*/
		List<UnitData> listUnitData = UnitData.find(
				"SELECT u from UnitData u where u.data =? order by id", data)
				.fetch();

		try {
				/*query = JPA
						.em()
						.createNativeQuery(
								"INSERT INTO tblFlat (data_id,form_id,startTime,endTime,received, user_ID) values("
										+ data.id
										+ ","
										+ data.form.id
										+ ",'"
										+ startTime
										+ "' , '"
										+ endTime
										+ "' , '"
										+ received
										+ "' ,'"
										+ data.sender.id + "'" + ")");*/
				
				query = JPA
						.em()
						.createNativeQuery(
								"INSERT INTO tblFlat (data_id,form_id,received, user_ID) values("
										+ data.id
										+ ","
										+ data.form.id
										+ ",'"
										+ received
										+ "' ,'"
										+ data.sender.id + "'" + ")");
				
				query.executeUpdate();

				int counter = 0;
				for (UnitData unitData : listUnitData) {					
					
					
					if(unitData.titleVar.contains("trainees/")){
						
						if(unitData.iterationNo > counter){
							t_information = new TrainingMemberInformation();
							counter = unitData.iterationNo;
						}
						//t_information.trainingId = data.traningId;
						t_information.data = data;
						
						String fieldName = unitData.titleVar.replace("data/trainees/", "");
						Logger.logInfo("Retrieving Field Name : " + fieldName);
						
						Field field;
						try {
							if(fieldName.equalsIgnoreCase("FPGTRR_farmer_id")){
								Logger.logInfo("Setting ValueVar : " + unitData.valueVar);
								String[] farmerInfo = unitData.valueVar.split(",");
								t_information.FPGTRR_PG_farmerId = farmerInfo[0];
								t_information.FPGTRR_PG_farmerName = farmerInfo[1];
								t_information.FPGTRR_PG_husband = farmerInfo[2];
								t_information.FPGTRR_PG_father = farmerInfo[3];
								t_information.FPGTRR_PG_Gender = farmerInfo[4];
							}else{
								field = t_information.getClass().getDeclaredField(fieldName);
								Logger.logInfo("Setting Value : " + unitData.valueVar);								
								field.set(t_information, unitData.valueVar.replaceAll(",", "-"));								
							}							
						} catch (NoSuchFieldException e1) {
							Logger.logInfo("No field named " + fieldName + " in Household Table");
						}
						catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						finally{
							t_information.save();
						}
						continue;					
					}		
					
					// check if column exist
					String dbName = Play.configuration.getProperty("ed.db.name");
					String flatTableName = Play.configuration.getProperty("ed.flattable.name");
					search_query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+dbName+"' AND TABLE_NAME = '"+flatTableName+"' AND COLUMN_NAME = '"
							+ unitData.titleVar.replaceAll("data/", "").replaceAll("/", "__")+"__"+unitData.iterationNo + "'";
					Logger.logInfo("search query " + search_query);
					
					stmt = conn.createStatement();
					rs = stmt.executeQuery(search_query);
					rs.next();					
						Logger.logInfo("rs.getString(1) = " +rs.getString(1));		
					/* if column exist only update the column*/
					if (rs.getString(1).matches("1")) {
						
						if(unitData.valueVar != null){
							unitData.valueVar = unitData.valueVar.replaceAll("'", "");							
							
							String update = "Update tblFlat set "
									+ unitData.titleVar.replaceAll("data/", "").replaceAll("'", "").replaceAll("/", "__") + "__"+unitData.iterationNo + " = '"
									+ unitData.valueVar + "'"
									+ " where data_id = " + data.id;
							
							Logger.logInfo("update query " + update);
							query = JPA.em().createNativeQuery(update);

							query.executeUpdate();
						}						
					}

					/*if column not exist create the column and then update*/
					else {
						
						String colName = unitData.titleVar.replaceAll("data/", "").replaceAll("/", "__") +"__"+unitData.iterationNo;
						String alterQuery = "ALTER TABLE tblFlat ADD " + colName + " VARCHAR(60)";
						query = JPA.em().createNativeQuery(alterQuery);
						Logger.logInfo("Alter Query " + alterQuery);
						
						query.executeUpdate();
						
						if(unitData.valueVar != null){
							String update = "Update tblFlat set "
									+ unitData.titleVar.replaceAll("data/", "").replaceAll("/", "__")+"__"+unitData.iterationNo + " = '"
									/* + unitData.value + "__" */ + unitData.valueVar + "'"
									+ " where data_id = " + data.id;
							Logger.logInfo("update query : " + update);
							query = JPA.em().createNativeQuery(update);
							
							query.executeUpdate();
						}
					}
					
					if (unitData.type.matches("gmap")
							|| unitData.type.matches("geopoint")) {
						
						if(unitData.value != null ){
							
							String long_lat = unitData.value;
							String location[] = long_lat.split(",");
							/*String hhreg_location = unitData.extraValue.replace(
									"'", "");
	*/
							/*query = JPA.em().createNativeQuery(
									"Update tblFlat set latitude = '"
											+ location[0] + "', longitude = '"
											+ location[1] + "', hhreg_location = '"
											+ hhreg_location + "' where data_id = "
											+ data.id);*/
							query = JPA.em().createNativeQuery(
									"Update tblFlat set latitude = '"
											+ location[0] + "', longitude = '"
											+ location[1] + "' where data_id = "
											+ data.id);
							
	
							query.executeUpdate();
						}
					}

				}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void generateUpdatedFlat(Data data, List<FlatData> flatDatas) {
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		Query query = null;
		String search_query = "";

		java.sql.Date startTime = new java.sql.Date(data.startTime.getTime());
		java.sql.Date endTime = new java.sql.Date(data.endTime.getTime());
		java.sql.Date received = new java.sql.Date(data.received.getTime());

		/*query = JPA.em().createNativeQuery(
				"Delete from tblRegistrationnew where data_id = " + data.id);
				query.executeUpdate();*/
		query = JPA.em().createNativeQuery(
				"Delete from tblFlat where data_id = " + data.id);
				query.executeUpdate();
		play.Logger.info("sender  " + data.sender.name);
/*		query = JPA
				.em()
				.createNativeQuery(
						"INSERT INTO tblRegistrationnew (data_id,startTime,endTime,received, user_ID) values("
								+ data.id
								+ ",'"
								+ startTime
								+ "' , '"
								+ endTime
								+ "' , '"
								+ received
								+ "' ,'"
								+ data.sender.id + "'" + ")");*/
		query = JPA
				.em()
				.createNativeQuery(
						"INSERT INTO tblFlat (data_id,startTime,endTime,received, user_ID) values("
								+ data.id
								+ ",'"
								+ startTime
								+ "' , '"
								+ endTime
								+ "' , '"
								+ received
								+ "' ,'"
								+ data.sender.id + "'" + ")");

		query.executeUpdate();
		try {
			for (FlatData flatData : flatDatas) {

				// check if column exist
				String dbName = Play.configuration.getProperty("ed.db.name");
				String flatTableName = Play.configuration.getProperty("ed.flattable.name");
				search_query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+dbName+"' AND TABLE_NAME = '"+flatTableName+"' AND COLUMN_NAME = '"
						+ flatData.titleVar.replaceAll("/", "__")+"__"+flatData.iterationNo + "'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(search_query);
				rs.next();
				
				play.Logger.info("flat value "+ flatData.value +"  "+flatData.valueVar);
				
				// if column exist only update the column
				if (rs.getString(1).matches("1")) {
					query = JPA.em().createNativeQuery(
							"Update " +flatTableName+  " set "
									+ flatData.titleVar.replaceAll("/", "__")
											.trim()+"__"+flatData.iterationNo + " = '" + flatData.value
									+ "__" + flatData.valueVar + "'"
									+ " where data_id = " + data.id);
					
					query.executeUpdate();
				}

				// if column not exist create the column and then update
				else {
					query = JPA.em().createNativeQuery(
							"ALTER TABLE " +flatTableName+  " ADD "
									+ flatData.titleVar.replaceAll("/", "__")
											.trim() + " VARCHAR(60)");

					query.executeUpdate();

					query = JPA.em().createNativeQuery(
							"Update " +flatTableName+  " set "
									+ flatData.titleVar.replaceAll("/", "__")
											.trim()+"__"+flatData.iterationNo + " = '" + flatData.value
									+ "__" + flatData.valueVar + "'"
									+ " where data_id = " + data.id);

					query.executeUpdate();

				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void generateUpdatedFlatForWeb(Data data, List<FlatData> flatDatas) {
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		Query query = null;
		String search_query = "";

		try {
			for (FlatData flatData : flatDatas) {

				// check if column exist
				String dbName = Play.configuration.getProperty("ed.db.name");
				String flatTableName = Play.configuration.getProperty("ed.flattable.name");
				search_query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+dbName+"' AND TABLE_NAME = '"+flatTableName+"' AND COLUMN_NAME = '"
						+ flatData.titleVar.replaceAll("/", "__")+"__"+flatData.iterationNo + "'";
				stmt = conn.createStatement();

				rs = stmt.executeQuery(search_query);

				rs.next();

				// if column exist only update the column
				if (rs.getString(1).matches("1")) {
					query = JPA.em().createNativeQuery(
							"Update " +flatTableName+  " set "
									+ flatData.titleVar.replaceAll("/", "__")
											.trim()+"__"+flatData.iterationNo + " = '" + flatData.value
									+ "__" + flatData.valueVar + "'"
									+ " where data_id = " + data.id);

					query.executeUpdate();
				}

				// if column not exist create the column and then update
				else {
					query = JPA.em().createNativeQuery(
							"ALTER TABLE " +flatTableName+  " ADD "
									+ flatData.titleVar.replaceAll("/", "__")
											.trim()+"__"+flatData.iterationNo + " VARCHAR(60)");

					query.executeUpdate();

					query = JPA.em().createNativeQuery(
							"Update " +flatTableName+  " set "
									+ flatData.titleVar.replaceAll("/", "__")
											.trim()+"__"+flatData.iterationNo + " = '" + flatData.value
									+ "__" + flatData.valueVar + "'"
									+ " where data_id = " + data.id);

					query.executeUpdate();

				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveTrainingBasicInfo(Data data){
		Document dataXml = XML.getDocument(new String(data.xml));
		TrainingInformation ti = new TrainingInformation();
		ti.trainingName = XPath.selectText("//data/FPGTRR_t_name",
				dataXml);
		ti.numberOfParticipants = XPath.selectText("//data/FPGTRR_Participents_total",
				dataXml);
		ti.numberOfMale = XPath.selectText("//data/FPGTRR_Participents_male",
				dataXml);
		ti.numberOfFeMale = XPath.selectText("//data/FPGTRR_Participents_female",
				dataXml); 
		ti.sender = data.sender;
		ti.data = data;
		//ti.trainingId = data.traningId;
		ti.received = data.received;
		ti.save();
		play.Logger.info("(Mobile.java) TrainingInfomation saved");
	}

}
