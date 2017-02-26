/*
 * Copyright (C) 2011 mPower Health
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import play.mvc.Util;

/**
 * Data Model - Definition of survey form data.
 */
@Entity
public class Data extends Model {
	
	/**ID to identify questions those are submitted in a single request.*/
	@Required
	public String submissionId;

	/**Tag name of a group question.*/
	public String groupTagName;
	
	public String respondentId;
	
	/**CRGS School principle activity is recorded Here by using this fields */
	public Boolean isVisited;
	public Date resolveDate;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	public SchoolInformation schools;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	public Ngo ngo;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	public Data comparedTo;
	
	/**Iterator for parsing group question.*/
	public int iteration = 1;
	
	public int approvalStatus;
	
	public int approvalState;
	
	/** If farmer will not have any activity then status would be false, otherwise true */
	public Boolean isFarmerActive = true;
	
	/** The form. */
	@Required
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public Form form;

	/** The xml. */
	//@Required
	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] xml;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	public byte[] dataSpecificXml;

	@Required
	@ManyToOne
	public User sender;
	
	@Required
	@ManyToOne
	public Role senderRole;
	
	public Boolean webEntry = false;
	
	/** The items, will not be persist. Will be used in View*/
	@Transient
	public List items;

	/** The received. */
	public Date received;

	public Date startTime;
	public Date endTime;

	public String[] audioData;

	// Extra Data for Dashboard
	public Double latitude;
	public Double longitude;
	public Double accuracy;
	public String image;
	public Boolean isExtracted = false;
	public String webOrMob = "";
	
	
	

	/**
	 * Instantiates a new data.
	 *
	 * @param form the form
	 * @param xml the xml
	 */
	
	
	public Data(Form form, byte[] xml) {
		this.form = form;
		this.xml = xml;
	}
	
	public static List<Data> findByRoleName(Role role){
		
		return Data.find("bySenderRole", role).fetch();
	}
	
	public static List<Data> findByFormId(Form form){
		
		return Data.find("byForm", form).fetch();
	}


	/**
	 * Before insert.
	 */
	@PrePersist
    protected void beforeInsert() {
        this.received = new Date();
        this.form.lastReceived = this.received;
        this.form.dataCount++;
    }

	/**
	 * Before remove.
	 */
	@PreRemove
    protected void beforeRemove() {
        this.form.dataCount--;
    }
	
	public int countData(){
		//Data keyData = Data.findById(id);
		List<Data> data = Data.findAll();
		return data.size();
	}
}
