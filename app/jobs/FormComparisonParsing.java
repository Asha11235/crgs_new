package jobs;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.fileupload.InvalidFileNameException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.Logger;
import play.jobs.Job;
import play.libs.XML;
import utils.ApprovalData;
import utils.ApprovalLogic;
import utils.ApprovalState;
import utils.Comparison;
import utils.ComparisonList;
import utils.Field;
import controllers.Application;

/**
 * @author Sadat Sakif Ahmed
 * */
public class FormComparisonParsing extends Job {

	private final static String ERROR_INVALID_FILE = "The file has to be an XML file. Please check if the correct file has been uploaded";

	/** List of form elements */
	private final static String TAG_FORM = "form";
	private final static String TAG_COMPARE = "compare";
	private final static String TAG_REFERENCE = "reference";
	private final static String TAG_FIELD_LIST = "fieldlist";
	private final static String TAG_FIELD = "field";
	private final static String TAG_NUMERIC = "numeric";
	private final static String TAG_STRING = "string";
	private final static String TAG_MATCH = "match";
	private final static String TAG_LENGTH = "length";

	private final static String PROPERTY_NAME = "name";
	private final static String PROPERTY_TITLE = "title";
	private final static String PROPERTY_FROM = "from";
	private final static String PROPERTY_TO = "to";

	/** End list of form elements */

	private Document xmlDoc;

	private ArrayList<Comparison> compList;

	/**
	 * Create a class to parse out the form comparison configuration
	 * 
	 * @param file
	 *            - The form comparison config file. The file type has to be
	 *            .xml
	 * */
	public FormComparisonParsing(File file) {
		if (!file.getName().endsWith(".xml"))
			throw new InvalidFileNameException(file.getName(),
					ERROR_INVALID_FILE);
		xmlDoc = XML.getDocument(file);
		validateDocument();
		Logger.info(compList.toString());
	}

	private void validateDocument() {
		NodeList formNode = xmlDoc.getElementsByTagName(TAG_FORM);
		String formName = formNode.item(0).getAttributes()
				.getNamedItem(PROPERTY_NAME).getNodeValue();

		NodeList comparisons = xmlDoc.getElementsByTagName(TAG_COMPARE);
		ArrayList<Comparison> comparisonDatas = getComparisonConfigurations(
				formName, comparisons);
		compList = comparisonDatas;
		
		if (Application.comparison.getComparison(formName) == null){
			for (Comparison c : comparisonDatas)
			Application.comparison.addComparison(c);
		}
	}

	private ArrayList<Comparison> getComparisonConfigurations(String formName,
			NodeList comparisons) {
		ArrayList<Comparison> comps = new ArrayList<Comparison>();
		for (int i = 0; i < comparisons.getLength(); i++) {
			if (comparisons.item(i).getNodeName().equals(TAG_COMPARE)) {
				comps.add(getComparisonElement(formName, comparisons.item(i)));
			}
		}
		return comps;
	}

	private Comparison getComparisonElement(String formName, Node node) {
		String compToForm = node.getAttributes().getNamedItem(PROPERTY_TO).getNodeValue();
		NodeList nodes = node.getChildNodes();
		Comparison comp = new Comparison(formName, compToForm, null);
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeName().startsWith("#"))
				continue;

				if (nodes.item(i).getNodeName().equals(TAG_FIELD_LIST)){
				NodeList nn = nodes.item(i).getChildNodes();
				for (int j = 0; j < nn.getLength(); j++) {
					if (nn.item(j).getNodeName().equals(TAG_FIELD)){
					Field compElement = getFieldinfo(nn.item(j));
					//Logger.info(compElement.toString());
					comp.addField(compElement);
					}
				}
				}
				if (nodes.item(i).getNodeName().equals(TAG_REFERENCE)){
				Node n = nodes.item(i);
				String fromField = n.getAttributes()
						.getNamedItem(PROPERTY_FROM).getNodeValue();
				String toField = n.getAttributes().getNamedItem(PROPERTY_TO)
						.getNodeValue();
				String titleField = null;
				try{
				titleField = n.getAttributes().getNamedItem(PROPERTY_TITLE)
						.getNodeValue();
				}catch(NullPointerException npe){}
				try {
					Field f = new Field(fromField, toField, titleField, 
							Field.TYPE_REFERENCE);
					comp.setReference(f);
				} catch (Exception e) {
				}
			}
		}
		return comp;
	}

	private Field getFieldinfo(Node fieldNode) {
		String fromField = fieldNode.getAttributes()
				.getNamedItem(PROPERTY_FROM).getNodeValue();
		String toField = fieldNode.getAttributes().getNamedItem(PROPERTY_TO)
				.getNodeValue();
		String titleField = fieldNode.getAttributes().getNamedItem(PROPERTY_TITLE)
				.getNodeValue();
		int comparisonType = 0;
		NodeList nodes = fieldNode.getChildNodes();
		for (int i=0; i< nodes.getLength(); i++){
			if (nodes.item(i).getNodeName().equals(TAG_NUMERIC))	
			comparisonType = Field.TYPE_NUMERIC;
				if (nodes.item(i).getNodeName().equals(TAG_STRING)){
				NodeList n = nodes.item(i).getChildNodes();
				for (int j=0; j<n.getLength(); j++){
						if (n.item(j).getNodeName().equals(TAG_MATCH))
						comparisonType = Field.TYPE_STRING_MATCH;

						if (n.item(j).getNodeName().equals(TAG_LENGTH))
						comparisonType = Field.TYPE_STRING_LENGTH;
				}
			}
		}
		try {
			Field f = new Field(fromField, toField, titleField, comparisonType);
			return f;
		} catch (Exception e) {}
		return null;
	}

}