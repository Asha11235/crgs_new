package models;

import play.*;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class FormResource extends Model {

	@Required
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	public Form form;

	public String title;
	public String nodePath;
	public String type;

	public FormResource(Form form, String title, String nodepath, String type) {

		this.form = form;
		this.title = title;
		this.nodePath = nodepath;
		this.type = type;
	}

}
