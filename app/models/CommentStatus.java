package models;

/**
 * Status of comment
 * */
public enum CommentStatus {
	ACTIVE("active"), SENT("sent"), INACTIVE("inactive");
	
	private String value;
	
	private CommentStatus(String value){
		this.value = value; 
	}
	
	public String getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return this.value.toUpperCase().charAt(0) + this.value.toLowerCase().substring(1);		
	}
}
