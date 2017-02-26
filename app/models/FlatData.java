package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;

public class FlatData {
	public String titleVar;
	public String title;
	public String type;
	public String valueVar;
	public String value;
	public int iterationNo;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	public FormResource formResource;
}
