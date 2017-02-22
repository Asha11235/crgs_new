package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class TrainingInformation extends Model{
	public String trainingName;
	public String numberOfParticipants;
	public String numberOfMale;
	public String numberOfFeMale;
	@Required
	@ManyToOne	
	public Data data;
	@Required
	@ManyToOne
	public User sender;
	/** The received. */
	public Date received;
	
	@Required
	@Length(max=21, min=21)
	public  String trainingId;

	public static TrainingInformation findByTrainingId(String trainingId){		
		return TrainingInformation.find("byTrainingId", trainingId).first();
	}
	
}
