package controllers;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.SchoolInformation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import play.Logger;
import play.db.DB;
import play.mvc.With;
import utils.SchoolComponets;
import utils.SchoolWashComparetor;
import utils.WashItemsSumCalculation;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;

@With(Deadbolt.class)
public class SchoolStatus extends Controller{
	
	@ExternalRestrictions("Management Response")
	public static void washStatus(){
		int headSelector = 7;
		Map<Integer,String> xmlQuestions = new HashMap<Integer, String>();
		/*xmlQuestions.put(1, "গত এক মাসের স্কুলের সার্বিক খাবার পানির ব্যবস্থা কেমন ছিল? ");
		xmlQuestions.put(2, "গত এক মাসের স্কুলের সার্বিক পয়:নিষ্কাশন ব্যবস্থা কেমন ছিল?");
		xmlQuestions.put(3, "গত এক মাসের স্কুলের সার্বিক হাইজিন বা পরিস্কার পরিচ্ছন্নতা কেমন ছিল?");*/
		xmlQuestions.put(1, "Water ");
		xmlQuestions.put(2, "Sanitation");
		xmlQuestions.put(3, "Hygienic");
		render(headSelector,xmlQuestions);
	}
	@ExternalRestrictions("Management Response")
	public static void searchWashStatus(){
		String ajaxWashQuestion = request.params.get("wash");
		String ajaxGender = request.params.get("gender");
		String ajaxStartDate = request.params.get("startDate") + " 00:00:00";
		String ajaxEndDate = request.params.get("endDate")+ " 11:59:59";
		Logger.info("A- " + ajaxWashQuestion + " B- " + ajaxGender + " C- " + ajaxStartDate + " D- " + ajaxEndDate);
		String dataIdQuery = "";
		if(!ajaxGender.equals("") && !ajaxGender.equals("0") && !ajaxStartDate.equals(" 00:00:00") && !ajaxEndDate.equals(" 11:59:59")){
		 dataIdQuery = "SELECT D.schools_id,GROUP_CONCAT(D.id SEPARATOR ',') as ids "+  
							" FROM crgs.Data as D ,crgs.tblFlat as F where D.id = F.data_id "+ 
							"and F.res_type__1 = '"+ajaxGender+"' and F.received between '"+ajaxStartDate+"' and '"+ajaxEndDate+"' group by D.schools_id;";
	    }
		if(!ajaxGender.equals("") && !ajaxGender.equals("0") && ajaxStartDate.equals(" 00:00:00") && ajaxEndDate.equals(" 11:59:59")){
			 dataIdQuery = "SELECT D.schools_id,GROUP_CONCAT(D.id SEPARATOR ',') as ids "+  
								" FROM crgs.Data as D ,crgs.tblFlat as F where D.id = F.data_id "+ 
								"and F.res_type__1 = '"+ajaxGender+"' group by D.schools_id;";
		}
		if(!ajaxGender.equals("") && ajaxGender.equals("0") && !ajaxStartDate.equals(" 00:00:00") && !ajaxEndDate.equals(" 11:59:59")){
			 dataIdQuery = "SELECT D.schools_id ,GROUP_CONCAT(D.id SEPARATOR ',') as ids "+  
								" FROM crgs.Data as D ,crgs.tblFlat as F where D.id = F.data_id "+ 
								"and F.received between '"+ajaxStartDate+"' and '"+ajaxEndDate+"' group by D.schools_id;";
		}
		if(!ajaxGender.equals("") && ajaxGender.equals("0") && ajaxStartDate.equals(" 00:00:00") && ajaxEndDate.equals(" 11:59:59")){
			 dataIdQuery = "SELECT D.schools_id,GROUP_CONCAT(D.id SEPARATOR ',') as ids "+  
								" FROM crgs.Data as D ,crgs.tblFlat as F where D.id = F.data_id "+ 
								" group by D.schools_id;";
		}
		List<WashItemsSumCalculation> washDataList = new ArrayList<WashItemsSumCalculation>();
		List<WashItemsSumCalculation> washSumList = new ArrayList<WashItemsSumCalculation>();
		ResultSet results = DB.executeQuery(dataIdQuery);
		try{
			while(results.next()){
				WashItemsSumCalculation item = new WashItemsSumCalculation();
				item.school = results.getString("schools_id");
				item.datas = results.getString("ids");
				washDataList.add(item);
			}
		}catch(Exception e){}
		String column = "";
		String heading = "";
		if(ajaxWashQuestion.equals("1")){
			column = "rank_water__1";
			heading = "Water";
		}
		if(ajaxWashQuestion.equals("2")){
			column = "rank_sanitation__1";
			heading = "Sanitation";
		}
		if(ajaxWashQuestion.equals("3")){
			column = "rank_hygiene__1";
			heading = "Hygiene";
		}
			for(WashItemsSumCalculation w : washDataList ){
				String query = "SELECT sum(" + column + ") as " + column +" from crgs.tblFlat where data_id in ("+w.datas+")";
				//Logger.info("Query " + query);
				WashItemsSumCalculation item = new WashItemsSumCalculation();
				ResultSet rs = DB.executeQuery(query);
				String itemsSum = "";
				try{
					while(rs.next()){
						itemsSum = rs.getString(column);
					}
				}catch(Exception e){}
				item.school = w.school;
				item.datas = w.datas;
				String [] dSize = w.datas.split(",");
				DecimalFormat df = new DecimalFormat("#.##"); 
				item.itemsum = String.valueOf( df.format((Double.valueOf(itemsSum)/dSize.length)));
				washSumList.add(item);
			}
		
		List<SchoolComponets> arraylist = new ArrayList<SchoolComponets>();
		for(WashItemsSumCalculation w :washSumList){
			SchoolInformation school = SchoolInformation.findById(Long.valueOf(w.school));
			arraylist.add(new SchoolComponets(school.name,w.itemsum));
		}
		Collections.sort(arraylist,new SchoolWashComparetor());
		JsonArray json_dataList =new JsonArray();
		
		
		for(SchoolComponets s : arraylist ){
			JsonObject inactiveToiletReasonJson = new JsonObject();
			inactiveToiletReasonJson.addProperty("x_data", s.name);
			inactiveToiletReasonJson.addProperty("y_data", s.avg.toString());
			json_dataList.add(inactiveToiletReasonJson);
			//inactiveToiletReasonJson = new JsonObject();
		}
		//Logger.info("JSON " +json_dataList );
		render(heading,json_dataList);
	}
}
