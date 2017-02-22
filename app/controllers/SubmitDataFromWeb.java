package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import play.Logger;
import play.Play;
import play.mvc.With;
import utils.FlatDataGenerator;
import controllers.deadbolt.Deadbolt;
import models.Data;
import models.FlatData;
import models.Form;
import models.FormResource;

@With(Deadbolt.class)
public class SubmitDataFromWeb extends Controller{

	public static void viewMobileDataList(){
		long id = 1;
    	Form form = Form.findById(id);
		List<Data> data = Data.find("form = ? order by id desc", form).fetch();
    	render(form, data);
	}

	public static void viewMobileDataAndWebField(Long id) throws SQLException{
		
		Data data = Data.findById(id);
		notFoundIfNull(data);

		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		String search_query = "";
		Statement stmt = null;

		search_query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'world_bank' AND TABLE_NAME = 'tblRegistrationnew'";

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
				// Logger.info("column  "+column +"   "+ rs.getString(column));
				String nodePath=column.substring(6);
				FormResource dataInfo = FormResource.find("byNodePathAndform",
						nodePath, data.form).first();

				if (dataInfo != null) {
					if(rs.getString(column) != null){
						String values[] = rs.getString(column).split("__");
						FlatData flatData = new FlatData();
						flatData.title = dataInfo.title;
						flatData.value = values[0];
						flatData.valueVar = values[1];
						flatData.type = dataInfo.type;
						/*Logger.info("data  " + flatData.title + "  "
								+ flatData.value);*/
						flatData.formResource = dataInfo;
						flatDatas.add(flatData);
					}
				}

			}
		}
		
    	String enableAudioText = Play.configuration.getProperty("aggregate.enableAudioText");
    	
    	render(data, flatDatas, enableAudioText);
	}
	
	public static void submitWebData(Long id) throws SQLException{
		Logger.info(" data is  "+id);
		Data data = Data.findById(id);
		String webEntryDatas[] = new String[8];
		 webEntryDatas[0] = request.params.get("budgetInstruction");
		 webEntryDatas[1] = request.params.get("trainingCost");
		 webEntryDatas[2] = request.params.get("communicationCost");
		 webEntryDatas[3] = request.params.get("totalCost");
		 webEntryDatas[4] = request.params.get("actualBudgetInstruction");
		 webEntryDatas[5] = request.params.get("actualTrainingCost");
		 webEntryDatas[6] = request.params.get("actualCommunicationCost");
		 webEntryDatas[7] = request.params.get("actualTotalCost");
		 
		 String nodePath[] = new String[8];
		 
		 nodePath[0] = "budgetInstruction"; nodePath[1] = "trainingCost"; nodePath[2] = "communicationCost"; 
		 nodePath[3] = "totalCost"; nodePath[4] = "actualBudgetInstruction"; nodePath[5] = "actualTrainingCost";
		 nodePath[6] = "actualCommunicationCost"; nodePath[7] = "actualTotalCost";
		 
		 Long fid = (long) 2;
		 Form form = Form.findById(fid);
		 
/*		  new FormResource(form, "Budget Money Instruction", "budgetInstruction", "string").save();
		  new FormResource(form, "Budget Training Cost", "trainingCost", "string").save();
		  new FormResource(form, "Budget Communication Cost", "communicationCost", "string").save();
		  new FormResource(form, "Budget Money Total", "totalCost", "string").save();
		  new FormResource(form, "Actual Expenditure Instruction", "actualBudgetInstruction", "string").save();
		  new FormResource(form, "Actual Expenditure Training Cost", "actualTrainingCost", "string").save();
		  new FormResource(form, "Actual Expenditure Communication Cost", "actualCommunicationCost", "string").save();
		  new FormResource(form, "Actual Expenditure Total", "actualTotalCost", "string").save();*/
		  
		List<FlatData> flatDatas = new ArrayList<FlatData>();
		
		for(int i=0; i<8; i++){
			if(webEntryDatas[i]!=null){
				FormResource dataInfo = FormResource.find("byNodePathAndform",
						nodePath[i], form).first();
				if (dataInfo != null) {
					FlatData flatData = new FlatData();
					flatData.titleVar = "data/"+dataInfo.nodePath;
					flatData.title = dataInfo.title;
					flatData.type = dataInfo.type;
					flatData.value = webEntryDatas[i];
					flatData.valueVar = webEntryDatas[i];
					flatData.formResource = dataInfo;
					flatDatas.add(flatData);
				}
			}
		}
		
		FlatDataGenerator.generateUpdatedFlatForWeb(data, flatDatas);
		EditData.viewFromFlat(data.id);
	}
}
