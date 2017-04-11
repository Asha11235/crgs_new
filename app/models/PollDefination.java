package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
		qString =   " SELECT PollDefination.title , PollDefination.questionType , "+
					" PollDefination.startDate,PollDefination.endDate , "+
					" PollDefination.id as poll_id , "+
					" PollQuestionOption.options "+
					" FROM PollDefination "+
					" JOIN PollQuestionOption ON PollDefination.id = PollQuestionOption.poll_id "+
					" WHERE PollDefination.status=1 "+
					" ORDER BY NewCrgs.PollDefination.created_at ASC ";
		
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
				
				msg = msg + ";" + title + ";" + questiontype +  ";" + startdate + ";" + enddate + ";" + options + ";" + pollId + ":" ;

				Logger.info("endDate1: " +enddate);//count++;
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
			Logger.info("endDate2: " +enddate);
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


	public static String getPollGraphData() throws SQLException {

		Logger.info("hit3");
		String qString = null;
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		ResultSet rs1=null;
		String msg="";

        qString = " SELECT PollDefination.id , " +
				" PollDefination.title ,\n" +
				" PollDefination.startDate, \n" +
				" PollDefination.endDate,\n" +
				" DATEDIFF (\n" +
				"        str_to_date(PollDefination.endDate, '%d/%m/%Y'),\n" +
				"        str_to_date(PollDefination.startDate, '%d/%m/%Y')\n" +
				"    ) AS datedifference , \n" +
				" PollQuestionOption.options,\n" +
				" PollVoteReply.answer\n" +
				" FROM PollDefination\n" +
				" JOIN PollQuestionOption ON PollQuestionOption.poll_id= PollDefination.id\n" +
				" JOIN PollVoteReply ON PollVoteReply.poll_id= PollDefination.id\n" +
				" WHERE PollDefination.status=0";


			PreparedStatement queryForExecution = conn.prepareStatement(qString);
			//Logger.info("qString: "+qString);
			rs = queryForExecution.executeQuery();



			String title=null;
			String answer=null;
			String startdate=null;
			String enddate=null;
			String options=null;
			String pollId=null;
			String totaldays=null;
			String answerammount=null;


			while (rs.next()) {

				title = rs.getString("title");
				totaldays = rs.getString("datedifference");
				startdate = rs.getString("startDate");
				enddate = rs.getString("endDate");
				options = rs.getString("options");
				answer = rs.getString("answer");
				pollId = rs.getString("id");

				msg = msg + ";" + title + ";" + totaldays +  ";" + startdate + ";" + enddate + ";" + options + " ; " + pollId ;

			}

				String[] answerList = answer.split(",");

				answer = answer.substring(1, answer.length()-1);
				answer=answer.trim();

				//Logger.info(answer);
				String[] optionList = answer.split(",");

				//Logger.info("optionList: "+optionList.length);

		        String answerammount2="";
				for (int i = 0; i < optionList.length; i++) {
					String var1 = optionList[i].substring(1, optionList[i].length() - 1);
					String var = var1.trim();
					//Logger.info(var);

					String qString2 = "SELECT count(PollVoteReply.answer) as countinganswer" +
							" FROM PollVoteReply " +
							" WHERE PollVoteReply.answer LIKE ?";

					//Logger.info("qString2" + qString2);
					PreparedStatement queryForExecution2 = conn.prepareStatement(qString2);

					queryForExecution2.setString(1, "%"+var+"%");

					ResultSet rs3 = queryForExecution2.executeQuery();

					while (rs3.next()) {

						answerammount = rs3.getString("countinganswer");
						answerammount2 = answerammount2 + "," +var + ":" + answerammount ;


					}


				}

		           msg = msg  + ";" + answerammount2;

				String	qString3="SELECT count(PollVoteReply.answer) as counting " +
							" FROM PollVoteReply " +
							" JOIN PollDefination ON PollVoteReply.poll_id= PollDefination.id "+
							" WHERE PollDefination.id  =" + pollId;

					PreparedStatement queryForExecution3 = conn.prepareStatement(qString3);
					ResultSet rs2 = queryForExecution3.executeQuery();

					while (rs2.next()) {

						answerammount = rs2.getString("counting");
						String answerammount3 = "Total Vote: " + answerammount;
						msg = msg + ";" + answerammount3;

					}

					//Logger.info(msg);

		return msg;
	}
	
}
