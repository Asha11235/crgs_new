package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.Query;

import models.AnswerOptionOfQuestion;
import models.DashboardReport;
import models.Data;
import models.Event;
import models.Form;
import models.FormDefination;
import models.FormStatus;
import models.GeoDivision;
import models.QuestionType;
import models.Role;
import models.Sanitation;
import models.SchoolEnvironment;
import models.SportsRecreation;
import models.UnitData;
import models.User;





import models.Water;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

import play.Logger;
import play.Play;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.db.jpa.JPA;
import play.mvc.Util;
import play.mvc.With;
import utils.MakeUnitDataList;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;


/**
 * The Class Forms.
 */
@With(Deadbolt.class)
public class Forms extends Controller {
	
	public static void landingPage(){
		Logger.info("Forms.landing page");
		render();
	}
	
	public static String getDashBoardData() throws SQLException {
		Map<String, Long> mp = new HashMap<String, Long>();
		//mp.put("json", 55L);
		mp = DashboardReport.getDashboardData();
		Gson gson= new Gson();
		return gson.toJson(mp);
	}

	/**
     * List all Form.
     */
    @ExternalRestrictions("View Form")
    public static void list() {
        List<Form> forms = Form.findAll();
        render(forms);
    }

    /**
     * List data.
     *
     * @param id the form id
     */
    @ExternalRestrictions("View Data")
    public static void listData(String id,Long page) {
		int currentPage = 1;
		int recordsPerPage = 20;
		int totalPages = 0;
		Long Id = 0L;
	
		if (request.params.get("page") != null) {
			currentPage = Integer.parseInt(request.params.get("page"));		
		}
		
		if(request.params.get("page").equals(0)){
			currentPage = 1;
		}
		
		if(request.params.get("id").equals("Water")){
			Id = 1L;
		}
		
		else if(request.params.get("id").equals("Sanitation")){
			Id = 2L;
		}
		
		else if(request.params.get("id").equals("Environment")){
			Id = 3L;
		}
		
		else if(request.params.get("id").equals("Sports")){
			Id = 4L;
		}
		
		else{
		
		Id = Long.parseLong(id);
		}
		Query query = null;
		
		Form form = Form.findById(Id);
		notFoundIfNull(form);
		String countRow = "select count(*) from Data where form_id = " + Id ;
		List<Data> data = Data.find("form = ? and webOrMob != ? order by id desc", form,"null").fetch(currentPage,recordsPerPage);
		query = JPA.em().createQuery(countRow);
		int rowNumber = ( (Number)query.getSingleResult()).intValue();
		
		totalPages = (int) Math.ceil(rowNumber * 1.0 / recordsPerPage);
    	render(form, data,rowNumber,currentPage,totalPages);
    }

    /**
     * View data.
     *
     * @param id the data id
     */
    @ExternalRestrictions("View Data")
    public static void viewData(Long id) {
    	Data data = Data.findById(id);
    	List<UnitData> list = new ArrayList<UnitData>();
    	String enableAudioText = Play.configuration.getProperty("aggregate.enableAudioText");
    	
    	//For Web Submitted Data View 
    	if(data.webOrMob.equalsIgnoreCase("web")){
    		List<FormDefination> fromDef = FormDefination.find("form = ? ", data.form).fetch();
    		
    		if(data.form.id == 1L){
    			Water water = Water.find("data = ?", data).first();
    			Logger.info("Title " );
    			list = MakeUnitDataList.unitDataForWaterForm(water, fromDef);
    		}
    		if(data.form.id == 2L){
    			Sanitation sanitation = Sanitation.find("data = ?", data).first();
    			list = MakeUnitDataList.unitDataForSanitationForm(sanitation,fromDef);
    			
    		}
    		if(data.form.id == 3L){
    			SchoolEnvironment schoolEnvironment = SchoolEnvironment.find("data = ?", data).first();
    			list = MakeUnitDataList.unitDataForEnvironmentForm(schoolEnvironment,fromDef);
    		}
    		if(data.form.id == 4L){
    			SportsRecreation sportsRecreation = SportsRecreation.find("data = ?", data).first();
			list = MakeUnitDataList.unitDataForSportsForm(sportsRecreation,fromDef);
    		}
    		render(data, list, enableAudioText);
    	}
    	else{
		    	notFoundIfNull(data);
		    	
		    	List<UnitData> duplicateTitleList = new ArrayList<UnitData>();
		    	List<UnitData> unitList = UnitData.find("data=?", data).fetch();
		    	
		    	List<String> titleVarS = new ArrayList<String>();
		    	UnitData tmpUnitdata = null;
		    	UnitData tmp = null;
		    	Boolean selectFlag = false;
		    	Boolean select = true;
		    	HashMap<Integer , UnitData> mapUnit = new HashMap<Integer, UnitData>();
		    	
		    	for(int s = 0; s<unitList.size(); s++){
		    		duplicateTitleList.add(unitList.get(s));
		    		if(unitList.get(s).type.equals("select") && s != 0){
		    			if(unitList.get(s-1).title != null && !unitList.get(s-1).title.equals("") && unitList.get(s).title != null){
		    				if(unitList.get(s).title.equals(unitList.get(s-1).title)){
		        				duplicateTitleList.remove(duplicateTitleList.size()-2);
		        			}
		    			}
		    		}
		    	}
				for(UnitData u : duplicateTitleList){
		    	 if(!titleVarS.contains(u.titleVar) && !titleVarS.contains("data/"+u.titleVar)){
		    		 titleVarS.add(u.titleVar);
		    		 list.add(u);
		    	 }
		     }
			render(data, list, enableAudioText);
    	}
    }
	
