package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.persistence.Query;

import controllers.Application;

public class Field {

	public static final int TYPE_REFERENCE = 111;
	
	public static final int TYPE_NUMERIC = 112;
	public static final int TYPE_STRING_MATCH = 113;
	public static final int TYPE_STRING_LENGTH = 114;

	public String from;
	public String to;
	public String title;
	public int comparisonType;

	private String errorMsg = "comparisonType has to be either 'TYPE_REFERENCE', 'TYPE_NUMERIC', 'TYPE_STRING_MATCH' or 'TYPE_STRING_LENGTH'";

	public Field(String from, String to, String title, int comparisonType) throws Exception {
		if (comparisonType < 111 || comparisonType > 114)
			throw new Exception(errorMsg);
		
		this.from = from;
		this.to = to;
		this.title = title;
		this.comparisonType = comparisonType;
	}

	@Override
	public String toString() {
		return "Field Named '"+title+"' using '"+from+"' compared to '"+to+"' using type '"+comparisonType+"'";
	}

}
