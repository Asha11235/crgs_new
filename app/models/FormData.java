package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class FormData extends Model{

	@Required
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private FormResource formResource;
	
	@Required
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Data data;
	
	private String answer;
	
	private int iterationNo;
	
	public FormData(FormResource formResource, Data data, String answer, int iterationNo)
	{
		this.formResource = formResource;
		this.data = data;
		this.answer = answer;
		this.iterationNo = iterationNo;
	}
	
}
