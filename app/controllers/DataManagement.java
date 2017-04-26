package controllers;
import java.util.ArrayList;
import java.util.List;

import models.*;
import org.osgi.framework.hooks.service.FindHook;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import play.Logger;
import play.data.validation.Valid;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.With;
import responses.LoginResponse;
import utils.DataField;
import utils.School;
import utils.UserUtils.UserRole;
import annotations.Mobile;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import controllers.deadbolt.Unrestricted;

//@With(Deadbolt.class)

public class DataManagement extends Controller{
	
	@ExternalRestrictions("Data Management")
	public static void schoolList() {
		
		/*List<SchoolInformation>schoolInfo = *///SchoolInformation.findAll();
		//Logger.info(schoolInfo.toString());//
		List<School> schoolInfoList = new ArrayList<School>();
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		String search_query = "";
		search_query = "select * from SchoolInformation";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(search_query);
			while(rs.next()){
				School schoolInfo = new School();
				schoolInfo.id = rs.getInt("id");
				schoolInfo.name = rs.getString("name");
				schoolInfo.address =rs.getString("address");
				//Logger.info(schoolInfo.toString());
				schoolInfoList.add(schoolInfo);	
			}
			//Logger.info(schoolInfo.address);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		render(schoolInfoList);
	}
	
	
	public static void viewDetails(Long id){
		
		/*User webUser = User.findByName(session.get("username"));
		Long user_id = webUser.id;
		//SchoolInformation schoolInfo = SchoolInformation.findById(id);
		Connection conn = play.db.DB.getConnection();
		ResultSet rs = null, rs1 = null, rs0 = null;
		Statement stmt = null;
		String search_query = "", search_query1 = "", user_query = "";
		user_query = "select User.id as user_id,User.displayName as senderName from User left join User_SchoolInformation on User.id = User_SchoolInformation.User_id where User_SchoolInformation.schoolInformation_id = '"+id+"'";
		search_query = "select * from SchoolInformation where id = '"+id+"'";
		String sender_name = "";
		Long sender_id = null;
		Logger.info(user_query);
		School school = new School();
		
		try{
			stmt = conn.createStatement();
			rs0 = stmt.executeQuery(user_query);
			while(rs0.next()){
				sender_name = rs0.getString("senderName");
				sender_id = rs0.getLong("user_id");
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		search_query1 = "select * from Data where sender_id = '"+sender_id+"'";
		try {
			rs = stmt.executeQuery(search_query);
			
			while(rs.next()){
				school.id = rs.getInt("id");
				school.name = rs.getString("name");
				school.address =rs.getString("address");
				school.totalStudent = rs.getString("totalStudent");
				school.femailStudent = rs.getString("femailStudent");
				school.maleStruden = rs.getString("maleStruden");
				school.totalToilets = rs.getString("totalToilets");
				school.totalTubewells = rs.getString("totalTubewells");
				//schoolInfoList.add(schoolInfo);	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<DataField>dataList = new ArrayList<DataField>();
		try{
			rs1 = stmt.executeQuery(search_query1);
			
			while(rs1.next()){
				DataField data = new DataField();
				data.data_id = rs1.getInt("id");
				data.received_date = rs1.getDate("created_at");
				data.sender_name = sender_name;
				dataList.add(data);
				//Logger.info(dataList.get(0).received_date+"oper");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		//Logger.info(dataList.toString());
		// for loop	
		for (int i = 0; i < dataList.size(); i++) {
			System.out.println(dataList.get(i).received_date);
		}*/

		String users = session.get("username");

		User user = User.findByName(users);

        List<Water> waterList=new ArrayList<Water>();
        List<Sanitation> sanitationList =new ArrayList<Sanitation>();
        List<SportsRecreation> sportsRecreationList =new ArrayList<SportsRecreation>();
        List<SchoolEnvironment> schoolEnvironmentList =new ArrayList<SchoolEnvironment>();



        List<GeoDivision> geoDivisionList = GeoDivision.findAll();
        List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
        List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
        List<Form> formList = Form.findAll();
        List<SchoolInformation> schoolList = SchoolInformation.findAll();

		Long roleId = user.role.id;




		if(roleId==3){

            Long schoolId = user.school.id;
            Logger.info("school: " + schoolId + "roleId: " + roleId);

		    waterList=Water.find("school_id=?",schoolId).fetch();
		    Logger.info("size: " + waterList.size());
            sanitationList = Sanitation.find("school_id=?",schoolId).fetch();
            sportsRecreationList = SportsRecreation.find("school_id=?",schoolId).fetch();
            schoolEnvironmentList = SchoolEnvironment.find("school_id=?",schoolId).fetch();

            render(geoDivisionList, geoDistrictList, geoUpazillaList,schoolList, formList,waterList,sanitationList,sportsRecreationList,schoolEnvironmentList);
        }


		else if(roleId!=3){

            waterList = Water.findAll();
            Logger.info("size: " + waterList.size());
            sanitationList = Sanitation.findAll();
            sportsRecreationList = SportsRecreation.findAll();
            schoolEnvironmentList = SchoolEnvironment.findAll();

            render(geoDivisionList, geoDistrictList, geoUpazillaList,schoolList, formList,waterList,sanitationList,sportsRecreationList,schoolEnvironmentList);

        }


	}
	
	
	public static String LoadDataReport(Long divisionId, Long districtId, Long upazillaId, Long schoolId, Long formId) throws SQLException {

		String mp = "";
		
		mp = Data.getDataReport(divisionId, districtId, upazillaId, schoolId , formId);
			
		Gson gson = new Gson();

		return gson.toJson(mp);
	}
	
