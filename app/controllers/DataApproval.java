package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.ApprovalHistory;
import models.Data;
import models.Form;
import models.Role;
import models.UnitData;
import models.User;
import play.Logger;
import play.Play;
import utils.Approval;
import utils.Comparison;
import utils.Field;
import controllers.deadbolt.ExternalRestrictions;

public class DataApproval extends Controller {
	
	public static final int TYPE_REFERENCE = 111;
	
	public static final int TYPE_NUMERIC = 112;
	public static final int TYPE_STRING_MATCH = 113;
	public static final int TYPE_STRING_LENGTH = 114;
	
	private static List<User> assignedEnumerators(long loggedInUserId, long userRoleId){
		List<User> assignEnumerators = new ArrayList<User>();		
		//DS
		if (userRoleId == 2) {
			List<User> fsUserList = User.findFsByDsUser(loggedInUserId);			
			for (User fsUser : fsUserList) {
				List<User> enumeratorUserList = User.findEnumeratorByFsUser(fsUser.id);
				for (User user : enumeratorUserList) {
					assignEnumerators.add(user);
				}
			}
		}
		//FS
		else if (userRoleId == 3) {
			List<User> enumeratorUserList = User.findEnumeratorByFsUser(loggedInUserId);
			for (User user : enumeratorUserList) {
				assignEnumerators.add(user);
			}
		}
		if(assignEnumerators.size() > 0) return assignEnumerators;
		return null;
	}
	
	/*@ExternalRestrictions("Data Approve")*/
	public static void index() {
		User webUser = User.findByName(session.get("username"));
		String userRole = webUser.role.name;
		List<Data> data = new  ArrayList<Data> ();
		int  Status = 0;
		if (webUser.role.id != 1){
		List<User> assign = assignedEnumerators(webUser.id, webUser.role.id);
			data = Data.find("select d from Data d where d.approvalState = ? and d.sender in (:senders)",Status).setParameter("senders", assign).fetch();
		} else {
			data = Data.find("select d from Data d where d.approvalState = ? ",Status).fetch();
		}
		int len = data.size();
		render(data, len , userRole);
	}

	public static void approve() {

		String selectedCheckBox = request.params.get("selectedCheckBox");
		String accept = request.params.get("accept");
		String reject = request.params.get("reject");

		User webUser = User.findByName(session.get("username"));

		String[] selectedData = selectedCheckBox.split(" ");

		if (selectedData.length > 0 && !selectedCheckBox.matches("")) {

			if (accept != null) {
				if (accept.matches("Accept")) {
					int action = Approval.ACTION_DEFAULT;
						action = Approval.ACTION_ACCEPT;
					for (String dataSelected : selectedData) {
						Data data = Data.findById(Long.parseLong(dataSelected));
						int newState = Application.approval.getDataApprovalState(
								Application.approval.getApproval(data.form.shortName), data.id, webUser,
								data.approvalState, action);

						ApprovalHistory history = new ApprovalHistory();

						history.status = newState;
						history.changedBy = webUser;
						history.data = data;
						history.save();
						if (newState != Approval.NULL_VALUE) {
							data.approvalState = newState;
							data.save();
						}
					}
				}
			}

			else if (reject != null) {
				if (reject.matches("Reject")) {
					int action = Approval.ACTION_DEFAULT;
						action = Approval.ACTION_REJECT;
					for (String dataSelected : selectedData) {
						Data data = Data.findById(Long.parseLong(dataSelected));
						int newState = Application.approval.getDataApprovalState(
								Application.approval.getApproval(data.form.shortName), data.id, webUser,
								data.approvalState, action);

						ApprovalHistory history = new ApprovalHistory();

						history.status = newState;
						history.changedBy = webUser;
						history.data = data;
						history.save();
						if (newState != Approval.NULL_VALUE) {
							data.approvalState = newState;
							data.save();
						}
					}
				}
			}
		}
		index();
	}

	@ExternalRestrictions("View Data")
	public static void viewData(Long id) {
		Data data = Data.findById(id);
		notFoundIfNull(data);

		List<UnitData> list = UnitData.find("byData", data).fetch();
		String enableAudioText = Play.configuration
				.getProperty("aggregate.enableAudioText");

		render(data, list, enableAudioText);
	}

