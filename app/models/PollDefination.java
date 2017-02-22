package models;

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

}
