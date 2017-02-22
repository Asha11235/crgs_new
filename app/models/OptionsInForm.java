package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class OptionsInForm extends Model {
	
	@Required
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	public Form form;
	
	@Required
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	public FormResource formResource;
	
	public String value;
	public String valueVar;
	
	public OptionsInForm(Form form, FormResource formResource, String value, String valueVar){
		
			this.form = form;
			this.formResource = formResource;
			this.value = value;
			this.valueVar = valueVar;
	}

}
