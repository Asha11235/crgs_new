package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Event extends Model {

	@Required
	public String name;

	public Event(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
