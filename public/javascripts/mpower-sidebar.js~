function reset(){
    	$("#first").empty();
    	$("#second").empty();
    	$("#third").empty();
    	$("#fourth").empty();
    	
    	$("#issue").empty();
    	$("#response").empty();
}


function drawPie(id, value , complaintextPosition, resolvetextPosition, complainColor, resolveColor){
	var width = 240,height = 240
	        
	    	var angle = (2 * Math.PI * value) / 100;
			  
		    var canvas = d3.select(id).append("svg")
	                       .attr("width", width).attr("height", height);
	  
	        //canvas.append("rect").attr("width", width).attr("height", height);
	                             
		                  
		    var arc1 = d3.svg.arc().innerRadius(0).outerRadius(100)
	                               .startAngle(0).endAngle(angle);
		      
		    var arc2 = d3.svg.arc().innerRadius(0).outerRadius(120)
	                               .startAngle(angle).endAngle(2 * Math.PI);
	  
	         var widthScale = d3.scale.linear().domain([0, 2 * Math.PI]).range([0,100])
		
		    canvas.append("g").attr("transform", "translate(120,120)")
	                            .append("path").attr("d", arc1)
		                        .attr("fill", complainColor);
		
		    canvas.append("g").attr("transform", "translate(120,120)")
	                            .append("path").attr("d", arc2)
		      					.attr("fill", resolveColor);
	  
	  
		    canvas.append("text").text("%" + " Unresolved")
		                  .attr("text-anchor", "left")
		                  .attr("font-size", "15px")
		                  .attr("fill", "#454545")
	                      .attr("transform", 
	                            "translate(130,110) rotate("+ complaintextPosition + ")")
	  
	        canvas.append("text").text("% Resolved")
	                      .style("text-anchor", "end")
		                  .attr("text-anchor", "right")
		                  .attr("font-size", "15px")
		                  .attr("fill", "#FFFFFF")
	                      .attr("transform", 
	                            "translate(110,100) rotate("+ resolvetextPosition + ")")

	}
  	
