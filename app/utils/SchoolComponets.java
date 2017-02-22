package utils;

public class SchoolComponets {
	public String name;
	public String avg;
	public SchoolComponets(String name, String avg) {
		super();
		this.name = name;
		this.avg = avg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvg() {
		return avg;
	}
	public void setAvg(String avg) {
		this.avg = avg;
	}
}
