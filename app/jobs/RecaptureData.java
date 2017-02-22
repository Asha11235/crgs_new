package jobs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import models.UnitData;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.On;
import utils.FlatDataGenerator;


//@Every("5h")
@On("0 17 12 * * ?")
public class RecaptureData extends Job{

	 public void doJob() {
		   String dbName = Play.configuration.getProperty("ed.db.name");
	       Connection conn = play.db.DB.getConnection();
	   	   ResultSet rs = null;
	   	   Statement stmt = null;
		   List<models.Data> dataList = models.Data.findAll();
	       for(models.Data data : dataList){
	    	   int i = 0;
	    	   String query = "SELECT * FROM "+dbName +".tblFlat where data_id = " + String.valueOf(data.id);
	    	   try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
				rs.next();
				i = rs.getRow();
				/*if(i == 0){
					Logger.info("FlatData is missing --- > " + data.id);
					FlatDataGenerator.generateFlat(data);
				}*/
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	    	   UnitData unit = UnitData.find("data = ?", data).first();
	    	   if(unit == null){
	    		   Logger.info("Missing Data in UnitData -> " + data.id);
	    		   new ExtractData(data).now();
	    	   }
	    	   if(unit != null && i == 0){
	    		   Logger.info("FlatData is missing --- > " + data.id);
					FlatDataGenerator.generateFlat(data);
	    	   }
	       }
	       try {
			conn.close();
	       		} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
	       		}
	    }
}
