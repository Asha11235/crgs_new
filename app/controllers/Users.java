package controllers;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import models.Aco;
import models.Data;
import models.GeoDistrict;
import models.GeoDivision;
import models.GeoUnion;
import models.GeoUpazilla;
import models.Ngo;
import models.Role;
import models.SchoolInformation;
import models.User;
import play.Logger;
import play.data.validation.Valid;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPA;
import play.mvc.With;
import responses.LoginResponse;
import annotations.Mobile;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import controllers.deadbolt.Unrestricted;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.Gson;

@With(Deadbolt.class)
public class Users extends Controller {

	@ExternalRestrictions("View User")
    public static void list(String page) {
        int currentPage = 1;
        int recordsPerPage = 20;
        int totalPages = 0;
        
        if (request.params.get("page") != null) {
            currentPage = Integer.parseInt(request.params.get("page"));     
        }
        
        if(request.params.get("page").equals(0)){
            currentPage = 1;
        }

        Query query = null;
        
        String countRow = "select count(*) from User";
        
        query = JPA.em().createQuery(countRow);
        int rowNumber = ( (Number)query.getSingleResult()).intValue();
        
        totalPages = (int) Math.ceil(rowNumber * 1.0 / recordsPerPage);

		List<User> users = User.find("name <> 'admin' order by id desc").fetch();
		
		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		//List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		//List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
		List<Role> roleList = Role.findAll();
		
		renderArgs.put("geoDivisionList", geoDivisionList);
		//renderArgs.put("geoDistrictList", geoDistrictList);
		//renderArgs.put("geoDistrictList", geoUpazillaList);
		renderArgs.put("roleList", roleList);
        render(users,rowNumber,currentPage,totalPages);
		
    }
 
	@ExternalRestrictions("View User")
    public static void details(Long id) {
		User user = User.findById(id);
		notFoundIfNull(user, "user not found");
		
		String fieldName = "geoUpazilla.geoDistrict";
		render(user, fieldName);
    }

	@ExternalRestrictions("Edit User")
    public static void assignGeoPSU(Long userId, Long[] geoPSUs, Boolean remove) {
		notFoundIfNull(userId, "No userId provided");
		ok();
    }
	
    @ExternalRestrictions("Edit User")
    public static void create() {
    	List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		//List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		//List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
    	List<Role> roles = Role.findAll();
    	List<User> users = User.find("role = ?", Role.findByName("Mobilizer")).fetch();
    	//List<SchoolInformation> schoolList = SchoolInformation.find("approavedStatus = ? ", "Approved").fetch();
    	List<Ngo> ngoList = Ngo.findAll();
    	//render("@edit", users, roles,schoolList,ngoList,geoDivisionList,geoDistrictList,geoUpazillaList);
    	render("@edit", users, roles,ngoList,geoDivisionList);
    }

	@ExternalRestrictions("Edit User")
   public static void edit(Long id) {
		
    	User user = User.findById(id);
    	flash("user", "" + user.id);
    	notFoundIfNull(user, "user not found");
    	user.password = null;
    	List<Role> roles = Role.findAll();
    	//List<SchoolInformation> schoolList = SchoolInformation.find("approavedStatus = ? ", "Approved").fetch();
    	List<Ngo> ngoList = Ngo.findAll();
    	
    	List<GeoDivision> geoDivisionList = GeoDivision.findAll();
		//List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
		//List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
    	//render(user,schoolList,ngoList, roles,geoDivisionList,geoDistrictList,geoUpazillaList);
    	render(user,ngoList, roles,geoDivisionList);
    }
	
	
	public static void cancelEdit(){
		Logger.info("flashUserId in CancelEdit: " + flash.get("user"));
		render("@edit");
	}

