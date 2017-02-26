package jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import models.Data;
import models.FormResource;
import models.UnitData;
import models.User;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import play.Logger;
import play.Play;
import play.jobs.Job;
import play.libs.F;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.XML;
import play.libs.XPath;
import utils.CommonUtil;
import utils.FlatDataGenerator;
import utils.WaterAdapter;

public class ExtractData extends Job {

	public enum MediaType {
		AUDIO, VIDEO
	}

	private Data data;
	private int serial;
	private User user;
	private HashMap<String, Integer> nodeMap = new HashMap<String, Integer>();
	private List<String> nodeList = new ArrayList<String>();
	private HashMap<String, Integer> iterationMap = new HashMap<String, Integer>();

	public ExtractData(Data data,User user) {
		this.data = data;
		this.user = user;
		this.serial = 1;// set default serial value to '1'
	}
	
	public ExtractData(Data data) {
		this.data = data;
		this.serial = 1;// set default serial value to '1'
	}

	public ExtractData(Data data, int serial) {
		this.data = data;
		this.serial = serial;
	}

	public ExtractData(Data data, int serial, User user) {
		this.data = data;
		this.serial = serial;
		this.user = user;
	}

	@Override
	public void doJob() throws Exception {

		nodeMap.put("data", 1);
		nodeList.add("data");

		ExtractFromDataXML("/data/");
		
		Logger.info("Data-%d extracted", this.data.id);
    	//if (FlatDataGeneration){
    	Logger.info("Generating Flat Data for dataId : " + data.form.id);
    	//FlatDataGenerator.generateFlat(data);
    	if(data.form.id == 1L){
    		WaterAdapter.insetIntoWater(data, user);
    	}
    	if(data.form.id == 2L){
    		WaterAdapter.insetIntoSanitation(data, user);
    	}
    	if(data.form.id == 3L){
    		WaterAdapter.insetIntoSchoolEnvironment(data, user);
    	}
    	if(data.form.id == 4L){
    		WaterAdapter.insetIntoSchoolSportsRecreation(data, user);
    	}
    	
	}

