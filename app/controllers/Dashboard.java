package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import utils.DataField;

import javax.persistence.Query;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jobs.FormValidationParsing;
import models.Data;
import models.Form;
import models.FormStatus;
import models.SchoolInformation;
import models.UnitData;
import models.User;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.Logger;
import play.Play;
import play.data.FileUpload;
import play.data.validation.Valid;
import play.db.jpa.JPA;
import play.mvc.Util;
import play.mvc.With;
import utils.CommonUtil;
import utils.UserUtils;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


//import controllers.Reports.schoolInfo;

public class Dashboard extends  Controller{

    public static int getQuarter() {
 		int quarter = 1;
 		Date date = new Date();
 		
 		Calendar cal = Calendar.getInstance();
 		cal.setTime(date);
 		int month = cal.get(Calendar.MONTH);
 		if(month >=1 && month <=3){
 			quarter = 1 ;
 		}else if(month >=4 && month <=6){
 			quarter = 2 ;	
 		}else if(month >=7 && month <=9){
 			quarter = 3 ;
 		}else if(month >=10 && month <=12){
 			quarter = 4 ;
 		}
 		return quarter;
 	}
    
	public static void showQuestionnaire(){
	    	
	  render();
	}
	
	public static void homedashboard(){
		render();
	}

}
