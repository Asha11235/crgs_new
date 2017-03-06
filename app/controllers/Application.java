package controllers;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.dom.DOMSource;

import models.Data;
import models.Form;
import models.SchoolInformation;
import models.User;
import models.Water;
import play.Logger;
import play.mvc.With;
import utils.Approval;
import utils.ComparisonList;
import utils.FlatDataGenerator;
import utils.XMLBuilder;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import play.data.validation.Valid;
import play.db.jpa.*;
import java.util.Date;


@With(Deadbolt.class)
public class Application extends Controller {
	
	public static Approval approval = new Approval();
	public static ComparisonList comparison = new ComparisonList();

	@ExternalRestrictions("Dashboard")
	public static void index() {
		/*
		 * Master Updated with Development branch Code
		 * This master Updated by Shakil Ahammad .
		 * 
		 */
		
		User webUser = User.findByName(session.get("username"));
		
		if(webUser.role.id == 1){
			
			List<Form> form = Form.findAll();
			if(form != null){
				//Forms.list();
				Logger.info("going to launch forms.landingPage");
				Forms.landingPage();
			}
			else
				Forms.create();
		}
		//if school principle's assigned school is empty then logout
        SchoolInformation school = webUser.school;
        if(school == null){
            try {
            	String logoutMessage = "You have not been assigned any school";
                //Secure.logoutWithoutAssignedSchool(logoutMessage);
            } catch (Throwable e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
			List<Form> form = Form.findAll();
			if(form != null){
				//Forms.list();
				Logger.info("going to call forms.landingPage");
				Forms.landingPage();
			}else
				Forms.create();
        }
    }	
	

	public static void getNotificationTest(String id) {
		Logger.info("I am  here" + id);
		renderJSON("abc");
	}
	
	public static void parse(){
		List<Data> datas = Data.findAll();
		for(Data data : datas){
			FlatDataGenerator.generateFlat(data);
		}
	}
	/*
	 * 	For Redirected the Forms URL From Left Manu   
	 */
	@ExternalRestrictions("Forms")
	public static void forms(){
		render();
	}
	@ExternalRestrictions("Water Form")
	public static void waterForm(){
		render();
	}

	public static void submitWaterForm(@Valid Water water){
		//Map<String,String[]> allValue =  request.params.all();
		Logger.info("submitWaterForm");
		if(validation.hasErrors()){
			validation.keep();
			waterForm();
		}
		
		User sender = User.findByName(session.get("username"));
		String [] res_type = request.params.getAll("res_type");
		String [] water_source = request.params.getAll("water_source");
		String [] num_waterSource = request.params.getAll("num_waterSource");
		
		String [] activeWaterSource = request.params.getAll("activeWaterSource");
		
		String [] is_potable = request.params.getAll("is_potable");
		String [] why_not_potable = request.params.getAll("why_not_potable");
		String [] why_not_potable_other = request.params.getAll("why_not_potable_other");
		String [] is_tank_cleaned = request.params.getAll("is_tank_cleaned");
		String [] is_informed_authority_water_prob = request.params.getAll("is_informed_authority_water_prob");
		String [] how_informed_water_prob = request.params.getAll("how_informed_water_prob");
		String [] how_informed_water_prob_other = request.params.getAll("how_informed_water_prob_other");
		String [] water_prob_solved_authority = request.params.getAll("water_prob_solved_authority");
		String [] rank_water = request.params.getAll("rank_water");
		XMLBuilder.XmlFile(sender,"2", res_type, water_source, num_waterSource, activeWaterSource, is_potable, why_not_potable, why_not_potable_other, is_tank_cleaned, is_informed_authority_water_prob, how_informed_water_prob, how_informed_water_prob_other, water_prob_solved_authority, rank_water);
		
		Logger.info("Values " + res_type.length+"**" + res_type[0]);
		index();
		
		
	}
	


	/* 
	* Submission of Water form from Web Portal
	* 
	*/
	@ExternalRestrictions("Water Form")
	public static void submitionWaterForm(@Valid models.Water water){
		
		//Map<String,String[]> allValue =  request.params.all();
		try{
			if(validation.hasErrors()){
				validation.keep();
				waterForm();
			}
			Logger.info("water form submission");
			Form form=Form.findById((long)1);
			User sender = User.findByLogin(session.get("username"));
			models.Data data=new models.Data(form,null);
			data.sender = sender;
			data.senderRole = data.sender.role;
			//data.resolveDate = new Date();
			data.schools =  sender.school;
			data.ngo = sender.ngo;
			data.webOrMob = "web";
			data = data.save();
			
			water.data=data;
			water.form=form;
			water.sender=sender;
			water.school=sender.school;
			water.date=new Date();
			water.save();
			flash.success("Recored saved successfully");
			
			//render("@waterForm");
			index();
		}
		catch(Exception e)
		{
			flash.success("Recoed Submission Failed. Please Try again");
			JPA.setRollbackOnly();
			render("@waterForm",water);
		}
	}

	
	@ExternalRestrictions("Sanitation Form")
	public static void sanitationForm(){
		render();
	}
	
	/* Sanitation Form Submit
	*/
	@ExternalRestrictions("Sanitation Form")
	public static void submitionSanitationForm(models.Sanitation sanitation){
		//Map<String,String[]> allValue =  request.params.all();
		try{
			Form form=Form.findById((long)2);
			User sender = User.findByLogin(session.get("username"));
			models.Data data=new models.Data(form,null);
			data.sender = sender;
			data.senderRole = data.sender.role; 
			data.schools =  sender.school;
			data.ngo = sender.ngo;
			data.webOrMob = "web";
			data = data.save();
			sanitation.data=data;
			sanitation.form=form;
			sanitation.sender=sender;
			sanitation.school=sender.school;
			sanitation.date=new Date();

			sanitation.save();
			flash.success("Recoed saved successfully");
			//render("@sanitationForm");
			index();
		}
		catch(Exception e)
		{
			flash.success("Recoed Submission Failed. Please Try again");
			JPA.setRollbackOnly();
			render("@sanitationForm",sanitation);
		}
	}

	@ExternalRestrictions("School Environment Form")
	public static void schoolEnvironment(){
		render();
	}
	
	/* School Environment Form Submit
	*/
	public static void submitionSchoolEnvironment(models.SchoolEnvironment environment){
		try{
			Form form=Form.findById((long)3);
			User sender = User.findByLogin(session.get("username"));
			models.Data data=new models.Data(form,null);
			data.sender = sender;
			data.senderRole = data.sender.role;
			data.schools =  sender.school;
			data.webOrMob = "web";
			data.ngo = sender.ngo;
			data = data.save();
			environment.data=data;
			environment.form=form;
			environment.sender=sender;
			environment.date=new Date();
			environment.school=sender.school;
			environment.save();
			flash.success("Recoed saved successfully");
			//render("@schoolEnvironment");
			index();
		}
		catch(Exception e)
		{
			flash.success("Recoed Submission Failed. Please Try again");
			JPA.setRollbackOnly();
			render("@schoolEnvironment",environment);
		}
	}
	
	@ExternalRestrictions("Sports And Recreation Form")
	public static void sportsRecreation(){
		render();
	}

	/*Sports And Recreation Form Form Submit
	*/
	@ExternalRestrictions("Sports And Recreation Form")
	public static void submitionSportsRecreation(models.SportsRecreation recreation){
		//Map<String,String[]> allValue =  request.params.all();
		try{
			Form form=Form.findById((long)4);
			User sender = User.findByLogin(session.get("username"));
			models.Data data=new models.Data(form,null);
			data.sender = sender;
			data.senderRole = data.sender.role;
			data.schools =  sender.school;
			data.ngo = sender.ngo;
			data.webOrMob = "web";
			data = data.save();
			recreation.data=data;
			recreation.date=new Date();
			recreation.form=form;
			recreation.sender=sender;
			recreation.school=sender.school;
			recreation.save();
			flash.success("Recoed saved successfully");
			//render("@sportsRecreation");
			index();
		}
		catch(Exception e)
		{
			flash.success("Recoed Submission Failed. Please Try again");
			JPA.setRollbackOnly();
			render("@sportsRecreation",recreation);
		}
	}

}
