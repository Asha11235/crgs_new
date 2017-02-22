package models;

public enum FormType {

	DEFAULT, CONSENT, FOOD;

	public String getName() {
		String tmp = name();
		return tmp.charAt(0) + tmp.toLowerCase().substring(1);
	}

}