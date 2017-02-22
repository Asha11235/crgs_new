package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class PollQuestionOption extends Model {
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	public PollDefination poll;
	
	public String options;

}
