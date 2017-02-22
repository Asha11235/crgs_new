package models;


import javax.persistence.ManyToOne;

import play.db.jpa.Model;

public class Issue extends Model {
	
	public String title;
	public String titleVar;
	public String ans;
	public String createDate;
	public String resolvedDate;
	
	@ManyToOne
	public Data data;
	
	@ManyToOne
	public SchoolInformation schools;
	
	public Boolean isResolved;
	
	public Issue (){
		
	}

}
