package controllers;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jobs.ExtractData;
import models.Data;
import models.Form;
import models.GeoUnion;
import models.SchoolInformation;
import models.User;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Document;

import play.Logger;
import play.Play;
import play.data.FileUpload;
import play.libs.XML;
import play.libs.XPath;
import play.mvc.Before;
import play.mvc.Controller;
import utils.CommonUtil;

/**
 * Mobile Controller - Mobile API end points handlers.
 */
public class Mobile extends Controller {

	@Before
	static void checkDigestAuth() {
		if (!DigestRequest.isAuthorized(request)) {
			throw new UnauthorizedDigest("Super Secret Stuff");
		}
	}

	/**
	 * List forms as XML (API end point).
	 */
	@annotations.Mobile
	public static void listAsXml() {
		
		Logger.info("ABCDEFGHIJ");
		List<Form> forms = Form.findAll();
		
		request.format = "xml";
		// response.setHeader("X-OpenRosa-Version", "1.0");
		render(forms);
	}

	/**
	 * Get file as binary
	 * 
	 * @param sourceFileName
	 *            - source file name with relative path
	 * 
	 * */
	@annotations.Mobile
	public static void getFileAsBinary(String sourceFileName) {
		String fileName = Play.applicationPath.getAbsolutePath()
				+ File.separator + sourceFileName;
		
		File file = new File(fileName);
		renderBinary(file, sourceFileName.substring(sourceFileName
				.lastIndexOf(File.separator) + 1));
	}

	/**
	 * View the form as XML (API end point).
	 * 
	 * @param id
	 *            the id
	 * @throws Exception
	 *             the exception
	 */
	@annotations.Mobile
	public static void getFormCSV(Long id) throws Exception {
		Form form = Form.findById(id);
		if (form == null) {
			throw new Exception();
		}

		String xmlText = new String(form.xml).trim().replaceFirst("^([\\W]+)<",
				"<");

		renderXml(xmlText);
	}

	/**
	 * View the form as XML (API end point).
	 * 
	 * @param id
	 *            the id
	 * @throws Exception
	 *             the exception
	 */
	@annotations.Mobile
	public static void viewAsXml(Long id) throws Exception {
		Form form = Form.findById(id);
		if (form == null) {
			throw new Exception();
		}

		String xmlText = new String(form.xml).trim().replaceFirst("^([\\W]+)<",
				"<");

		renderXml(xmlText);
	}

	/**
	 * Handle the form data (API end point).
	 * 
	 * @param xml_submission_file
	 *            name of the incoming XML, to notify the method that it should
	 *            take multipart/data
	 */
	@annotations.Mobile
	public static void submit(File xml_submission_file) {
	
		Logger.info("Submit from mobile1 " + xml_submission_file);
		
		String respondentId = "";
		if (request.method.equalsIgnoreCase("POST")) {
			// Supported MIME Types
			Set<String> mime_types = new HashSet<String>(
					Arrays.asList(new String[] { "text/xml", "image/jpeg",
							"audio/3gpp", "video/3gpp", "video/mp4",
							"text/csv", "audio/amr", "application/vnd.ms-excel" }));
			// Supported file extensions
			Set<String> extensions = new HashSet<String>(
					Arrays.asList(new String[] { "xml", "jpg", "3gpp", "3gp",
							"mp4", "csv", "amr", "xls", "3ga" }));
			// Get the default upload directory from application.
			String path = Play.configuration.getProperty("aggregate.uploadDir")
					+ File.separator;

			// Get response files
			List<FileUpload> uploads = (List<FileUpload>) request.args
					.get("__UPLOADS");

			// Check if it has any file or not
			if (uploads.size() > 0 && xml_submission_file != null) {
				long ret = 0;
				Boolean isSuccessful = false;
				String retMsg = "";
				for (int i = 0; i < uploads.size(); ++i) {
					FileUpload tmpFile = uploads.get(i);
					Logger.info("submit from mobile2 ", tmpFile);
					String fileName = tmpFile.getFileName();
					
					// First file should be the 'xml_submission_file' file
					if (i == 0) {
						if (tmpFile.getFieldName()
								.equals("xml_submission_file")) {
							Document dataXml = XML
									.getDocument(xml_submission_file);
							String formId = XPath.selectText("/data/@id",
									dataXml);
							Logger.info("DataXML id " + formId);
							Form form = Form.findById(Long.parseLong(formId));

							Data data = new Data(form, tmpFile.asBytes());
							User sender = User.findByName(session.get("apiUser"));
							SchoolInformation schoolLis = null;
							try{
								
								schoolLis = sender.school;
								if(schoolLis == null){
									isSuccessful = false;
									retMsg = "This NCTF user have no School Mapping";
								}
								//SchoolInformation school = User.find("schoolInformation = ?",sender.schoolInformation).first();

								else if(!sender.role.name.equals("NCTF")){
									isSuccessful = false;
									retMsg = "This user has no permitted to send data";
								}
								else {
									
									Logger.info("School Name " + schoolLis.name);
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
								data.schools =  sender.school;
								data.ngo = sender.ngo;

								data = data.save();
								// Run as a Job to give quick response in
								// mobile
								new ExtractData(data,sender).now();
								ret = data.id;
								respondentId = String.valueOf(data.id);
								isSuccessful = true;
								}
							}catch(Exception e){
								isSuccessful = false;
								retMsg = "You have no school assigned";
								e.printStackTrace();
							}
							
						}else {
							// Invalid File
							Logger.info("XML file expected but got '%s'",
									tmpFile.getFieldName());
							break;
						}
					} else {
						String extension = CommonUtil.getExtension(fileName);
						// Save only allowed files
						if (mime_types.contains(tmpFile.getContentType())
								&& extensions.contains(extension)) {
							tmpFile.asFile(path + fileName);

						} else {
							Logger.info("Unsupported Content: %s %s",
									tmpFile.getContentType(),
									CommonUtil.getExtension(fileName));
						}
					}
				}
				if (isSuccessful) {
					response.status = 201;
					//response.status = 203;
					response.setHeader("Location", "http://" + request.host);
					Logger.info("(Mobile.java) " + respondentId);
					renderText(" Data Id: " + respondentId);
				}
				else{
					//For NCTF Message Control 
					response.status = 403;
					response.setHeader("Location", "http://" + request.host);
					renderText("" + retMsg);
				}
			}
		} else {
			response.status = 204;
		}
	}
	
	public static String chageDateFormat(String date){
		final String PREVIOUS_FORMAT = "yyyy-MM-dd";
		final String CURRENT_FORMAT = "dd-MM-yy";

		String newDateString;

		SimpleDateFormat sdf = new SimpleDateFormat(PREVIOUS_FORMAT);
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sdf.applyPattern(CURRENT_FORMAT);
		newDateString = sdf.format(d);
		return newDateString;
	}	
}