function drawCircles(value, id){
		
    	var width = 160, height = 160;
    	var angle = (2 * Math.PI * value) / 100;
		  
	    var canvas = d3.select(id).append("svg").attr("width", width).attr("height", height);
	                  
	    var arc1 = d3.svg.arc().innerRadius(65).outerRadius(71).startAngle(0).endAngle(angle);
	      
	    var arc2 = d3.svg.arc().innerRadius(67).outerRadius(69).startAngle(angle).endAngle(2 * Math.PI);
	
	    canvas.append("g").attr("transform", "translate(80,80)").append("path").attr("d", arc1)
	                        .attr("fill", "#FBB414");
	
	    canvas.append("g").attr("transform", "translate(80,80)").append("path").attr("d", arc2)
	      					.attr("fill", "#E0DFDF");
	      
	    canvas.append("text").text(value + "%").attr("x", 85).attr("y", 85)
	                  .attr("text-anchor", "middle")
	                  .attr("font-size", "25px")
	                  .attr("fill", "#818285");

      
}
$("document").ready(function(){
	
	var waterCasueLabel = {waterCause1 : "Bad odor", waterCause2 : "Iron", waterCause3 : "Arsenic / Manganese",
			waterCause4 : "Other"};
	
	var sanitationCauseLabel = {sanitationCause1: "Tap not functional",
		sanitationCause2 : "Source of water/water tap not within the reach", sanitationCause3 : "No water in the toilet", 
		sanitationCause4 : "Toilet lock is not functional", sanitationCause5 : "Toilet door broken/a whole in the door", 
		sanitationCause6 : "No mug, Bucket, Bodna in the toilet", 
		sanitationCause7 : "overflow of stagnant dirty water in the pan/commode", 
		sanitationCause8 : "Stagnant water in the toilet", 
		sanitationCause9 : "No separate bucket for female for throwing napkin/pad", 
		sanitationCause10 : "No Sufficient ventilation and lighting in the toilet", 
		sanitationCause11 : "Insects in the toilet", sanitationCause12 : "Foul smell", sanitationCause13 : "Other"};
	
	var schoolEnvironmentCauseLabel = {schoolEnvironmentCause1 : 'Fear of sexual harassment', 
		schoolEnvironmentCause2 : "Road accident", schoolEnvironmentCause3 : "School is far away from home", 
		schoolEnvironmentCause4 : "Fear of corporal punishment at school",
		schoolEnvironmentCause5 : "Fear of attack by outsiders/others",
		schoolEnvironmentCause6 : "Other"};
	
	var sportsAndRecreationLabel = {sportsAndRecreationCause1 : 'Not enough space to play together', 
			sportsAndRecreationCause2 : 'Boys and Girls are not interested to play simultaneously',
			sportsAndRecreationCause3 : 'Respective teachers are not interested', 
			sportsAndRecreationCause4 : 'Parents are not interest', 
			sportsAndRecreationCause5 : 'Not have much time to play',
			sportsAndRecreationCause6 : 'Do not have common room', 
			sportsAndRecreationCause7 : 'No instruments for girls', sportsAndRecreationCause8 : 'Other'};
		
		
		$.ajax({
			type : "GET",
			url : "/forms/getDashBoardData",
			success: function(data){
				
				var json = JSON.parse(data);
				//console.log(json);
				$("#total_school").html(json.total_school);
				$("#total_boys").html(json.total_boys);
				$("#total_girls").html(json.total_girls);
				$("#total_Toilets").html(json.total_Toilets);
				$("#boys_toilet_ratio").html(json.boys_toilet_ratio);
				$("#girls_toilet_ratio").html(json.girls_toilet_ratio);
				$("#school_forum").html(json.school_forum);
				$("#organized_annual_sports").html(json.organized_annual_sports);
				
				var waterCauseId = 1, sanitationCasueId = 1, schoolEnvironmentCauseId = 1, sportsAndRecreationCauseId = 1;
				
				$.each(json, function(k, v) {
				    if(k in waterCasueLabel){
				    	drawCircles(v, "#waterCause" + waterCauseId);
				    	
				    	var key = "" + k;
				    	$("waterCauseLabel" + waterCauseId).html(waterCasueLabel[key]);
				    	
				    	waterCauseId++;
				    	console.log(k + ' is ' + v);
				    }
				    if(k in sanitationCauseLabel){
				    	drawCircles(v, "#sanitationCause" + sanitationCasueId);
				    	
				    	var key = "" + k;
				    	$("#sanitationCauseLabel" + sanitationCasueId)
				    					.html(sanitationCauseLabel[key]);
				    	sanitationCasueId++;
				    	console.log(k + ' is ' + v);
				    }
				    if(k in schoolEnvironmentCauseLabel){
				    	drawCircles(v, "#schoolEnvironmentCause" + schoolEnvironmentCauseId);
				    	
				    	var key = "" + k;
				    	$("#schoolEnvironmentCauseLabel" + schoolEnvironmentCauseId)
				    					.html(schoolEnvironmentCauseLabel[key]);
				    	schoolEnvironmentCauseId++;
				    	console.log(k + ' is ' + v);
				    }
				    if(k in sportsAndRecreationLabel){
				    	drawCircles(v, "#sportsAndRecreation" + sportsAndRecreationCauseId);
				    	
				    	var key = "" + k;
				    	$("#sportsAndRecreationCauseLabel" + sportsAndRecreationCauseId)
				    					.html(sportsAndRecreationLabel[key]);
				    	
				    	sportsAndRecreationCauseId++;
				    	
				    	console.log(k + " is " + v);
				    }
				    
				    
				    
				});
				var value = json.waterResolved;
		        angle1= -90 + value * 1.8
		        angle2 = 90 + (value - 100) * 1.8;
		        if(value < 10) angle1 = -85;
		        if(value > 90) angle2 = 85;
		    	drawPie("#waterPie",value,angle1,angle2, "#DFDFDF", "#54B0E3");
		    	
		    	var value = json.sanitationIssueResolved;
		        angle1= -90 + value * 1.8
		        angle2 = 90 + (value - 100) * 1.8;
		        if(value < 10) angle1 = -85;
		        if(value > 90) angle2 = 85;
		    	drawPie("#sanitationPie",value,angle1,angle2, "#D6D6D7", "#FF5142");
		    	
		    	var value = json.schoolEnvironmentIssueResolved;
		        angle1= -90 + value * 1.8
		        angle2 = 90 + (value - 100) * 1.8;
		        if(value < 10) angle1 = -85;
		        if(value > 90) angle2 = 85;
		    	drawPie("#schoolEnvironmentPie",value,angle1,angle2, "#D6D6D7", "#54B0E3");
		    	
		    	var value = (json.waterResolved + json.sanitationIssueResolved + json.schoolEnvironmentIssueResolved)/3;
		        angle1= -90 + value * 1.8
		        angle2 = 90 + (value - 100) * 1.8;
		        if(value < 10) angle1 = -85;
		        if(value > 90) angle2 = 85;
		    	drawPie("#totalPie",value,angle1,angle2, "#D6D6D7", "#FF5142");
		    	
		    	console.log(json.waterResolved +" "+ json.sanitationIssueResolved + " " 
		    			+ json.schoolEnvironmentIssueResolved);
			}
		});

});
