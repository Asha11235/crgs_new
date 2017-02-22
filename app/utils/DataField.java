package utils;

import java.util.Date;

import org.joda.time.DateTime;

public class DataField {
	public int data_id;
	public int sender_id;
	public Date received_date;
	public String sender_name;
	public Date startTime;
	public Date startTime1;
	public Date endTime;
	public boolean isVisited;
	public Long receivedTimemili;
	@Override
	public String toString() {
		return "DataField [data_id=" + data_id + ", sender_id=" + sender_id
				+ ", received_date=" + received_date + "]";
	}

}
