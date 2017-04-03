package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class PollDefination extends Model{
	
	@Required
	@Column( columnDefinition="TEXT")
	public String title;
	
	public String startDate;
	
	public String endDate;
	
	@Required
	public String questionType;
	
	@Required
	public String optionNumber;
	
	@ManyToOne
	public User creater;
	
	public Date createDate;
	
	@ManyToOne
	public User updatedby; 

	public Date updatedDate;
	
	public String status = "0";
	
	public String resultStatus;

	@Override
	public String toString() {
		return "PollDefination [title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", questionType=" + questionType + ", optionNumber=" + optionNumber + ", creater=" + creater
				+ ", createDate=" + createDate + ", updatedby=" + updatedby + ", updatedDate=" + updatedDate
				+ ", status=" + status + ", resultStatus=" + resultStatus + "]";
	}
	
	
	public static String getPollReport() throws SQLException {
		
		String qString = null;
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		int flag=0;
		qString = " SELECT PollDefination.title , PollDefination.questionType , "+
				" PollDefination.startDate,PollDefination.endDate , "+
				" PollDefination.id as poll_id , "+
				" PollQuestionOption.options "+
				" FROM PollDefination "+
				" JOIN PollQuestionOption ON PollDefination.id = PollQuestionOption.poll_id "+
				" WHERE PollDefination.status=1";
		
		PreparedStatement queryForExecution = conn.prepareStatement(qString);
		rs = queryForExecution.executeQuery();
		
		String title=null;
		String questiontype=null;
		String startdate=null;
		String enddate=null;
		String options=null;
		String pollId=null;
		String msg="";
		
		String npub="not publish";
		
		try {

			while (rs.next()) {

				title = rs.getString("title");
				questiontype = rs.getString("questionType");
				startdate = rs.getString("startDate");
				enddate = rs.getString("endDate") ;
				options = rs.getString("options") ;
				pollId=rs.getString("poll_id");
				
				msg = msg + ";" + title + ";" + questiontype +  ";" + startdate + ";" + enddate + ";" + options + ";" + pollId ;
				//count++;
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		
		
		try {
/*			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			String todayDate=dateFormat.format(date);
			Date lastDate=new SimpleDateFormat("dd/MM/yyyy").parse(enddate);
			String endDate = lastDate.toString();*/
			
			 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        Date todayDate = new Date();
		        Date lastDate = sdf.parse(enddate);
			
			if (lastDate.compareTo(todayDate) > 0) {
	            Logger.info("endDate is after todayDate");
	            
	            flag=0;
	            
	        } 
			
			else{
				
				 Logger.info("can not publish");
				 
				flag=1;
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    if(flag==0){
		return msg;
	    }
	    else{
	    	return npub;
	    }
	}
	
}
