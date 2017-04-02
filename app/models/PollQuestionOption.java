package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import net.sf.oval.constraint.Length;
import play.db.jpa.Model;
import sun.org.mozilla.javascript.internal.regexp.SubString;

@Entity
public class PollQuestionOption extends Model {
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	public PollDefination poll;
	
	public String options;
	
	public PollQuestionOption(PollDefination poll) {
		this.poll = poll;
		
	}

	public static String findByPollId(Long id) {
		// TODO Auto-generated method stub
		
		String pollQuestionOptions = PollQuestionOption.find("SELECT id from PollQuestionOption WHERE poll_id = ? ", id).fetch().toString();
		String option = pollQuestionOptions.substring(1, pollQuestionOptions.length()-1);
		return option;
	}

	@Override
	public String toString() {
		return "PollQuestionOption [poll=" + poll + ", options=" + options + "]";
	}
	
	

}
