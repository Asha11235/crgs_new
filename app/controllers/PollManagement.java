package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.h2.util.New;

import com.google.gson.Gson;

import models.Data;
import models.GeoDistrict;
import models.PollDefination;
import models.PollQuestionOption;
import models.PollVoteReply;
import models.User;
import models.Vote;
import play.Logger;
import play.data.validation.Valid;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.With;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import groovyjarjarasm.asm.tree.IntInsnNode;


@With(Deadbolt.class) 
public class PollManagement extends Controller {
	@ExternalRestrictions("Edit Poll")
	public static void createPoll(PollDefination poll , PollQuestionOption pollQuestionOption){
		
		List<String> questionType = new ArrayList<String>();
		questionType.add("Single Select");
		questionType.add("Multiple Select");
		

		List<PollDefination> pollList = PollDefination.find("status = ? ", "1").fetch();
		
		//render("@editPoll",questiontype);
		/*List<PollDefination> poll = PollDefination.findById(id);*/
		render(questionType,poll,pollQuestionOption,pollList);
	}
	@ExternalRestrictions("Edit Poll")
	public static void editPoll(Long id){
		
		PollDefination poll = PollDefination.findById(id);
		
		flash("poll", "" + poll.id);
		session.put("poll", id);
	
		
		List<String> pollQuestionOption2 = PollQuestionOption.find("SELECT options from PollQuestionOption WHERE poll_id = ? ", id).fetch();
		
	
		List<PollQuestionOption> pollQuestionOptions = PollQuestionOption.find("poll_id = ?", id).fetch();
		
		PollQuestionOption pollQuestionOption = pollQuestionOptions.get(0);
		int j=0;
		List<String>  optionitem = new ArrayList<String>();
		for(int i=0 ; i< pollQuestionOption2.size() ; i++){
			//Logger.info("data: "+pollQuestionOption2.get(i) +" " );
			
		optionitem.addAll(Arrays.asList(pollQuestionOption2.get(i).split(",")));
		    
		}
		
		List<String> questionType = new ArrayList<String>();
		questionType.add(0,"Single Select");
		questionType.add(1,"Multiple Select");
		render(questionType,poll,pollQuestionOption,optionitem);
	}
	
	 @ExternalRestrictions("Edit Poll")
	public static void submit(@Valid PollDefination poll , @Valid PollQuestionOption pollQuestionOption){
		
		 validation.valid(poll);
		 validation.valid(pollQuestionOption);
		
		poll.createDate = new Date();
		poll.creater = User.findByName(session.get("username"));
		
		if(validation.hasErrors()){
			List<String> questiontype = new ArrayList<String>();
			questiontype.add("Single Select");
			questiontype.add("Multiple Select");
			//render("@editPoll",poll,questiontype);
			
		}
		
		Logger.info("poll: " + session.get("poll"));
		if(session.get("poll") != null){
			
			PollDefination editedPollDefination = PollDefination.findById(Long.parseLong(session.get("poll")));
			
			editedPollDefination.optionNumber = poll.optionNumber;
			editedPollDefination.questionType = poll.questionType;
			editedPollDefination.resultStatus = poll.resultStatus;
			editedPollDefination.startDate = poll.startDate ;
			editedPollDefination.endDate = poll.endDate ;
			editedPollDefination.status = poll.status;
			editedPollDefination.title = poll.title ;
			
			editedPollDefination.updatedby = User.findByName(session.get("username"));
            editedPollDefination.updatedDate = new Date();
            
            editedPollDefination=editedPollDefination.save();
           String questionid= PollQuestionOption.findByPollId(editedPollDefination.id);
          models.PollQuestionOption editedpollQuestionOption = PollQuestionOption.findById(Long.parseLong(questionid));
            
          editedpollQuestionOption.poll = editedPollDefination;
          editedpollQuestionOption.options = pollQuestionOption.options;
          editedpollQuestionOption.save();
          //  pollQuestionOption.poll = editedPollDefination ;
           // pollQuestionOption.save();
           //editedpollQuestionOption.save();
			Logger.info("edited");
		}
		else{
			
				    poll = poll.save();
				    Logger.info("not edited");
				   // models.PollQuestionOption nextpollQuestionOption = new models.PollQuestionOption(poll);
					
					//nextpollQuestionOption.poll = poll ;
					//nextpollQuestionOption.options = pollQuestionOption.options;
				    pollQuestionOption.poll = poll;
				    pollQuestionOption.save();
					//nextpollQuestionOption.save();
		}
		
		
		flash.success("Record saved successfully.");
		session.remove("poll");
		listPoll();
	}
	@ExternalRestrictions("View Poll")
	public static void listPoll(){
		List<PollDefination> listPoll = PollDefination.find("order by id desc").fetch();
		Logger.info("called list:");
		render(listPoll);
	}
	
