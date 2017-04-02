package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
		
		qString = " SELECT PollDefination.title , PollDefination.questionType , "+
				" PollDefination.startDate,PollDefination.endDate , "+
				" PollQuestionOption.options "+
				" FROM PollDefination "+
				" JOIN PollQuestionOption ON PollDefination.id = PollQuestionOption.poll_id "+
				" WHERE PollDefination.status=1";
		
		return null;
	
	}
	
}