	private void ExtractFromDataXML(String nodePath) {

		int iterationCount = 0;

		Document dataXml = XML.getDocument(new String(this.data.xml));
		Document formXml = XML.getDocument(new String(this.data.form.xml).trim().replaceFirst("^([\\W]+)<","<"));

		this.data.form.xml = XML.serialize(formXml).getBytes();

		List<Node> nodes = XPath.selectNodes(nodePath + "*", dataXml);

		int serial = this.serial;// member serial

		if (nodes.size() > 0 && nodeList.size() > 0) {
			String buildPath = "";
			String bindPath = "";

			for (Node dataNode : nodes) {

				String nodeName = dataNode.getNodeName();
				String type = new String("");
				String title = new String("");
				String value = new String("");
				String valueVar = new String("");

				if (nodeMap.containsKey(nodeName)) {
					nodeList.add(nodeName);
					nodeMap.put(nodeName, nodeMap.get(nodeName) + 1);

					List<Node> resetnodes = XPath.selectNodes("//"
							+ buildPath() + "*", dataXml);

					if (resetnodes.size() > 0) {
						resetNodes(resetnodes);

					}
				} else {
					nodeMap.put(nodeName, serial);
					nodeList.add(nodeName);
				}

				buildPath = buildPath();
				bindPath = bindPath();
				bindPath = bindPath.substring(0, bindPath.length() - 1);

				List<Node> childNodes = XPath.selectNodes("//" + buildPath
						+ "*", dataXml);

				String subPath = "";
				subPath = buildPath.substring(0, buildPath.length() - 1);

				Node checkNode = XPath.selectNode("//" + subPath, dataXml);

				if (childNodes.size() > 0 && dataNode.hasChildNodes()
						&& checkNode != null) {
					Node bindNode = (XPath.selectNode(
							"//bind[contains(@nodeset, '" + bindPath + "')]",
							formXml));

					if (bindNode != null && bindNode.hasAttributes()) {
						if (bindNode.getAttributes().getNamedItem("type") != null) {
							type = bindNode.getAttributes()
									.getNamedItem("type").getNodeValue();
						}
					}

					title = XPath.selectText("//text[contains(@id, '"
							+ bindPath + ":label')]/value", formXml);

					value = XPath.selectText("//" + subPath, dataXml);

					valueVar = value;

					UnitData unitData = null;

					/*if (!iterationMap.containsKey(subPath)) {
						iterationMap.put(subPath, nodeMap.get(nodeName));
					} else {
						iterationMap.put(subPath, 0);
					}

					iterationCount = iterationMap.containsKey(subPath) ? iterationMap
							.get(subPath) : 0;*/
					/*
					 * Logger.debug("bindPath:" + bindPath + " title: " + title
					 * + " nodeName:" + nodeName + " iterationCount: " +
					 * iterationCount + " subPath:" + subPath);
					 */
					unitData = makeUnitData(bindPath, title, type, value,
							valueVar/*, iterationCount*/);

					if (unitData != null) {
						unitData.save();
					}

					buildPath = buildPath();
					if (nodeList.size() > 0) {
						ExtractFromDataXML(buildPath);
					}

				}

				else if (checkNode != null) {

					Node bindNode = (XPath.selectNode(
							"//bind[contains(@nodeset, '" + bindPath + "')]",
							formXml));

					if (bindNode != null && bindNode.hasAttributes()) {
						if (bindNode.getAttributes().getNamedItem("type") != null) {
							type = bindNode.getAttributes()
									.getNamedItem("type").getNodeValue();
						}
					}

					title = XPath.selectText("//text[contains(@id, '"
							+ bindPath + ":label')]/value", formXml);

					value = XPath.selectText("//" + subPath, dataXml);

					valueVar = value;


					UnitData unitData = null;
					String parentSubPath = subPath.substring(0,
							subPath.lastIndexOf("/"));
					/*iterationCount = iterationMap.containsKey(parentSubPath) ? iterationMap
							.get(parentSubPath) : 0;*/
					/*
					 * Logger.debug("bindPath:" + bindPath + " parentSubPath:" +
					 * parentSubPath + " title: " + title + " nodeName:" +
					 * nodeName + " iterationCount: " + iterationCount +
					 * " subPath:" + subPath);
					 */
					unitData = makeUnitData(bindPath, title, type, value,
							valueVar/*, iterationCount*/);

					if (unitData != null) {
						unitData.save();
					}
				}

				if (nodeList.size() > 0)
					nodeList.remove(nodeList.size() - 1);

			}
			if (nodeList.size() > 0) {
				nodeList.remove(nodeList.size() - 1);
				buildPath = buildPath();
				ExtractFromDataXML(buildPath);
			}

		}

	}

	private void resetNodes(List<Node> resetNodes) {

		for (Node node : resetNodes) {
			String nodeName = node.getNodeName();

			if (nodeMap.containsKey(nodeName))
				nodeMap.put(nodeName, 0);
		}
	}

	private String buildPath() {
		String buildPath = "";
		Integer nodeIndex;

		for (String nodeName : nodeList) {

			if (nodeMap.containsKey(nodeName)) {
				nodeIndex = nodeMap.get(nodeName);
				buildPath = buildPath + nodeName + "[" + nodeIndex + "]" + "/";
			}

		}
		return buildPath;
	}

	private String bindPath() {
		String bindPath = "";

		for (String nodeName : nodeList) {

			if (nodeMap.containsKey(nodeName)) {
				bindPath = bindPath + nodeName + "/";
			}
		}
		return bindPath;
	}

