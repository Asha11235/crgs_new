package utils;

import java.util.ArrayList;
import java.util.Map;

public class Comparison {

	public String baseFormName;
	public String compFormName;
	Field reference;
	ArrayList<Field> comparableFields;
	
	public Comparison(String formname, String toForm, Field reference){
		this.baseFormName = formname;
		this.compFormName = toForm;
		if (reference != null)
			this.reference = reference;
		comparableFields = new ArrayList<Field>();
	}
	
	public void addField(Field f){
		this.comparableFields.add(f);
	}
	
	public Field getField(String fieldName){
		for (Field f: comparableFields)
			if (f.from.equals(fieldName) || f.to.equals(fieldName))
				return f;
		
		return null;
	}
	
	public ArrayList<Field> getFields(){
		return comparableFields;
	}
	
	public Field getReference(){
		return this.reference;
	}
	
	public void setReference(Field reference){
		this.reference = reference;
	}
	
	@Override
	public String toString() {
		return "'"+this.baseFormName + "' compares with '"+ this.compFormName+"' using fields "+ this.comparableFields +" using this reference "+ this.reference;
	}
}
