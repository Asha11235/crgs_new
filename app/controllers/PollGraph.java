package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.PollDefination;
import play.Logger;
import play.Play;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
public class PollGraph extends  Controller {
	
	
	public static void index(){
		
		List<PollDefination>poll = new ArrayList<PollDefination>();
		poll = PollDefination.findAll();

		
		render(poll);
	}
	
	
	public static String getPollData(){
		String pollId = request.params.get("pollId");
		String query = "";
		query = "SELECT t.*,vwPollQuesOptions.title,vwPollQuesOptions.options FROM vwPollQuesOptions,("
				 +"SELECT poll_id,opton_id,count(*) vote_count FROM Vote group by poll_id,opton_id) as t where "
				+"t.poll_id=vwPollQuesOptions.pollId and " 
				+"t.opton_id=vwPollQuesOptions.optionId and t.poll_id='"+pollId+"';";
		Logger.info("query::"+query );
		JsonArray pollJsonArray =new JsonArray();
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()){
				JsonObject objJson = new JsonObject();
				objJson.addProperty("title", rs.getString("options"));
				objJson.addProperty("number", rs.getInt("vote_count"));
				pollJsonArray.add(objJson);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Logger.info("pollJsonArray::"+pollJsonArray.toString());
		return pollJsonArray.toString();
	}
}
