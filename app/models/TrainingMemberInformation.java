package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.hibernate.validator.constraints.Length;
import play.data.validation.Required;
import play.db.jpa.Model;
import java.util.Date;

@Entity
public class TrainingMemberInformation extends Model{
	public  String FPGTRR_PG_name;
	public  String FPGTRR_PG_serial;
	public  String FPGTRR_PG_farmerId;
	public  String FPGTRR_PG_farmerName;
	public  String FPGTRR_PG_father;
	public  String FPGTRR_PG_husband;
	public  String FPGTRR_PG_Gender;
	public  String FPGTRR_PG_mobile;
	public  String FPGTRR_Day1;
	public  String FPGTRR_Day2;
	public  String FPGTRR_Day3;
	public  Boolean FPGTRR_PG_farmar_activity_Status;
	public  String FPGTRR_PG_farmarStatus_Cause;
	public  Date FPGTRR_PG_farmarStatus_date;
	@Required
	@Length(max=21, min=21)
	public  String trainingId;
	@Required
	@ManyToOne
	public Data data;
}