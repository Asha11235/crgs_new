package models;

import javax.persistence.Entity;

import play.db.jpa.Model;
import play.modules.chronostamp.NoChronostamp;

@NoChronostamp
@Entity
public class NotificationMsg extends Model{

	public String Keyword;
	public String Msg;
	public String getKeyword() {
		return Keyword;
	}
	public void setKeyword(String keyword) {
		Keyword = keyword;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	
}
