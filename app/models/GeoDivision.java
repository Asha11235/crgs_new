package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class GeoDivision extends Model {
	
	@Required
	public String name;
	
	@Required
	//@Length(min=1, max=2)
//	@Match("[0-9]")
	public String code;
	
	public static GeoDivision findByDivisionCode(String divCode) {
		return GeoDivision.find("byCode", divCode).first();
	}
}
