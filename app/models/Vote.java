package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Vote extends Model {
	
	@ManyToOne
	public PollDefination poll;
	
	@ManyToOne
	public PollQuestionOption opton;
	
	public Date date;
	
	public String browser;

}
