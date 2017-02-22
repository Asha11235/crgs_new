package utils;

public class ApprovalState {

	private String name;
	private long duration;
	private int value;
	
	public ApprovalState(String name, String duration, String value){
		this.name = name;
		this.duration = Long.parseLong(duration);
		this.value = Integer.parseInt(value);
	}
	
	public String getName(){
		return new String(this.name);
	}
	
	public long getDuration(){
		return this.duration;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public String get(int value){
		if (this.value == value)
			return this.name;
		else return null;
	}
	
	@Override
	public String toString(){
		return this.name + " "+this.duration + " "+this.value;
	}
	
}
