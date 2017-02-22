$("document").ready(function(){
	
	loadData(null, null, null, null, null, null, null);
	
	$(".SEARCH").on("change", function() {
		
		if(typeof $(this).find(":selected").attr("value") == "undefined"){
			return;
		}
		var divisionId = $('#division').find(":selected").attr("value");
		
		var districtId = $('#ngo\\.geoDistrict\\.id').find(":selected").attr("value");
		
		var upazillaId = $('#ngo\\.geoUpazilla\\.id').find(":selected").attr("value");
		
		var schoolId = $('#school').find(":selected").attr("value");
		
		var studentType = $('#schoolType').find(":selected").attr("value");
		
		
		console.log("divisionId : "+ divisionId + " districtId : " + districtId + " UpazillaId : "+ upazillaId
				+ " schoolId : "+ schoolId + " schoolType : " + schoolType);
		loadData(divisionId,districtId,upazillaId,schoolId, studentType, null, null);
	});
	
	
	/**
	 * return data from successful AJAX request 
	 * */
	function loadData(divisionId,districtId,upazillaId,schoolId, studentType, startDate, endDate) {
		var formId = $("#formId").val();
		return $.ajax({
				type: "GET",
              	url:  "/reports/loadReport",
				data : {
					formId : formId,
					divisionId : divisionId,
					districtId : districtId,
					upazillaId : upazillaId,
					schoolId : schoolId,
					studentType : studentType,
					startDate : startDate,
					endDate : endDate
				},
				
				success: function(data) {
					var json = JSON.parse(data);
					if(formId == 1){
						console.log("Water form");
						setWaterData(data);
					}
					else if(formId == 2){
						console.log("Sanitation form");
						setSanitation(data);
					}
					else if(formId == 3){
						console.log("School Environment form");
						setSchoolEnvironment(data);
					}
					else if(formId == 4){
						console.log("Sports form");
						setSportsAndRecreation(data);
					}
                }
			
			}
		);
	}
	
	
	function setWaterData(data){
		reset();
		var json = JSON.parse(data);
		
		
		
		$("#boys").html(json.boys);$("#girls").html(json.girls);$("#boys_Plus_girls").html(json.boys + json.girls);
		
		$("#total_school").html(json.total_school);
		
		$("#is_informed_authority_water_prob_boys").html(json.is_informed_authority_water_prob_boys);
		$("#is_informed_authority_water_prob_girls").html(json.is_informed_authority_water_prob_girls);
		$("#is_informed_authority_water_prob_boys_Plus_is_informed_authority_water_prob_girls")
			.html(json.is_informed_authority_water_prob_boys + json.is_informed_authority_water_prob_girls);
		//for boys section
		$("#total_water_source_boys").html(json.total_water_source_boys);
		$("#total_active_water_boys").html(json.total_active_water_boys);
		$("#total_water_source_boys_Minus_total_active_water_boys")
				.html(json.total_water_source_boys - json.total_active_water_boys);
		$("#total_potable_water_boys").html(json.total_potable_water_boys);
		//for girls section
		$("#total_water_source_girls").html(json.total_water_source_girls);
		$("#total_active_water_girls").html(json.total_active_water_girls);
		$("#total_water_source_girls_Minus_total_active_water_girls")
				.html(json.total_water_source_girls - json.total_active_water_girls);
		$("#total_potable_water_girls").html(json.total_potable_water_girls);
		
		drawCircles(json.causeOne, "#first");drawCircles(json.causeTwo, "#second");
    	drawCircles(json.causeThree, "#third");drawCircles(json.causeFour,"#fourth");
		drawIssue([json.issueOne, json.issueTwo, 
		           json.issueThree, json.issueFour,
		           json.issueFive], "#issue");
		
		drawResponse([json.solved_within_4_7_days,json.solved_within_2_3_days,
		              json.solved_within_8_30_days,json.more_than_30_days,
		              json.no_measure_till_now,json.instantly_Within_1_day,
		              json.not_known], "#response");
		
	}
	
	
	
	function setSanitation(data){
		reset();
		var json = JSON.parse(data);
		
		$("#boys").html(json.boys);$("#girls").html(json.girls);$("#boysPlusgirls").html(json.boys + json.girls);
		
		$("#total_school").html(json.total_school);
		
		$("#is_sanitation_prob_informed_boys").html(json.is_sanitation_prob_informed_boys);
		$("#is_sanitation_prob_informed_girls").html(json.is_sanitation_prob_informed_girls);
		$("#is_sanitation_prob_informed_boysPlusis_sanitation_prob_informed_girls")
			.html(json.is_sanitation_prob_informed_boys + json.is_sanitation_prob_informed_girls);
		
		
		$("#total_activeOrInAcitve_toilet_boys").html(json.total_activeOrInAcitve_toilet_boys);
		$("#total_activeOrInAcitve_toilet_girls").html(json.total_activeOrInAcitve_toilet_girls);
		
		
		causeOfToilet([json.cause1,json.cause2,json.cause3,json.cause4,json.cause5,
		               json.cause6,json.cause7,json.cause8,json.cause9,json.cause10,json.cause11
		               ,json.cause12,json.cause13],"#unUsableToilet");
		
		drawIssue([json.issue1,json.issue2,json.issue3,json.issue4,json.issue5], "#issue");
		
		drawResponse([json.solved_within_4_7_days,json.solved_within_2_3_days,
		              json.solved_within_8_30_days, json.more_than_30_days,
		              json.no_measure_till_now, json.instantly_Within_1_day, 
		              json.not_known], "#response");
		console.log(json)

	}
	
	function setSchoolEnvironment(data){
		reset();
		var json = JSON.parse(data);
		$("#boys").html(json.boys);$("#girls").html(json.girls);$("#boysPlusgirls").html(json.boys + json.girls);
		
		$("#total_school").html(json.total_school);
		
		$("#sufficient_seat_boys").html(json.sufficient_seat_boys);
		$("#sufficient_seat_girls").html(json.sufficient_seat_girls);
		
		$("#scareSafe_boys").html(json.scareSafe_boys);
		$("#scareSafe_girls").html(json.scareSafe_girls);
		
		$("#complained_SchoolEnvironment_boys").html(json.complained_SchoolEnvironment_boys);
		$("#complained_SchoolEnvironment_girls").html(json.complained_SchoolEnvironment_girls);
		$("#complained_SchoolEnvironment_boysPluscomplained_SchoolEnvironment_girls")
			.html(json.complained_SchoolEnvironment_boys + json.complained_SchoolEnvironment_girls);
		
		
		drawCircles(json.notSecure1, "#notSecure1");drawCircles(json.notSecure2, "#notSecure2");
		drawCircles(json.notSecure3, "#notSecure3");drawCircles(json.notSecure4, "#notSecure4");
		drawCircles(json.notSecure5, "#notSecure5");
    	
    	drawCircles(json.food1, "#food1");drawCircles(json.food2, "#food2");drawCircles(json.food3, "#food3");
    	drawCircles(json.food4, "#food4");drawCircles(json.food5, "#food5");
    	
    	drawIssue([json.issue1,json.issue2,json.issue3,json.issue4,json.issue5], "#issue");
    	
    	drawResponse([json.solved_within_4_7_days,json.solved_within_2_3_days,
		              json.solved_within_8_30_days, json.more_than_30_days,
		              json.no_measure_till_now, json.instantly_Within_1_day, 
		              json.not_known], "#response");
		
		console.log(json);
	}
	
	function setSportsAndRecreation(data){
		reset();
		var json = JSON.parse(data);
		
		$("#boys").html(json.boys);$("#girls").html(json.girls);$("#boysPlusgirls").html(json.boys + json.girls);
		
		$("#total_school").html(json.total_school);
		
		$("#instrumentUsable_boys").html(json.instrumentUsable_boys); 
		$("#instrumentUsable_girls").html(json.instrumentUsable_girls);
		$("#instrumentEqualAccess_boys").html(json.instrumentEqualAccess_boys);
		$("#instrumentEqualAccess_girls").html(json.instrumentEqualAccess_girls);
		
		
		
		drawInEqual([json.cause1, json.cause2,json.cause3,
		             json.cause4,json.cause5,json.cause6,json.cause7,json.cause8],"#inEqual");
		
   	 	drawFacilities([json.facility1,json.facility2,json.facility3,json.facility4,
   	 	                json.facility5,json.facility6, json.facility7], "#facilities");
   	 	
   	 	drawEvent([json.event1,json.event2,json.event3,json.event4,json.event5,json.event6, json.event7], "#event");

	}
	
});