    @ExternalRestrictions("Edit User")
   public static void submit(@Valid models.User user) {
    	
    	
    	Logger.info("flashUserId in Submit: " + flash.get("user") + " and user id is : " + user.id);
    	/*validation.valid(user);*/
    	Logger.info("validation: " + validation.hasErrors());
    	if(validation.hasErrors() && flash.get("user") == null) {
    		List<Role> roles = Role.findAll();
    		List<GeoDivision> geoDivisionList = GeoDivision.findAll();
    		List<GeoDistrict> geoDistrictList = GeoDistrict.findAll();
    		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.findAll();
    		List<SchoolInformation> schoolList = SchoolInformation.findAll();
    		List<Ngo>ngoList = Ngo.findAll();
    		if(user.name==null || user.displayName == null ||  user.email ==null || user.role == null || user.school == null || user.ngo==null || user.geoDivision == null || user.geoDistrict== null || user.geoUpazilla==null){
    			
    			flash.success("Error occured.Get null value. Please,try again");
    		}
    			
        	render("@edit", user, roles , geoDivisionList ,ngoList , geoDistrictList , geoUpazillaList , schoolList);
        }
    	
    	
	    //Logger.info ("id: "+ user.geoUpazilla.id);   
    	 if(flash.get("user") != null){
    		
    		 //Logger.info("not null: "+validation.valid(user).toString());
    		User editedUser = User.findById(Long.parseLong(flash.get("user")));
    		
    		editedUser.name = user.name;
    		editedUser.displayName = user.displayName;
    		editedUser.email = user.email;
    		editedUser.role = user.role;
    		editedUser.school= user.school;
    		editedUser.ngo = user.ngo;
    		editedUser.geoDivision = user.geoDivision;
    		editedUser.geoDistrict = user.geoDistrict;
    		editedUser.geoUpazilla = user.geoUpazilla;
    		
if(user.name==null || user.displayName == null ||  user.email ==null || user.role == null || user.school == null || user.ngo==null || user.geoDivision == null || user.geoDistrict== null || user.geoUpazilla==null){
	
	flash.success("Error occured.Get null value. Please,try again");
    list("0");
	
}
    		
    		//Logger.info("id : " + editedUser.id + " name: " + editedUser.name + " displayname: " + editedUser.displayName + " email: " + editedUser.email + " role: " + editedUser.role.id + " ngoId: " + editedUser.ngo.id + " geoDivisionId: " 
    		//+ editedUser.geoDivision.id + " geoDistrictId: "+ editedUser.geoDistrict.id + " geoUpazilaId: "+ editedUser.geoUpazilla.id);
else{
	
	editedUser.save();
	flash.success("Record saved successfully.");
    list("0");
	
}
    		
    		
    		
    	}
    	else {
    		
    		user.save();
    		 flash.success("Record saved successfully.");
    	     list("0");
    		
    		Logger.info("save successfully" + user.toString());
		}
        
       
    }
    

    
    @ExternalRestrictions("Edit User")
    public static void loadMobilizerList(User user) {
       	List<Role> roles = Role.findAll();

       	render("@edit",user, roles);
    }

    /*@ExternalRestrictions("Edit User")
    public static void delete(Long id) {
    	Logger.info("enter delete method");
    	if(request.isAjax()) {
	    	notFoundIfNull(id, "id not provided");
	    	User user = User.findById(id);
	    	notFoundIfNull(user, "user not found");
	    	//user.delete();
	    	ok();
    	}
    }*/
    
    @ExternalRestrictions("Edit User")
    public static int delete(Long id) {
    	
    	
    	int confirm = 1;
    	if(request.isAjax()) {
    		//Long id = Long.valueOf(request.params.get("userId"));
    		User user = User.findById(id);
        	
	    	notFoundIfNull(id, "id not provided");
	    	notFoundIfNull(user, "user not found");
	    	
	    	JPAQuery q = Data.find("sender = ?",user);
	    	List<Data> d = q.fetch();
	    	
	    	int sizee = d.size();
	    	//Logger.info("Data : " +  d);
	    	
	    	if ( sizee == 0){
	    	
	    		user.delete();
	    		flash.success("Record delete successfully.");
	    		
	    	}
	    	
	    	else{
	    		
	    		confirm = 0;
	    		flash.success("Record can not be deleted.");
	    	}
	    	/*try {
	    	
			} catch (Exception e) {
				confirm = 0;
				
				//e.printStackTrace();
				//Logger.info("error in delete" + e);
			}*/
        	
    	}
    	
    	return confirm;
    }
    /* Roles */
	@ExternalRestrictions("Edit User")
    public static void roleList() {
		List<Role> roles = Role.find("id <> 1").fetch();
        render(roles);
    }

    @ExternalRestrictions("Edit Role")
    public static void roleCreate() {
    	render("@roleEdit");
    }

