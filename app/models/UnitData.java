package models;

import play.*;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class UnitData extends Model {

	@Required
	@ManyToOne
	public Data data;

	@ManyToOne
	public Form form;

	@Required
	public String titleVar;
	public String title;
	public String type;
	public String valueVar;
	@Column(length=1023)
	public String value;
	public String extraValue;
	
	@Required
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	public FormResource formResource;
	

	@Min(0)
	public int iterationNo;

	public UnitData(Data data, Form form, String titleVar, String title, String type, String valueVar, String value, String extraValue, int itNo, FormResource formResource) {
		this.data = data;
		this.form = form;
		this.titleVar = titleVar;
		this.title = title;
		this.type = type;
		this.valueVar = valueVar;
		this.value = value;
		this.extraValue = extraValue;
		this.iterationNo = itNo;
		this.formResource = formResource;
	}

	public UnitData(Form form, String titleVar, String title, String type, String valueVar, String value, String extraValue) {
		this.form = form;
		this.titleVar = titleVar;
		this.title = title;
		this.type = type;
		this.valueVar = valueVar;
		this.value = value;
		this.extraValue = extraValue;
	}
	
	public UnitData(Data data, Form form, String titleVar, String title, String type, String valueVar, String value, String extraValue) {
		this.data = data;
		this.form = form;
		this.titleVar = titleVar;
		this.title = title;
		this.type = type;
		this.valueVar = valueVar;
		this.value = value;
		this.extraValue = extraValue;
	}

	public UnitData() {
		// TODO Auto-generated constructor stub
	}
}
