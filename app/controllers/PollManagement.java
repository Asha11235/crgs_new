package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.PollDefination;
import models.PollQuestionOption;
import models.User;
import models.Vote;
import play.Logger;
import play.data.validation.Valid;
import play.mvc.With;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;


@With(Deadbolt.class) 
public class PollManagement extends Controller {
	@ExternalRestrictions("Edit Poll")
	public static void createPoll(){
		List<String> questiontype = new ArrayList<String>();
		questiontype.add("Single Select");
		questiontype.add("Multipul Select");
		render("@editPoll",questiontype);
	}
	@ExternalRestrictions("Edit Poll")
	public static void editPoll(Long id){
		PollDefination poll = PollDefination.findById(id);
		List<String> questiontype = new ArrayList<String>();
		questiontype.add("Single Select");
		questiontype.add("Multipul Select");
		render(questiontype,poll);
	}
	
	public static void submit(@Valid PollDefination poll){
		validation.valid(poll);
		poll.createDate = new Date();
		poll.creater = User.findByName(session.get("username"));
		if(validation.hasErrors()){
			List<String> questiontype = new ArrayList<String>();
			questiontype.add("Single Select");
			questiontype.add("Multipul Select");
			render("@editPoll",poll,questiontype);
		}
		poll.save();
		flash.success("Record saved successfully.");
		listPoll();
	}
	@ExternalRestrictions("View Poll")
	public static void listPoll(){
		List<PollDefination> listPoll = PollDefination.find("order by id desc").fetch();
		render(listPoll);
	}
	
	public static void deletePoll(Long id){
		PollDefination poll = PollDefination.findById(id);
		poll.delete();
		ok();
	}
	
	public static void detailPoll(Long id){
		PollDefination poll = PollDefination.findById(id);
		
		Integer inputs = Integer.parseInt(poll.optionNumber);
		
		List<Integer> input = new ArrayList<Integer>();
		for(int i = 1;i<= Integer.parseInt(poll.optionNumber);i++){
			input.add(i);
		}
		
		List<PollQuestionOption> optionList = PollQuestionOption.find("poll = ?", poll).fetch();
		
		render(poll,input,optionList);
	}
	
	public static void submitdetail(){
		String  pollid = request.params.get("poll.id");
		String [] options = request.params.getAll("poll[]");
		
		PollDefination polldef = PollDefination.findById(Long.parseLong(pollid));
		List<PollQuestionOption> opts = PollQuestionOption.find("poll = ? ", polldef).fetch();
		if(opts.size() == 0){
			for(int i = 0; i < options.length;i++){
				PollQuestionOption option = new PollQuestionOption();
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
	
	/* public static void submit(){
	    	String polldev = request.params.get("polldef.id");
	    	String [] options = request.params.getAll("group1");
	    	Logger.info("Voting COntroller "  + polldev);
	    	PollDefination poll = PollDefination.findById(Long.parseLong(polldev));
	    	
	    	for(int i = 0;i<options.length; i++){
	    		PollQuestionOption option =PollQuestionOption.findById(Long.parseLong(options[i]));
	    		Vote vote = new Vote();
	    		vote.poll = poll;
	    		vote.opton = option;
	    		vote.date = new Date();
	    		vote.save();
	    	}
	    	flash.success("Thanks for your vote");
	    }*/
	
}
