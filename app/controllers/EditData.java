package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import models.Data;
import models.FlatData;
import models.FormResource;
import models.OptionsInForm;
import models.UnitData;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.mvc.Util;
import play.mvc.With;
import utils.FlatDataGenerator;



@With(Deadbolt.class)
public class EditData extends Controller{
	
    @ExternalRestrictions("View Data")
    @Util
    public static void viewEditData(Long id) {
    	Data data = Data.findById(id);
    	notFoundIfNull(data);    	
		List<UnitData> list = new ArrayList();
		List<UnitData> rawDataList = UnitData.find("byData", data).fetch();
		// Supress multiple select options with last tuple
		UnitData tmpUnitdata = null;
		Boolean selectFlag = false;
		for (UnitData unitdata : rawDataList) {
			if ("select".equals(unitdata.type) && (tmpUnitdata == null)) {
				tmpUnitdata = unitdata;
				continue;
			} else if ("select".equals(unitdata.type)
					&& (tmpUnitdata.id < unitdata.id)) {
				tmpUnitdata = unitdata;
				selectFlag = true;
				continue;
			}
			if (selectFlag) {
				list.add(tmpUnitdata);
				tmpUnitdata = null;
				selectFlag = false;
			}
			list.add(unitdata);
		}
    	String enableAudioText = Play.configuration.getProperty("aggregate.enableAudioText");    	
    	List<OptionsInForm>optionsList = OptionsInForm.find("byForm", data.form).fetch();    	
    	render(data, list, optionsList); 	
      }
    
    @ExternalRestrictions("View Data")
    @Util
    public static void editDataFromFlat(Long id) throws SQLException {
		Data data = Data.findById(id);
		notFoundIfNull(data);

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		String search_query = "";
		Statement stmt = null;

		String dbName = Play.configuration.getProperty("wb.db.name");
		String flatTableName = Play.configuration.getProperty("wb.flattable.name");
		search_query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + dbName + "'  AND TABLE_NAME = '" + flatTableName + "'";

		stmt = conn.createStatement();
		rs = stmt.executeQuery(search_query);
		List<String> columns = new ArrayList<String>();
		while (rs.next()) {
			columns.add(rs.getString(1));
		}

		search_query = "SELECT * FROM tblRegistrationnew where data_id=" + id
				+ " order by data_id";

		stmt = conn.createStatement();
		rs = stmt.executeQuery(search_query);

		List<FlatData> flatDatas = new ArrayList<FlatData>();

		while (rs.next()) {
			for (String column : columns) {
				String nodePaths[]=column.split("__");
				String interation = nodePaths[nodePaths.length-1];
				String nodePath="";
				for(int i=1;i<nodePaths.length-1; i++){
					nodePath += nodePaths[i]+"/";
				}
				if(nodePath.length()>0)
						nodePath = nodePath.substring(0,nodePath.length()-1);
				FormResource dataInfo = FormResource.find("byNodePathAndform",
						nodePath, data.form).first();

				if (dataInfo != null && rs.getString(column)!=null) {
					String values[] = rs.getString(column).split("__");
					FlatData flatData = new FlatData();
					flatData.titleVar = dataInfo.nodePath;
					flatData.title = dataInfo.title;
					flatData.value = values[0];
					flatData.valueVar = values[1];
					flatData.iterationNo = Integer.parseInt(interation);
					flatData.type = dataInfo.type;
					flatData.formResource = dataInfo;
					flatDatas.add(flatData);
				}
			}
		}		
    	String enableAudioText = Play.configuration.getProperty("aggregate.enableAudioText");    	
    	List<OptionsInForm>optionsList = OptionsInForm.find("byForm", data.form).fetch();    	
    	render(data, flatDatas, optionsList);
    }
    
    
	public static void submitUpdatedDataFromFlat(Long id) throws SQLException {

		Data data = Data.findById(id);
		notFoundIfNull(data);
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		String search_query = "";
		Statement stmt = null;
		
		String dbName = Play.configuration.getProperty("wb.db.name");
		String flatTableName = Play.configuration.getProperty("wb.flattable.name");
		search_query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+dbName+"' AND TABLE_NAME = '"+flatTableName+"'";

		stmt = conn.createStatement();
		rs = stmt.executeQuery(search_query);
		List<String> columns = new ArrayList<String>();
		while (rs.next()) {
			columns.add(rs.getString(1));
		}

		List<FlatData> flatDatas = new ArrayList<FlatData>();
		for (String column : columns) {
			String nodePaths[]=column.split("__");
			String iteration = nodePaths[nodePaths.length-1];
			String nodePath="";
			for(int i=1;i<nodePaths.length-1; i++){
				nodePath += nodePaths[i]+"/";
			}
			if(nodePath.length()>0)
					nodePath = nodePath.substring(0,nodePath.length()-1);
			
//			Logger.info("columns "+column);
			if (request.params.get(nodePath) != null) {
				String values = request.params.get(nodePath);

				FormResource dataInfo = FormResource.find("byNodePathAndform",
						nodePath, data.form).first();

				if (dataInfo != null) {
					FlatData flatData = new FlatData();
					flatData.titleVar = "data/"+dataInfo.nodePath;
					flatData.title = dataInfo.title;
					flatData.iterationNo = Integer.parseInt(iteration);
					if (dataInfo.type.matches("select1")) {
						String value[] = values.split(",");
						flatData.valueVar = value[0];
						flatData.value = value[1];
					} else if (dataInfo.type.matches("select")) {
						String selectedValues = "";
						String selectedValueVar = "";
						String multiValues[] = request.params.getAll(nodePath);
						for (String values1 : multiValues) {
							FlatData flatData1 = new FlatData();
							String value[] = values1.split(",");
							flatData1.valueVar = value[0];
							flatData1.value = value[1];
							selectedValueVar += value[0]+",";
							selectedValues += value[1]+",";
							flatData1.title = dataInfo.title;
							flatData1.iterationNo = Integer.parseInt(iteration);
							flatData1.titleVar = "data/"+dataInfo.nodePath+"_"+value[0];
							flatData1.type = dataInfo.type;
							flatData1.formResource = dataInfo;
							flatDatas.add(flatData1);
						}
						flatData.valueVar = selectedValueVar;
						flatData.value = selectedValues;
						flatData.type = dataInfo.type;
						flatData.formResource = dataInfo;
						flatDatas.add(flatData);
					} else {
						flatData.value = values;
						flatData.valueVar = values;
					}
					flatData.type = dataInfo.type;
					flatData.formResource = dataInfo;
					flatDatas.add(flatData);
				}
			}
		}
		
		FlatDataGenerator.generateUpdatedFlat(data, flatDatas);
		
		viewFromFlat(data.id);
	}
    
