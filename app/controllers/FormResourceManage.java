package controllers;

import java.util.ArrayList;
import java.util.List;

import models.AnswerOptionOfQuestion;
import models.Form;
import models.FormResource;
import models.OptionsInForm;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import play.Logger;
import play.libs.XML;
import play.libs.XPath;

public class FormResourceManage {
	
	public static void FormResource(Form form) {
//		List<FormResource> questionsOfForm = new ArrayList<FormResource>();
		Document formXml = XML.getDocument(new String(form.xml).trim().replaceFirst("^([\\W]+)<","<"));
//		xml.trim().replaceFirst("^([\\W]+)<","<");
		List<Node> rootNodes = XPath.selectNodes("//bind", formXml);
		
		for(Node root: rootNodes) {
    		if(root.hasAttributes()) {
    			String nodeId = root.getAttributes().getNamedItem("nodeset").getNodeValue();
    			
    			nodeId = nodeId.substring(6);
    			Node type = root.getAttributes().getNamedItem("type");
    			
    			String typeValue = "";
    			if (type != null) {
    				typeValue = type.getNodeValue();
    			}
    			
    			String title = XPath.selectText("//text[contains(@id, '/data/" + nodeId + ":label')]/value", formXml);
    			
    			// questionsOfForm.add(new QuestionOfForm(form, nodeId, title, typeValue));
    			FormResource formResource = new FormResource(form, title, nodeId, typeValue).save();
    			
    			if(typeValue.equalsIgnoreCase("select") 
    					|| typeValue.equalsIgnoreCase("select1")) {
    				
    				List<Node> optionNodes = XPath.selectNodes("//value[ ../label[contains(@ref, '/data/" + nodeId + ":')] ]", formXml);
    				for (Node option : optionNodes) {
    					String valueVar = option.getTextContent();
    					String value = XPath.selectText("//text[contains(@id, '" + nodeId
    							+ ":" + valueVar + "')]/value", formXml)
    							+ "\n";
    				//	String label = value;
    			//		options.add(new AnswerOptionOfQuestion(label, value));
    					new OptionsInForm(form, formResource, value, valueVar).save();
    				}
    			}
    		}
		}
		
//		return questionsOfForm;
	}

}