	@ExternalRestrictions("Edit Poll")
    public static int deletePoll(Long id) {
    	
    	
    	int confirm = 1;
    	if(request.isAjax()) {
    		//Long id = Long.valueOf(request.params.get("userId"));
    		PollDefination poll = PollDefination.findById(id);
    		
    		 String questionid= PollQuestionOption.findByPollId(poll.id);
            models.PollQuestionOption pollQuestionOption = PollQuestionOption.findById(Long.parseLong(questionid));
              
        	
	    	notFoundIfNull(id, "id not provided");
	    	notFoundIfNull(poll, "poll not found");
	    	
	    	/*JPAQuery q = Data.find("sender = ?",user);
	    	List<Data> d = q.fetch();*/
	    	
	    	try {
	    		pollQuestionOption.delete();
	    		
	    		poll.delete();
			} catch (Exception e) {
				confirm = 0;
				e.printStackTrace();
				Logger.info("error in delete" + e);
			}
        	
    	}
    	
    	return confirm;
    }
	
	@ExternalRestrictions("View Poll")
	public static void detailPoll(Long id){
		PollDefination poll = PollDefination.findById(id);
		
		//Integer inputs = Integer.parseInt(poll.optionNumber);
		
		List<String> pollQuestionOption2 = PollQuestionOption.find("SELECT options from PollQuestionOption WHERE poll_id = ? ", poll.id).fetch();
		
		List<String>  optionitem = new ArrayList<String>();
		for(int i=0 ; i< pollQuestionOption2.size() ; i++){
			Logger.info("data: "+pollQuestionOption2.get(i) +" " );
			
		optionitem.addAll(Arrays.asList(pollQuestionOption2.get(i).split(",")));
		    
		}
		
		/*List<Integer> input = new ArrayList<Integer>();
		for(int i = 1;i<= Integer.parseInt(poll.optionNumber);i++){
			input.add(i);
		}
		
		List<PollQuestionOption> optionList = PollQuestionOption.find("poll = ?", poll).fetch();*/
		
		render(poll,optionitem);
	}
	
	public static void submitdetail(){
		String  pollid = request.params.get("poll.id");
		String [] options = request.params.getAll("poll[]");
		
		PollDefination polldef = PollDefination.findById(Long.parseLong(pollid));
		List<PollQuestionOption> opts = PollQuestionOption.find("poll = ? ", polldef).fetch();
		if(opts.size() == 0){
			for(int i = 0; i < options.length;i++){
				PollQuestionOption option = new PollQuestionOption(null);
				option.poll = polldef;
				option.options = options[i];
				option.save();
			}
		}
		else{
			for(int i =0 ; i< options.length; i++){
				PollQuestionOption option = opts.get(i);
				option.options = options[i];
				option.save();
			}
		}
		flash.success("Record saved successfully.");
		listPoll();
	}
	
	public static void publishPoll(Long id){
		PollDefination polldef = PollDefination.findById(id);
		polldef.status = "1";
		polldef.save();
		listPoll();
	}
	public static void removepublishPoll(Long id){
		PollDefination polldef = PollDefination.findById(id);
		polldef.status = "0";
		polldef.save();
		listPoll();
	}
	
	public static void makePollBox(){
		PollDefination polldef = PollDefination.find("status = '1'").first();
		List<PollQuestionOption> pollOption = PollQuestionOption.find("poll = ? ", polldef).fetch();
		
		render(polldef,pollOption);
	}
	
public static void voteReply(String gender, String option, String age,String pollId){
	
	
	    pollId=pollId.trim(); 
		Logger.info("PollId: " + pollId);
		Long pollid= Long.parseLong(pollId);
		PollDefination polldef = PollDefination.findById(pollid);
		
		PollVoteReply pollvotereply = new PollVoteReply();
		
		pollvotereply.gender=gender;
		pollvotereply.age=age;
		pollvotereply.poll=polldef;
		pollvotereply.answer=option;
		
		pollvotereply.save();
		
		
		try {
			if(session.get("username") != null){
				
				Forms.landingPage();
			}
			else{
				
				Secure.login();
			}
		
		}catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//Forms.landingPage();
	}

		public static String loadPoll() throws SQLException {
		
			String mp = "";
			
			mp = PollDefination.getPollReport();
				
			Gson gson = new Gson();
		
			return gson.toJson(mp);
		}

		public static void pollGraph(){


	        List<PollDefination> pollDefinationList = PollDefination.find(" status= 0 ").fetch();
			List<String> ageList = new ArrayList<String>();

			ageList.add(0,"All");
			ageList.add(1, "Below 18'+");
			ageList.add(2, "Above 18'+");

			List<String> genderList = new ArrayList<String>();

			genderList.add(0,"All");
			genderList.add(1, "Male");
			genderList.add(2, "Female");
			genderList.add(3,"Other");

	         render(pollDefinationList,ageList,genderList);

		//render();
			}

			 public static String loadPollGraph() throws SQLException {


			  // Logger.info("gender: " + gender + " age: " + age);


				 String mp = "";

				 mp = PollDefination.getPollGraphData();

				 Gson gson = new Gson();

				 return gson.toJson(mp);

			 }

			public static String loadPollGraphResult(String pollId,String gender,String age)throws SQLException{

				String mp = "";

				mp = PollDefination.getPollGraphResultData(pollId,gender,age);

				Gson gson = new Gson();

				return gson.toJson(mp);
			}
  
	
	
}