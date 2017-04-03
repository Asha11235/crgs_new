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
public class PollVoteReply extends Model {
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	public PollDefination poll;
	
	public String gender;
	public String age;
	public String answer;
	@Override
	public String toString() {
		return "PollVoteReply [poll=" + poll + ", gender=" + gender + ", age=" + age + ", answer=" + answer + "]";
	}
	
	/*public PollVoteReply(PollDefination poll) {
		this.poll = poll;
		
	}*/

	

}
