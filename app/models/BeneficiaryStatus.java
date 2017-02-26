package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public abstract class BeneficiaryStatus extends Model {
	
	public Integer cId;
    public String name;
    
    
    public BeneficiaryStatus(Integer code) {
    	this.cId = code;
    	this.name = null;
    }
    
    public BeneficiaryStatus(Integer code, String name) {
    	this.cId = code;
    	this.name = name;
    }
    
    @Override
    public String toString() {
		return String.format("%02d", this.cId) + " - " + this.name;
    }
}