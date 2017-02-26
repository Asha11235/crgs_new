package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.DefaultEditorKit.CopyAction;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import play.Play;
import play.libs.Files;
import play.libs.IO;
import play.libs.XML;
import play.libs.XPath;

public class XMLSpliter {
	
	public static Map<String, String> getXmlList(File xmlFile) throws IOException {
		Map<String, String> xmlMap = new HashMap<String, String>();
				
		Map<String, Integer> groupNodeMap = new HashMap<String, Integer>();
		Document xmlDom = XML.getDocument(xmlFile);	
		
		String parentNodeName = "//data";
    	int iterator = 1;
    	
    	//parse for group nodes
		for (Node node : getChildNodes(parentNodeName, iterator, xmlDom)) {
			String nodeName = node.getNodeName();
			if(getChildNodes(parentNodeName+"/"+nodeName, 1, xmlDom).size() > 0) {
				Integer counter = groupNodeMap.get(nodeName);
				if (counter == null) {
					counter = 0;
				}
				groupNodeMap.put(nodeName, ++counter);
			}
		}
		
		for ( Entry<String, Integer> e : groupNodeMap.entrySet()) {
			String nameGroupNode = e.getKey();
			Integer iteratorGroupNode = e.getValue();
			
			for (int i = 1; i <= iteratorGroupNode; i++ ) {
				//single data
				File file = new File (Play.applicationPath + "/tmp/"+"dummy.xml");
				if(!file.exists()) {
					if(file.createNewFile()) {
						//System.out.println("file created!!!");
					}
					else {
						System.out.println("fialed");
					}
				}
				
				FileUtils.copyFile(xmlFile, file);

				Document newDoc = XML.getDocument(file);
				
				Map<String, Integer> countGroupNodeTraverse = new HashMap<String, Integer>();
				//parse and create customized XML
				for (Node node : getChildNodes(parentNodeName, iterator, newDoc)) {
					String nodeName = node.getNodeName();
					int countChild = getChildNodes(parentNodeName+"/"+nodeName, 1, xmlDom).size();
					if (countChild > 0) {//group node
						if (nameGroupNode.equals(nodeName)) {
							Integer countTraverse = countGroupNodeTraverse.get(nodeName);
							if (countTraverse == null) {
								countTraverse = 0;
							}
							countGroupNodeTraverse.put(nodeName, ++countTraverse);
							
							if(countTraverse != i){
								node.getParentNode().removeChild(node);
							}
						}
						else {
							node.getParentNode().removeChild(node);
						}
					}
				}
				
				file.delete();
				
				xmlMap.put(nameGroupNode + ">" + i, XML.serialize(newDoc));
			}
		}

		if (groupNodeMap.isEmpty()) {
			xmlMap.put("noGroup", XML.serialize(xmlDom));
		}
		
		return xmlMap;
	}

	private static List<Node> getChildNodes(String parentNodeName, int iterator, Document xmlDom) {
		return XPath.selectNodes(parentNodeName + "[" + iterator + "]" +"/*", xmlDom);
	}
	
}