	@ExternalRestrictions("Edit Role")
    public static void roleEdit(Long id) {
		Logger.info("Edit role");
    	Role role = Role.findById(id);
    	notFoundIfNull(role, "user not found");
    	render(role);
    }

	
	
    @ExternalRestrictions("Edit User")
    public static void roleSubmit(@Valid Role role) {
        if(validation.hasErrors()) {
        	render("@roleEdit", role);
        }
        
        String name = role.name;
        
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(name);
       
        boolean b = m.find();
        if (b == false){
        	
        	role.save();
            flash.success("Record saved successfully.");
            roleList();
        }
           
        else if ( b== true){
        	 flash.success("Role can not get any special character.");
        	 render("@roleEdit", role);
        } 
        
    }

    @ExternalRestrictions("Edit Role")
    public static int roleDelete(Long id) {
    	
    	
    	 int confirm = 0;
	    	if(request.isAjax()) {
	    		notFoundIfNull(id, "id not provided");
		    	Role role = Role.findById(id);
		    	notFoundIfNull(role, "role not found");
		    	try {
		    		role.delete();
		    		render();
		    		confirm = 1;
				} catch (Exception e) {
					// TODO: handle exception
					
				}
		    	
	        	
	    	}
	    	
    	return confirm;
    }

    
	/* Access Control List */
    @ExternalRestrictions("ACL")
    public static void acl() {
    	Logger.info("Access control");
    	List<Role> roles = Role.findAll();
    	List<Aco> acos = Aco.find("name <> 'ACL'").fetch();
    	render(roles, acos);
    }

    @ExternalRestrictions("ACL")
    public static void updatePermission(long acoId, long roleId, boolean state) {
    	notFoundIfNull(acoId);
    	notFoundIfNull(roleId);
    	notFoundIfNull(state);
    	Aco aco = Aco.findById(acoId);
    	Role role = Role.findById(roleId);
		if(role.id == 1) {
			ok();
		}
    	notFoundIfNull(aco);
    	notFoundIfNull(role);
    	if(state) {
    		aco.roles.add(role);
    	} else {
    		aco.roles.remove(role);
    	}
    	aco.save();
    	ok();
    }

    /*
     * All mobile API end points are prefixed with the letter 'm'
     */
    @Unrestricted
    @Mobile
    public static void mLogin() {
		if(!session.contains("apiUser")) {
			error(424, "Session expired");
		}
		User user = User.find("byName", session.get("apiUser")).first();
		renderJSON(new LoginResponse(user));
    }
    
    public static void loadGeoDistrict(Long id) {
    	
		GeoDivision geoDivision = GeoDivision.findById(id);
		notFoundIfNull(geoDivision);
		List<GeoDistrict> geoDistrictList = GeoDistrict.find("geoDivision = ? ", geoDivision).fetch();
		render(geoDistrictList);
	}
   
    public static void loadGeoUpazilla(Long id) {
		GeoDistrict geoDistrict = GeoDistrict.findById(id);
		notFoundIfNull(geoDistrict);
		List<GeoUpazilla> geoUpazillaList = GeoUpazilla.find("geoDistrict = ? ", geoDistrict).fetch();
		render(geoUpazillaList);
	}
    
    public static void loadGeoUnion(Long id) {
		GeoUpazilla geoUpazilla = GeoUpazilla.findById(id);
		notFoundIfNull(geoUpazilla);
		List<GeoUnion> geoUnionList = GeoUnion.find("geoUpazilla = ?", geoUpazilla).fetch();
		
		render(geoUnionList);
	}
    
 public static void loadGeoSchool(Long id) {
    	
		GeoUpazilla geoUpazilla = GeoUpazilla.findById(id);
		notFoundIfNull(geoUpazilla);
		List<SchoolInformation> schoolList = SchoolInformation.find("geoUpazilla = ?", geoUpazilla).fetch();
		
		render(schoolList);
	}
  
    public static String loadUser(Long divisionId,Long districtId, Long upazillaId, Long schoolId, 
			Long roleId, Date startDate, Date endDate) throws SQLException {
    	
    	String msg="";
    	
    	msg= User.getUserData(divisionId, districtId, upazillaId, schoolId, roleId, startDate, endDate);
    	
    	Gson gson = new Gson();

		return gson.toJson(msg);
    }
}
