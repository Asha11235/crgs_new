package models;

public enum QuestionType {
	FORM_XML, MANUAL;
	
	String type ;
	
	private QuestionType() {
		this.type = this.name(); 
	}
	private QuestionType(String type) {
		this.type = type; 
	}
}