	public static void singleDataApprove() {
		String dataId = request.params.get("data_id");
		String accept = request.params.get("accept");
		String reject = request.params.get("reject");
		User webUser = User.findByName(session.get("username"));

		Data data = Data.findById(Long.parseLong(dataId));
		int action = Approval.ACTION_DEFAULT;
		if (accept != null) {
			action = Approval.ACTION_ACCEPT;
		} else if (reject != null) {
			action = Approval.ACTION_REJECT;
		}

		Logger.info("" + action);

		int newState = Application.approval.getDataApprovalState(
				Application.approval.getApproval(data.form.shortName), data.id, webUser,
				data.approvalState, action);

		ApprovalHistory history = new ApprovalHistory();

		history.status = newState;
		history.changedBy = webUser;
		history.data = data;
		history.save();
		if (newState != Approval.NULL_VALUE) {
			data.approvalState = newState;
			data.save();
		}
		index();
	}
	
	public static void comparisonList() {
		Form form = Form.findByShortName("WB_BPD");
		Role role = Role.findByName("FS");
		List<Data> comparisonData = Data.find("byFormAndsenderRole",form,role).fetch();
		Logger.info("list size  " +comparisonData.size());
/*		for(Data data: comparisonData){
			Data comparingData = Data.find("byRespondentId", data.respondentId).first();
	//		if(comparingData!=null)
			if(comparingData.approvalState!=1) comparisonData.remove(data);
		}*/
		render(comparisonData);
	}
	
	public static void comparison(Long id) {
		Data compareData = Data.findById(id);
		Role role = Role.findByName("Enumerator");
		Data comparingData = Data.find("byRespondentIdAndsenderRole", compareData.respondentId,role).first();
		String form1 = compareData.form.shortName;
		String form2 = comparingData.form.shortName;
		
		List<CompareValue> comparedValueList = comparingField(compareData.id, comparingData.id);
		render(comparedValueList,form1,form2,compareData);
	}
	
	public static List<CompareValue> comparingField(Long dataId1, Long dataId2){
		
		ArrayList<Comparison>	cList = Application.comparison.getComparison("WB_BPD");
		Comparison cSingle = null;
		for (Comparison c : cList){
			if (c.compFormName.matches("WB_BPD")){
				cSingle = c;
				break;
			}
		}
		
		ArrayList<Field> fieldList = cSingle.getFields();
//		fieldList.get(0).
		Logger.info(fieldList.toString());
		
		Connection conn = play.db.DB.getConnection();
		Statement stmt = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String qString1 = "";
		String qString2 = "";
		List<CompareValue> comparedValueList = new ArrayList<CompareValue>();
		try {
			qString1 = "select * from world_bank.tblRegistrationnew where data_id="
					+ dataId1;
			stmt = conn.createStatement();
			rs1 = stmt.executeQuery(qString1);
			rs1.next();

			qString2 = "select * from world_bank.tblRegistrationnew where data_id="
					+ dataId2;
			stmt = conn.createStatement();
			rs2 = stmt.executeQuery(qString2);
			rs2.next();

			for (Field compare : fieldList) {
				CompareValue compareValue = new CompareValue();
				compareValue.compValue1 = rs1.getString(compare.to);
				compareValue.compValue2 = rs2.getString(compare.from);
				compareValue.compTitle = compare.title;
				Logger.info("compare values :"+compareValue.compValue1 + compareValue.compValue2);
				switch (compare.comparisonType) {
				case TYPE_STRING_MATCH:
					compareValue.compResult = compareValue.compValue1
							.matches(compareValue.compValue2) ? "matched"
							: "Not matched";
					break;
				case TYPE_NUMERIC:
					compareValue.compResult = Integer
							.parseInt(compareValue.compValue1) == Integer
							.parseInt(compareValue.compValue2) ? "Number matched"
							: "Number not matched";
				default:
					break;
				}
				comparedValueList.add(compareValue);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comparedValueList;
	}

}