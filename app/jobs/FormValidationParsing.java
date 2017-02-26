package jobs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.fileupload.InvalidFileNameException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import controllers.Application;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStop;
import play.libs.XML;
import play.libs.XPath;
import utils.Approval;
import utils.ApprovalData;
import utils.ApprovalLogic;
import utils.ApprovalState;

public class FormValidationParsing extends Job {

	private final static String ERROR_INVALID_FILE = "The file has to be an XML file. Please check if the correct file has been uploaded";

	/** List of form elements */
	private final static String TAG_FORM = "form";
	private final static String TAG_STATE_LIST = "states";
	private final static String TAG_STATE = "state";
	private final static String TAG_APPROVAL = "approval";
	private final static String TAG_ROLE = "role";
	private final static String TAG_ACCEPT = "accept";
	private final static String TAG_REJECT = "reject";
	private final static String TAG_DEFAULT = "default";

	private final static String PROPERTY_NAME = "name";
	private final static String PROPERTY_DURATION = "duration";

	private final static String EVENT_ON_STATE = "onState";
	/** End list of form elements */

	private Document xmlDoc;

	private ArrayList<ApprovalState> stateList;
	private ArrayList<ApprovalLogic> approvalConditions;

	/**
	 * Create a class to parse out the form validation configuration
	 * 
	 * @param file
	 *            - The form validation config file. The file type has to be
	 *            .xml
	 * */
	public FormValidationParsing(File file) {
		if (!file.getName().endsWith(".xml"))
			throw new InvalidFileNameException(file.getName(),
					ERROR_INVALID_FILE);
		xmlDoc = XML.getDocument(file);
		validateDocument();
	}

	/**
	 * Create a class to parse out the form validation configuration
	 * 
	 * @param file
	 *            - The form validation config file's byte array.
	 * */
	public FormValidationParsing(byte[] file) {
		final InputStream inputStream = new ByteArrayInputStream(file);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;

		try {
			builder = factory.newDocumentBuilder();

			doc = builder.parse(inputStream);
		} catch (Exception e) {
		}
		this.xmlDoc = doc;
		/*
		 * if (!file.getName().endsWith(".xml")) throw new
		 * InvalidFileNameException(file.getName(), ERROR_INVALID_FILE); xmlDoc
		 * = XML.getDocument(file);
		 */
		validateDocument();
	}

	private void validateDocument() {
		NodeList formNode = xmlDoc.getElementsByTagName(TAG_FORM);
		Node form = formNode.item(0);
		String formName = form.getAttributes().getNamedItem(PROPERTY_NAME)
				.getNodeValue();
		Logger.info(formName);
		ArrayList<ApprovalState> states = validateStates(xmlDoc
				.getElementsByTagName(TAG_STATE));
		ArrayList<ApprovalLogic> approval = validateApproval(xmlDoc
				.getElementsByTagName(TAG_APPROVAL));
		Logger.info(states.toString());
		Logger.info(approval.toString());

		ApprovalData appro = new ApprovalData(formName, states, approval);
		if (!Application.approval.hasApproval(formName))
			Application.approval.addApproval(appro);
	}

	/**
	 * This is a method to parse the state list from the xml definition for the
	 * current form
	 * 
	 * @param stateList
	 *            - The list of "state" nodes
	 * @return The list of State objects contained within the document
	 * */
	private ArrayList<ApprovalState> validateStates(NodeList stateList) {
		ArrayList<ApprovalState> states = new ArrayList<ApprovalState>();
		for (int i = 0; i < stateList.getLength(); i++) {
			NamedNodeMap attributes = stateList.item(i).getAttributes();
			if (attributes.getNamedItem(PROPERTY_NAME) != null) {
				String state = attributes.getNamedItem(PROPERTY_NAME)
						.getNodeValue();
				String duration = "-1";
				if (attributes.getNamedItem(PROPERTY_DURATION) != null)
					duration = stateList.item(i).getAttributes()
							.getNamedItem(PROPERTY_DURATION).getNodeValue();
				String value = stateList.item(i).getTextContent();
				ApprovalState s = new ApprovalState(state, duration, value);
				Logger.info(state + " " + duration + " " + value);
				states.add(s);
			}
		}
		return states;
	}

	/**
	 * This is a method to parse the approval logic list from the xml definition
	 * for the current form
	 * 
	 * @param approvalList
	 *            - The list of "approval" nodes
	 * @return The list of ApprovalLogic objects contained within the document
	 * */
	private ArrayList<ApprovalLogic> validateApproval(NodeList approvalList) {
		ArrayList<ApprovalLogic> approvals = new ArrayList<ApprovalLogic>();
		NodeList roles = approvalList.item(0).getChildNodes();
		Logger.info("" + approvalList.item(0).getNodeName());
		for (int i = 0; i < roles.getLength(); i++) {
			Node role = roles.item(i);
			if (role.getNodeName().startsWith("#"))
				continue;

			Logger.info(role.getNodeName());
			String roleName = role.getAttributes().getNamedItem(PROPERTY_NAME)
					.getNodeValue();
			String roleState = role.getAttributes()
					.getNamedItem(EVENT_ON_STATE).getNodeValue();
			//Logger.info("Child Node Lengths" + role.getChildNodes().getLength());
			NodeList children = role.getChildNodes();
			String acceptState = null;
			String rejectState = null;
			String defaultState = null;
			for (int elems = 0; elems<children.getLength(); elems++){
				if (children.item(elems).getNodeName().equals(TAG_ACCEPT))
					acceptState = children.item(elems).getTextContent();
				else if (children.item(elems).getNodeName().equals(TAG_REJECT))
					rejectState = children.item(elems).getTextContent();
				else if (children.item(elems).getNodeName().equals(TAG_DEFAULT))
					defaultState = children.item(elems).getTextContent();
			}
			ApprovalLogic approval = new ApprovalLogic(roleName, roleState,
					acceptState, rejectState, defaultState);
			approvals.add(approval);
		}

		return approvals;
	}

}
