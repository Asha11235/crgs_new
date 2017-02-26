package models;

public enum ScheduleStatus {

	INACTIVE, ACTIVE, RUNNING, HOLD, DONE, EXPIRED, CANCELED, REFUSED, PERMANENTLY_REFUSED;

	public String getName() {
		String tmp = name();
		return tmp.charAt(0) + tmp.toLowerCase().substring(1).replaceAll("_", " ");
	}

}