	/**
	 * create and save UnitData instance
	 * */
	private UnitData makeUnitData(String nodeId, String title, String type,
			String value, String valueVar/*, int itCount*/) {
		Document formXml = XML.getDocument(new String(this.data.form.xml));
		Document dataXml = XML.getDocument(new String(this.data.xml));
		
		int iterationCount;
		
//		Logger.debug("node Id "+ nodeId);
		
		if (iterationMap.containsKey(nodeId)) {
			iterationMap.put(nodeId, iterationMap.get(nodeId)+1);
		} else {
			iterationMap.put(nodeId, 1);
		}

	//	FormResource formResource = FormResource.find("select fr from FormResource fr where fr.form=? and fr.nodePath=?", data.form, nodeId).first();
//		FormResource formResource = FormResource.find("byFormAndNodePath", data.form, nodeId).first();
		
//		Logger.info("formResource  " + formResource.nodePath);
		/*iterationCount = iterationMap.containsKey(nodeId) ? iterationMap
				.get(nodeId) : 0;*/
		
		iterationCount = iterationMap.get(nodeId);
		
//		Logger.debug("  count "+ iterationCount);

				/////////////////////........////////////////////
		if (nodeId.contains("i_start_time") || nodeId.contains("i_end_time")) {
			return null;
		}

		String extraValue = null;
		if (value != null && value.length() > 0) {
			// Binary files like Audio, Video, Images
			if (type.equals("binary")) {
				String extension = CommonUtil.getExtension(value);
				if (extension.equals("jpg")) {
					type = "image";
				} else if (extension.equals("3gp") || extension.equals("mp4")) {
					type = "video";
					if (extension.equals("3gp")) {
						value = convert(value, "mp4");
					}
				} else if (extension.equals("3gpp") || extension.equals("3ga")
						|| extension.equals("m4a") || extension.equals("amr")) {
					type = "audio";
					if (extension.equals("amr")) {
						value = convert(value, "mp3", MediaType.AUDIO);
					}
				}
			}
			// Location (Latitude and Longitude)
			else if (type.equals("geopoint")) {
				type = "gmap";
				String[] geo = value.split(" ");
				value = geo[0] + "," + geo[1];

/*				HttpResponse res = WS.url(
						"http://maps.googleapis.com/maps/api/geocode/xml?latlng="
								+ value + "&sensor=false").get();
				if (res.success() ) {
					Document xml = res.getXml();
					extraValue = XPath.selectText(
							"GeocodeResponse/result[1]/formatted_address", xml);
				};*/
								
				F.Promise<WS.HttpResponse> res = WS.url("http://maps.googleapis.com/maps/api/geocode/xml?latlng="
								+ value + "&sensor=false").getAsync();
				F.Promise<List<WS.HttpResponse>> promises = F.Promise.waitAll(res);
				//List<WS.HttpResponse> httpResponses = await(promises);
				
				try {
					if (promises.get().get(0) != null ) {
						Document xml;
						try {
							xml = promises.get().get(0).getXml();
							extraValue = XPath.selectText(
									"GeocodeResponse/result[1]/formatted_address", xml);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Checkbox
			else if (type.equals("select")) {
				String[] tokens = value.split(" ");
				value = "";
				String selectedValues = "";
				String selectedValueVar = "";
				UnitData unitData = null;
				String node_token = "";
				
				String nodePath = nodeId.substring(5);
				FormResource formResource = FormResource.find("byNodePathAndform", nodePath, data.form).first();
				for (String token : tokens) {
					
					
    				//value += XPath.selectText("//text[contains(@id, '/data/" + nodeId + ":" + token + "')]/value", formXml) + "\n";
    				

					value = XPath.selectText("//text[contains(@id, '" + nodeId
							+ ":" + token + "')]/value", formXml)
							+ "\n";
					
					selectedValueVar += token+",";
					selectedValues += value+",";
					node_token = nodeId + "_" + token;
					
					unitData = new UnitData(data, this.data.form,
							 node_token, title, type, token,
							value, extraValue,iterationCount/*, itCount*/,formResource);

					if (unitData != null)
						unitData.save();
				}
				
				//Logger.info("selected values : " +  selectedValues);
				
				String values = selectedValues;
				String valueVars = selectedValueVar;
				
				if (selectedValues.length() > 0 && selectedValues.charAt(selectedValues.length()-1)==',') {
					values = selectedValues.substring(0, selectedValues.length()-1);
				}
				
				if (selectedValueVar.length() > 0 && selectedValueVar.charAt(selectedValueVar.length()-1)==',') {
					valueVars = selectedValueVar.substring(0, selectedValueVar.length()-1);
				}
				
				
				//Logger.info("after removing last char: " + values);
				
				unitData = new UnitData(data, this.data.form,
						 nodeId, title, type, valueVars,
						 values, extraValue,iterationCount/*, itCount*/,formResource);

				if (unitData != null)
					unitData.save();

				return null;
			}
			// Radio Button
			else if (type.equals("select1")) {
				value = XPath.selectText("//text[contains(@id, '" + nodeId
						+ ":" + value + "')]/value", formXml);
				String ref = getRefference(nodeId);
				if (value == null) {// for cascade select
					value = XPath.selectText("//text[contains(@id, '" + ref
							+ "')]/value", formXml);
					title = XPath.selectText("//select1[contains(@ref,'"
							+ nodeId + "')]/label", formXml);
				}
			}
		}

		String nodePath = nodeId.substring(5);
		FormResource formResource = FormResource.find("byNodePathAndform", nodePath,data.form).first();
		
		return new UnitData(data, this.data.form, nodeId, title,
				type, valueVar, value, extraValue, iterationCount, formResource).save();
		// return null;//new UnitData(this.data, this.data.form,
		// this.data.gkvillage, nodeId, title, type, valueVar, value,
		// extraValue).save();

	}

	/**
	 * return attribute 'id' of 'text' node in formXML, for a particular node in
	 * dataXml
	 * 
	 * @param nodeId
	 *            the node name of a node in dataXml
	 * @return the id attribute of 'text' node in formXml
	 */
	public String getRefference(String nodeId) {
		Document formXml = XML.getDocument(new String(this.data.form.xml));
		Document dataXml = XML.getDocument(new String(this.data.xml));
		String text_id = null;

		java.util.List<org.w3c.dom.Node> items = XPath.selectNodes(
				"//instance[contains(@id, '" + nodeId + "')]/root/item",
				formXml);
		for (org.w3c.dom.Node item : items) {
			String reff = null;
			for (org.w3c.dom.Node childNode : XPath.selectNodes("*", item)) {
				if (!childNode.getNodeName().equals("name")
						&& !childNode.getNodeName().equals("itextId")) {
					// check equality of form node value with data node value
					if (childNode.getTextContent().equals(
							XPath.selectText("//" + childNode.getNodeName(),
									dataXml))) {
						continue;
					}
					break;
				}
				// child node 'name' must appeared at last position into
				// 'instance' node in formXml
				else if (childNode.getNodeName().equals("name")
						&& childNode.getTextContent().equals(
								XPath.selectText("//" + nodeId, dataXml))) {
					reff = XPath.selectText("itextId", item);
				}
			}
			if (reff != null) {
				text_id = reff;
				break;
			}
		}

		return text_id;
	}

	private String convert(String fileName, String extension) {
		return convert(fileName, extension, MediaType.VIDEO);
	}

	private String convert(String fileName, String extension, MediaType type) {
		// Get the default upload directory from application.
		String path = Play.applicationPath.getAbsolutePath()
				+ File.separator
				+ Play.configuration.getProperty("aggregate.uploadDir",
						"uploads") + File.separator;
		String oldFileName = fileName;
		fileName = CommonUtil.getName(oldFileName) + "_cc." + extension;

		if (!new File(path + fileName).exists()) {
			Runtime runtime = Runtime.getRuntime();
			Process process = null;
			try {
				// `libx264-baseline.ffpreset` file must be available in the
				// `$HOME/.ffmpeg/` folder or comment out the following line
				// Don't run from eclipse, video conversion will not work
				String[] cmdArray;
				if (type == MediaType.AUDIO) {
					cmdArray = new String[] { "ffmpeg", "-i",
							path + oldFileName, "-ar 22050", path + fileName };
				} else {
					cmdArray = new String[] { "ffmpeg", "-i",
							path + oldFileName, "-vcodec libx264",
							"-vpre baseline", "-crf 24", "-g 25",
							"-acodec libfaac", "-ab 192k", "-ar 44100",
							path + fileName };
				}
				String cmd = StringUtils.join(cmdArray, " ");
				process = runtime.exec(cmd);

				// Wait for ffmpeg to complete
				try {
					process.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (process.exitValue() != 0) {
					// Logger.info(cmd);
					BufferedReader errorReader = new BufferedReader(
							new InputStreamReader(process.getErrorStream()));
					System.out.println("Error:  " + errorReader.readLine());
					Logger.info(
							"Error - %d, ffmpeg conversion of %s to %s was unsuccesful",
							process.exitValue(), path + oldFileName, path
									+ fileName);
				} else {
					Logger.info("File saved in %s", path);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Logger.info("ffmpeg conversion could not start");
			} finally {
				if (process != null) {
					process.destroy();
				}
			}
		}
		return fileName;
	}
}