	@ExternalRestrictions("View Data")
	public static void dataDetails(Long id) throws SQLException {

		Logger.info("id: "+ id);

		//dataId=dataId.trim();

		//Long iid = Long.parseLong(dataId);
		Data data = Data.findById(id);

		String approvalStatus = Data.getApprovalStatus(id);

		if(approvalStatus.equals("0")){

			renderArgs.put("approvalStatus",approvalStatus);

		}

        else if(approvalStatus.equals("1")){

		    TeacherResponse teacherResponse = TeacherResponse.find("data_id=?",id).first();

		    renderArgs.put("teacherResponse",teacherResponse);

            //renderArgs.put("approvalStatus",approvalStatus);

        }
		String totalStudent = Data.getTotalStudent(id);

		Logger.info("totalStudent: " + totalStudent);

		renderArgs.put("totalStudent",totalStudent);


		render(data,totalStudent);
		
	}
	
	 @ExternalRestrictions("Edit Data")
	    public static int delete(Long id) {
		 
		 Logger.info("id: "+ id);
		
	    	int confirm = 1;
	    	
	    
        	
	    	if(request.isAjax()) {
	    		
	    		List<Water> water1 = Water.find("data_id = ?", id).fetch();
	    		List<Sanitation> sanitation1 = Sanitation.find("data_id = ?", id).fetch();
	    		List<SchoolEnvironment> schoolEnvironment1 = SchoolEnvironment.find("data_id = ?", id).fetch();
	    		List<SportsRecreation> sportsRecreation1 = SportsRecreation.find("data_id = ?", id).fetch();
	    		
	            if(sanitation1.size()!=0){
    				
    				Sanitation sanitation = sanitation1.get(0);
        			Logger.info("sanitationget: " + sanitation1.get(0));
        			Logger.info("sanitationget2: " + sanitation);
        			
        			
        			sanitation.delete();
	            }
	            
	            else if(water1.size()!=0){
	    			Water water = water1.get(0);
	    			water.delete();
	    			
	    			}
	            else if(schoolEnvironment1.size()!=0){
	    			SchoolEnvironment schoolEnvironment = schoolEnvironment1.get(0);
	    			schoolEnvironment.delete();
	    			
	    			}
	            else if(sportsRecreation1.size()!=0){
	    			SportsRecreation sportsRecreation = sportsRecreation1.get(0);
	    			sportsRecreation.delete();
		    		
	    			}
      
    			
	    	
	    	}
	    	
	    	return confirm;
	    }

	public static void submitTeacherResponse(TeacherResponse teacherResponse) throws SQLException {

		/*TeacherResponse teacherResponse = new TeacherResponse();

		//teacherResponse.data.id=

        teacherResponse.comment = comment ;
        teacherResponse.isSolved = isSolved;
        teacherResponse.reason = reasons;*/

		Long id = teacherResponse.data.id;

		Data data = Data.findById(id);

		data.approvalStatus=1;

		data.save();

		String userid = session.get("username");

		User users = User.findByName(userid);

		teacherResponse.user= users;


        teacherResponse.save();


		/*teacherResponse.save();
		Logger.info("Record saved successfully.");*/

		//render("@dataDetails",id);

        dataDetails(id);

	}
}


