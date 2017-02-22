package utils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import models.Data;
import models.Form;
import models.SchoolInformation;
import models.User;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.regexp.internal.recompile;

import play.Logger;
import play.libs.XML;
import play.libs.XPath;
import controllers.Mobile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import jobs.ExtractData;

public class XMLBuilder {
	
	public static String XmlFile(User sender,String id,String [] res_type,
									String [] water_source, String [] num_waterSource, String [] activeWaterSource,
									String [] is_potable, String [] why_not_potable, String [] why_not_potable_other,
									String [] is_tank_cleaned, String [] is_informed_authority_water_prob, String [] how_informed_water_prob, 
									String [] how_informed_water_prob_other, String [] water_prob_solved_authority,
									String [] rank_water){
		DOMSource source = null;
		Document doc = null;
		try {
	         DocumentBuilderFactory dbFactory =
	         DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = 
	         dbFactory.newDocumentBuilder();
	         doc = dBuilder.newDocument();
	         // root element
	         
	         Element rootElement = doc.createElement("data");
	         Attr idnode = doc.createAttribute("id");
	         idnode.setValue("2");
	         rootElement.setAttributeNode(idnode);
	         doc.appendChild(rootElement);

	         // carname element
	         
	         Element istart = doc.createElement("i_start_time");
	         istart.appendChild(doc.createTextNode(""));
	         rootElement.appendChild(istart);
	         
	         Element restype = doc.createElement("res_type");
	         restype.appendChild(doc.createTextNode(arrayToString(res_type)));
	         rootElement.appendChild(restype);
	         
	         Element watersource = doc.createElement("water_source");
	         watersource.appendChild(doc.createTextNode(arrayToString(water_source)));
	         rootElement.appendChild(watersource);
	         
	         Element numwaterSource = doc.createElement("num_waterSource");
	         numwaterSource.appendChild(doc.createTextNode(arrayToString(num_waterSource)));
	         rootElement.appendChild(numwaterSource);
	         
	         Element activeWSource = doc.createElement("activeWaterSource");
	         activeWSource.appendChild(doc.createTextNode(arrayToString(activeWaterSource)));
	         rootElement.appendChild(activeWSource);
	         
	         Element ispotable = doc.createElement("is_potable");
	         ispotable.appendChild(doc.createTextNode(arrayToString(is_potable)));
	         rootElement.appendChild(ispotable);
	         
	         Element whynot_potable = doc.createElement("why_not_potable");
	         whynot_potable.appendChild(doc.createTextNode(arrayToString(why_not_potable)));
	         rootElement.appendChild(whynot_potable);
	         
	         Element whynot_potable_other = doc.createElement("why_not_potable_other");
	         whynot_potable_other.appendChild(doc.createTextNode(arrayToString(why_not_potable_other)));
	         rootElement.appendChild(whynot_potable_other);
	         
	         Element istank_cleaned = doc.createElement("is_tank_cleaned");
	         istank_cleaned.appendChild(doc.createTextNode(arrayToString(is_tank_cleaned)));
	         rootElement.appendChild(istank_cleaned);
	         
	         Element isinformed_authority_water_prob = doc.createElement("is_informed_authority_water_prob");
	         isinformed_authority_water_prob.appendChild(doc.createTextNode(arrayToString(is_informed_authority_water_prob)));
	         rootElement.appendChild(isinformed_authority_water_prob);
	         
	         Element howinformed_water_prob = doc.createElement("how_informed_water_prob");
	         howinformed_water_prob.appendChild(doc.createTextNode(arrayToString(how_informed_water_prob)));
	         rootElement.appendChild(howinformed_water_prob);
	         
	         Element howinformed_water_prob_other = doc.createElement("how_informed_water_prob_other");
	         howinformed_water_prob_other.appendChild(doc.createTextNode(arrayToString(how_informed_water_prob_other)));
	         rootElement.appendChild(howinformed_water_prob_other);
	         
	         Element waterprob_solved_authority = doc.createElement("water_prob_solved_authority");
	         waterprob_solved_authority.appendChild(doc.createTextNode(arrayToString(water_prob_solved_authority)));
	         rootElement.appendChild(waterprob_solved_authority);
	         
	         Element rankwater = doc.createElement("rank_water");
	         rankwater.appendChild(doc.createTextNode(arrayToString(rank_water)));
	         rootElement.appendChild(rankwater);
	         
	         Element iend = doc.createElement("i_end_time");
	         iend.appendChild(doc.createTextNode(""));
	         rootElement.appendChild(iend);
	         
	         // write the content into xml file
	         TransformerFactory transformerFactory = TransformerFactory.newInstance();
	         Transformer transformer = transformerFactory.newTransformer();
	         source = new DOMSource(doc);
	         StreamResult result = new StreamResult(new File("/home/shakil/Desktop/data.xml"));
	         transformer.transform(source, result);
	         // Output to console for testing
	         StreamResult consoleResult = new StreamResult(System.out);
	         transformer.transform(source, consoleResult);
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		byte []array = null;
		try {
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
         StringWriter stringWriter = new StringWriter();
         Result result = new StreamResult(out);
         TransformerFactory factory = TransformerFactory.newInstance();
         Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
			array =  out.toByteArray();
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         
		
		
		/*ByteArrayOutputStream bos=new ByteArrayOutputStream();
		StreamResult result=new StreamResult(bos);
		result = result;
		
		byte []array=bos.toByteArray();*/
		
		
		Form form = Form.findById(Long.parseLong(id));
		Document dataXml = doc;
		
		Data data = new Data(form, array);
		//User sender = User.findByName(session.get("username"));
		SchoolInformation schoolLis = null;
		try{
			schoolLis =  sender.school;
			/*if(schoolLis.iterator().next() == null){
				isSuccessful = false;
				retMsg = "This NCTF user have no School Mapping";
			}*/
			//SchoolInformation school = User.find("schoolInformation = ?",sender.schoolInformation).first();
			if(!sender.role.name.equals("NCTF")){
				String iStartTime = XPath.selectText(
						"//data/i_start_time", dataXml);
				String iEndTime = XPath.selectText(
						"//data/i_end_time", dataXml);

				DateTimeFormatter dtf = ISODateTimeFormat
						.dateTime(); // ISO8601 (XML) Date/time

				/*data.startTime = dtf.parseDateTime(iStartTime)
						.toDate();

				data.endTime = dtf.parseDateTime(iEndTime).toDate();*/

				data.sender = sender;
				data.senderRole = data.sender.role;
				data.isVisited = false;
				data.resolveDate = new Date();
				//data.schools = schoolLis.iterator().next();
				

				data = data.save();
				// Run as a Job to give quick response in
				// mobile
				new ExtractData(data).now();
			}
			else {
				String iStartTime = XPath.selectText(
						"//data/i_start_time", dataXml);
				String iEndTime = XPath.selectText(
						"//data/i_end_time", dataXml);
	
				DateTimeFormatter dtf = ISODateTimeFormat
						.dateTime(); // ISO8601 (XML) Date/time
	
				data.startTime = dtf.parseDateTime(iStartTime)
						.toDate();
	
				data.endTime = dtf.parseDateTime(iEndTime).toDate();
	
				data.sender = sender;
				data.senderRole = data.sender.role;
				data.isVisited = false;
				data.resolveDate = new Date();
				data.schools = schoolLis;
				
	
				data = data.save();
				// Run as a Job to give quick response in
				// mobile
				new ExtractData(data).now();
			}
		
			}catch(Exception e){
				e.printStackTrace();
			}
		
		//Mobile.submit(new File("/home/shakil/Desktop/data.xml"));
		return "";
	}
	
	public static String arrayToString(String [] arr){
		if(arr == null) return "1"; // my check
		String s = "";
		if(arr.length > 1){
			for(int i = 0; i< arr.length; i++){
				if(i == arr.length-1){
					s = s + arr[i];
				}
				else{
					s = s + arr[i]+" ";
				}
			}
		}
		else {
			s = arr[0];
		}
		
		Logger.info("YEss " + s);
		return s;
	}
	
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
	   
	    Date now = new Date();
	    DateTime dateTime = getItemDate(now.toString(),"yyyy-MM-dd HH:mm:ss");
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	public static DateTime getItemDate(String date, String pattern) {
        return DateTimeFormat.forPattern(pattern)
            .parseDateTime(date)
                .withZone(DateTimeZone.getDefault());
    }

}
