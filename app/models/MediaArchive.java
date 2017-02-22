package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class MediaArchive extends Model {
	@Required
	public String type;
	@Required
	public String uploadDate;
	@Required
	public String entrySearchKey;
	@Required
	public String description;
	public String name;
	@ManyToOne
	public User uploadedBy;

}