    @ExternalRestrictions("View Form")
    public static void details(Long id) {
        Form form = Form.findById(id);
        render(form);
    }

    /**
     * Creates new Form.
     */
    @ExternalRestrictions("Edit Form")
    public static void create() {
    	render("@edit");
    }

    /**
     * Edit Form.
     *
     * @param id
     *            the id
     */
	@ExternalRestrictions("Edit Form")
    public static void edit(Long id) {
    	Form form = Form.findById(id);
        render(form);
    }

    /**
     * Submit new Form.
     *
     * @param form
     *            the form
     */
    @ExternalRestrictions("Edit Form")
    public static void submit(@Valid Form form) {
        if(Validation.hasErrors()) {
        	render("@edit", form);
        }
        form.save();
        flash.success("Record saved successfully.");
		list();
    }

    @ExternalRestrictions("Edit Form")
    public static String delete(String formid) {
    	 Long id = Long.valueOf(formid);
		 Boolean code = false;
		 String msg ="";
		 
		 if(request.isAjax()) {
			 //ConditionOfOutcome condition = ConditionOfOutcome.findById(id);
			 Form form=Form.findById(id);
			 String name=form.name;
			 if(form.dataCount==0){
				 form.delete();
				 code = true;
				 msg =  name +" deleted successfully";
			 }
			 else
			 {
				 msg = "This Form '"+ name +"' is currently being used/ have some data in system. So you can not delete this without deleting it's associate data";
		        	
			 }
		}
		 if(code){
	            response.status=200;
	            return msg;
	            
	      }
	      else {
	            
	            response.status=303;
	            return msg;
	      }
    }

    

    
    //public static void addLogic(Long id, @Valid Logic logic) {
   


    /*
     * Form Statuses
     */
    @ExternalRestrictions("View Form")
    public static void statuses(Long id) {
    	Form form = Form.findById(id);
        render(form);
    }

