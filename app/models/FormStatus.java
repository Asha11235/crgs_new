package models;

import play.*;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class FormStatus extends Model {

	@Required
	public Integer cId;

	@Required
	public String name;

    @ManyToOne
    public Form form;

    public FormStatus(Integer code) {
    	this.cId = code;
    }

    public FormStatus(Integer code, String name) {
    	this(code);
    	this.name = name;
    }

    public static FormStatus findByCode(Integer code, Form form) {
    	return FormStatus.find("byCIdAndForm", code, form).first();
    }

    @Override
    public String toString() {
		return String.format("%02d", this.cId) + " - " + this.name;
    }

}
