package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;
@Entity
public class FormDefination extends Model{
	
	@ManyToOne
	public Form form;
	
	public String titleVar;
	public String title;
	public String type;
	public String valueVar;
	public String value;

}