    @ExternalRestrictions("Edit Form")
    public static void addStatus(Long id, @Valid FormStatus formStatus) {
    	Form form = Form.findById(id);
        if(Validation.hasErrors()) {
        	render("@statuses", form, formStatus);
        }

    	form.addStatus(formStatus);
        statuses(id);
    }

    
    /*
     * Form outcomes
     */
  
    
    @ExternalRestrictions("Edit Form")
    public static void exportCSV(Long id) {
    	
    	
    	Logger.info("Export Data:" + id);
    	Form form = Form.findById(id);
    	notFoundIfNull(form);
    	response.setHeader("Content-Disposition", "attachment; filename=\"" + form.name + ".csv\"");	
    	List<UnitData> unitDataList = UnitData.find("form = ?", form).fetch();
    	
    	// CSV title set - used LinkedHashSet for which title sequence remain same as insertion order
    	Set<String> titles = new LinkedHashSet<String>();
    	// All CSV row storage - mapped each row with interview id
    	HashMap<Long, List<String>> filteredData = new HashMap<Long, List<String>>();
    	
    	//set of titles 
    	for(UnitData unitData : unitDataList){
    		titles.add(unitData.titleVar);
    	}    	
    	
    	//temporary store each CSV row data
    	List<String> row = new ArrayList<String>(titles);
    	
    	for(UnitData unit : unitDataList){
    		if(filteredData.containsKey(unit.data.id)){
    			row = filteredData.get(unit.data.id);
    		}
    		else{
    			row = new ArrayList<String>(titles);
    		}
 
    		if(row.contains(unit.titleVar)){
    			String columnValue = null;
    			if(unit.valueVar != null) {
    				columnValue = unit.valueVar;
    				//solution for unwanted new line and comma in value  
    				columnValue = columnValue.replaceAll("\n", "").replace(",", " ");  	
    			}
    			row.set( row.indexOf(unit.titleVar), columnValue);
    			//row.set( row.indexOf(unit.title), unit.value );
    			//row.set( row.indexOf(unit.titleVar), unit.titleVar);
    		}

    		filteredData.put(unit.data.id, row);
    	}
    	
    	String csvTitle = "id,received,interviewer,"+StringUtils.join(titles,",");
    	String output = "";
    	for(Entry<Long, List<String>> item : filteredData.entrySet()) {
    		Data data = Data.findById(item.getKey());
    		output += data.id + "," + data.received.toString() + "," + data.sender.name + ",";
    		List<String> rowValues = item.getValue();
    		
    		//assuming that no CSV column contains any value equals to titles - set blank for the titles which has no value  
    		for(String title : titles){
    			if(rowValues.contains(title)){
    				rowValues.set(rowValues.indexOf(title), "");
    			}
    		}
    		output += StringUtils.join(rowValues,",")+ "\n";
    	}
    	
    	csvTitle = getCustomizedTitle(form, csvTitle);
    	
    	output = csvTitle + "\n" + output;
    	
    	renderText(output);
    }
    
    /**
     * @param form - the form for which CSV will be generated
     * @param csvTitle - the  CSV title generated from UnitData 
     * @return customized CSV title
     * */
    @Util
    private static String getCustomizedTitle(Form form, String csvTitle) {
    	List<String> customizedTitleList = new ArrayList<String>();
    	String[] titles = csvTitle.split("\\,");
		
		Map<String, String> oldNewTitleMap = new HashMap<String, String>();
		
		String fileName = Play.applicationPath.getAbsolutePath() + File.separator + Play.configuration.getProperty("mcare.csv.titles") + File.separator + form.shortName + ".txt"; 		
		File file = new File(fileName);
		
		//parse and create old title and new title Map from text file
		try {
			Scanner fileScanner = new Scanner(file);
			while(fileScanner.hasNext()) {
				String line = fileScanner.next();
				
				if(line.startsWith("#")) {
					continue;
				}
				else if(!line.contains("=")) {
					break;
				}
				
				String oldTitle = line.split("=")[0].trim();
				String newTitle = line.split("=")[1].trim();
				oldNewTitleMap.put(oldTitle, newTitle);
			}
		} catch (FileNotFoundException e) {
			Logger.warn(fileName + " file missing");
		}
		
		if(!oldNewTitleMap.isEmpty()) {
			for(String title : titles) {
				String customizedTitle = oldNewTitleMap.get(title);
				if(customizedTitle == null) {
					customizedTitle = title;
				}
				customizedTitleList.add(customizedTitle);
			}
			csvTitle = StringUtils.join(customizedTitleList, ",");
		}
		
    	return csvTitle;
	}

	// TODO - Check with form for better security
    @ExternalRestrictions("Edit Form")
    public static void deleteStatus(Long id) {
    	if(request.isAjax()) {
    		notFoundIfNull(id, "id not provided");
    		FormStatus formStatus = FormStatus.findById(id);
	    	notFoundIfNull(formStatus, "form status not found");
	    	formStatus.delete();
			ok();
    	}
    }
    
    public static  void testmul(List<User> users) {
    	/*for(User u  : users)
    	System.out.println(u);*/
    	
    	String fileName = Play.applicationPath.getAbsolutePath() + File.separator + Play.configuration.getProperty("mcare.csv.titles") + File.separator + "PSF" + ".txt"; 		
		File file = new File(fileName);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + "abc" + ".csv\"");
    	renderBinary(file);
    }

}
