package models;

public class AnswerOptionOfQuestion {
	public String label;
	public String value;

	public AnswerOptionOfQuestion(String label, String value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
