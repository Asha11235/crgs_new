package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;
import play.libs.XML;
import play.libs.XPath;
import play.mvc.After;
import play.mvc.Router;

import javax.persistence.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.io.StringWriter;
import java.util.*;


/**
 * The Class Form.
 */

@Entity
public class Form extends Model {
/*
 * 	NOW obsoleted!!!!
//	@Required
	@Unique
	public String formId;
*/
    /** The Form Name. */
    @Required
    public String name;

    /** The Form Short name. */
    @Required
    @Unique
    public String shortName;

    /** The Validity of the form in days. */
    @Min(1)
    public Integer validity = 1;

    public FormType type = FormType.DEFAULT;

    /** The version. */
	@Required
	@Min(1)
	public int version = 1;

	/** The XML. */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] xml;

	/** The XML. */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] approvalxml;
	

	public Date lastReceived;
	
	/** The form has multimedia attachments. */  
  	public boolean isMedia;
	
  	public long dataCount;
    
    /** The logics. */
/*    @OneToMany(mappedBy = "sourceForm", cascade = CascadeType.ALL)
    public List<Logic> logics;*/

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    public List<FormStatus> statuses = new ArrayList<FormStatus>();

    /**
     * The Constructor.
     *
     * @param shortName
     *            the form short name
     */
    public Form(String shortName) {
 //       this.logics = new ArrayList<Logic>();
        this.statuses = new ArrayList<FormStatus>();
        this.shortName = shortName;
    }

    /**
     * The Constructor.
     *
     * @param name
     *            the form name
     * @param shortName
     *            the form short name
     */
    public Form(String name, String shortName) {
        this(shortName);
        this.name = name;
    }

    public static Form findByShortName(String shortName) {
    	Logger.info("short name "+shortName);
    	Form form = Form.find("byShortName", shortName).first();
    	return form;
    }

/*    public void addLogic(Logic logic) {
    	logic.sourceForm = this;
    	logic.save(); // this.save();
    	this.logics.add(logic);
    }*/

    public void addStatus(FormStatus formStatus) {
    	formStatus.form = this;
    	formStatus.save(); // this.save();
    	this.statuses.add(formStatus);
    }

/*	public List<Logic> getLogics(FormStatus formStatus, BeneficiaryStatus outcome) {
		return Logic.find("status = ? and (outcome = ? or outcome is null) and source = ?", formStatus, outcome, this).fetch();
	}*/
	
	///////find form by form id/////////////////////
	public static Form findByFormId(String formId) {
		return Form.find("byFormId", formId).first();
	}
	
	/**
	 * return all media URLs for this form
	 * */
	public String getMediaURLs() {
		String urls = "";
		
		if (this.isMedia) {
			java.util.List<String> mediaUrlList = new java.util.ArrayList<String>();
			Document formXml = XML.getDocument(new String(this.xml));
			for(Node valueNode : XPath.selectNodes("//value[@form]", formXml)) {
	    		if(valueNode.hasAttributes()) {
	    			
	    			String mediaType = valueNode.getAttributes().getNamedItem("form").getNodeValue();
	  
	    			String mediaLabel = valueNode.getTextContent();
	    			String mediaFileName = mediaLabel.substring(mediaLabel.lastIndexOf("/") + 1);
	    			
	    			String mediaBaseDir = Play.configuration.getProperty("media.dir", "media");
	    			String mediaSubDir = "";
	    			
	    			if (mediaType.equalsIgnoreCase("video")) {
	    				mediaSubDir = Play.configuration.getProperty("media.dir.video", mediaType);
	    			}
	    			else if (mediaType.equalsIgnoreCase("image")) {
	    				mediaSubDir = Play.configuration.getProperty("media.dir.image", mediaType);
	    			}else if (mediaType.equalsIgnoreCase("audio")) {
	    				mediaSubDir = Play.configuration.getProperty("media.dir.audio", mediaType);
	    			}
	    			
	    			String fileRelativePath = mediaBaseDir + File.separator + mediaSubDir + File.separator + mediaFileName;
	    			
	    			java.util.Map<String, Object> qString = new java.util.TreeMap<String, Object>();
	    			qString.put("sourceFileName", fileRelativePath);
	    			try {
	    				mediaUrlList.add(new org.apache.commons.codec.net.URLCodec().decode(Router.getFullUrl("Mobile.getFileAsBinary", qString)));
					} catch (org.apache.commons.codec.DecoderException e) {
						e.printStackTrace();
					}
	    		}	
			}	
			urls = StringUtils.join(mediaUrlList, " ");
		}

		return urls;
	}

	/**
	 * return all media information for this form as java Map
	 * */
	public Map<String, String> getMediaFileInfo() {
		Map<String, String> mediaFileInfo = new LinkedHashMap<String, String>();
		
		if (this.isMedia) {
			Document formXml = XML.getDocument(new String(this.xml));
			for(Node valueNode : XPath.selectNodes("//value[@form]", formXml)) {
	    		if(valueNode.hasAttributes()) {
	    			String mediaType = valueNode.getAttributes().getNamedItem("form").getNodeValue();
	  
	    			String mediaLabel = valueNode.getTextContent();
	    			String mediaFileName = mediaLabel.substring(mediaLabel.lastIndexOf("/") + 1);
	    			
	    			mediaFileInfo.put(mediaFileName, mediaType);
	    		}	
			}	
		}

		return mediaFileInfo;
	}
	
	/**
	 * Sync XML - update 'data' node 'id' attribute value by DB id
	 * */
	public void syncXmlWithDBId() {
		if (this != null && this.xml != null) {
			Document formXml = XML.getDocument(new String(this.xml));
			Node dataNode = XPath.selectNode("//data", formXml);
			if (dataNode == null ) {
				Logger.error("'data' node missing in XForm");
				return;
			}
			
			dataNode.getAttributes().getNamedItem("id").setNodeValue(""+this.id);
			
			this.xml = getStringFromDocument(formXml).getBytes();
			this.save();
			Logger.info("Form XML updated -form id sync with DB");
		}
	}
    
	//method to convert Document to String
    private String getStringFromDocument(Document doc) {
        try {
           DOMSource domSource = new DOMSource(doc);
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.transform(domSource, result);
           return writer.toString();
        } catch(TransformerException ex) {
           ex.printStackTrace();
           return null;
        }
    }	
}
