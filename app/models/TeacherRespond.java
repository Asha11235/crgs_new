package models;







import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;
@Entity
public class TeacherRespond extends Model{
	
	@OneToOne
	public Data data;
	
	@ManyToOne
	public SchoolInformation school;
	
	
	@ManyToOne
	public User user;
	
	public Date date;
	
	public String senderGender;
	
	public String isTankCleaned;
	public String isTankCleanedBn;
	public String isTankCleanedAnother;
	
	public String whyToiletUnusable;
	public String whyToiletUnusableBn;
	public String whyToiletUnusableAnother;
	
	public String isTakeRegularInitiativeCleanToilet;
	public String isTakeRegularInitiativeCleanToiletBn;
	public String isTakeRegularInitiativeCleanToiletAnother;
	
	public String isToiletUsableDifferentlyAble;
	public String isToiletUsableDifferentlyAbleBn;
	public String isToiletUsableDifferentlyAbleAnother;
	
	public String mug;
	public String mugBn;
	public String mugAnother;
	
	public String soapHandwash;
	public String soapHandwashBn;
	public String soapHandwashAnother;
	
	public String menstrulFacility;
	public String menstrulFacilityBn;
	public String menstrulFacilityAnother;
	
	public String freqCleanToilet;
	public String freqCleanToiletBn;
	public String freqCleanToiletAnother;
	
	public String disscusionCleanClassroom;
	public String disscusionCleanClassroomBn;
	public String disscusionCleanClassroomAnother;
	
	public String isStuPresentMeeting;
	public String isStuPresentMeetingBn;
	public String isStuPresentMeetingAnother;
	
	
}