    public static void submitUpdatedData(Long id) {
    	
    	Data data = Data.findById(id);
    	List<UnitData>ansList = UnitData.find("byData", data).fetch();
    	for(UnitData unitData: ansList){
    		if(request.params.get(unitData.id.toString())!=null){
    			String value = request.params.get(unitData.id.toString());
    			UnitData unit = UnitData.findById(unitData.id);
    			unit.value = value;
    			unit.save();
    		}
    	}
    	
    	flash.success("Record Updated successfully.");
    	//Forms.viewData(id);
    	
 //   	FlatDataGenerator.generateFlat(data);
	}
    
	public static void viewFromFlat(Long id) throws SQLException {
		Data data = Data.findById(id);
		notFoundIfNull(data);

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		String search_query = "";
		Statement stmt = null;

		String dbName = Play.configuration.getProperty("wb.db.name");
		String flatTableName = Play.configuration.getProperty("wb.flattable.name");
		search_query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+dbName+"' AND TABLE_NAME = '"+flatTableName+"'";

		stmt = conn.createStatement();
		rs = stmt.executeQuery(search_query);
		// rs.next();
		List<String> columns = new ArrayList<String>();
		while (rs.next()) {
			// Logger.info("columns   "+ rs.getString(1));
			columns.add(rs.getString(1));
		}

		search_query = "SELECT * FROM tblRegistrationnew where data_id=" + id
				+ " order by data_id";

		stmt = conn.createStatement();
		rs = stmt.executeQuery(search_query);

		List<FlatData> flatDatas = new ArrayList<FlatData>();

		while (rs.next()) {
			for (String column : columns) {
//				 Logger.info("column  "+column +"   "+ rs.getString(column));
				String nodePaths[]=column.split("__");
				String interation = nodePaths[nodePaths.length-1];
				String nodePath="";
				for(int i=1;i<nodePaths.length-1; i++){
					nodePath += nodePaths[i]+"/";
				}
				if(nodePath.length()>0)
						nodePath = nodePath.substring(0,nodePath.length()-1);
				
				FormResource dataInfo = FormResource.find("byNodePathAndform",
						nodePath, data.form).first();

				if (dataInfo != null && rs.getString(column)!=null) {
					String values[] = rs.getString(column).split("__");
					FlatData flatData = new FlatData();
					flatData.title = dataInfo.title;
					flatData.value = values[0];
					flatData.valueVar = values[1];
					flatData.iterationNo = Integer.parseInt(interation);
					flatData.type = dataInfo.type;
					/*Logger.info("data  " + flatData.title + "  "
							+ flatData.value);*/
					flatData.formResource = dataInfo;
					flatDatas.add(flatData);
				}

			}
		}
		
    	String enableAudioText = Play.configuration.getProperty("aggregate.enableAudioText");
    	
    	render(data, flatDatas, enableAudioText);
	}
}