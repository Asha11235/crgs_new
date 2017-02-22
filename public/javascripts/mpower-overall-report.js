
var xScale, yScale, canvas, indicator = 1;

  function initLineChart(id,width,height,xLabel){
		var margin = {left : 80, right : 300, top : 10, bottom : 70};
		if(id == "#container2"){margin.right = 30}
		console.log("width : " + width + " height : " + height);
	    canvas = d3.select(id).append("svg")
	                   .attr("width", width)
	                   .attr("height", height)
	   
	    canvas.append("rect").attr("width",width).attr("height",height).attr("fill","rgba(50,50,50,0.1)");
	    xScale = d3.scale.ordinal()
	             .domain(xLabel)
	             .rangePoints([margin.left, width - margin.right]);
	    
	    yScale = d3.scale.linear()
	             .domain([0,5])
	             .range([height - margin.bottom,margin.top])
	                  
	
	  
	    var xAxis = d3.svg.axis()
	                   .tickSize(1)
	                   .scale(xScale)
	    var yAxis = d3.svg.axis()
	                   .tickSize(1)
	                   .scale(yScale)
	                   .ticks(5)
	                   .orient("left")
	               
	    canvas.append("g").attr("transform","translate(0,309)").call(xAxis)
	    canvas.append("g").attr("transform","translate(80,30)").call(yAxis)
}
  
function makeXScale(label){
	xScale.domain(label);
}
 
function lineChart(lineData,color){

     var lineFunc = d3.svg.line()
               .x(function(d){return xScale(d.x)})
               .y(function(d){return yScale(d.y) + 30})
               .interpolate("line")
 
       canvas.append("g").attr("transform","translate(0,0)").append("path")
      .attr('d', lineFunc(lineData))
      .attr('stroke', color)
      .attr('stroke-width', 2)
      .attr('fill', 'none');
  }
  
function drawLegend(yPos, color, text){
    canvas.append("rect")
          .attr("width", 10)
          .attr("height", 10)
          .attr("x", 1100 - 280)
          .attr("y", yPos)
          .attr("fill", color)
    canvas.append("text")
          .text(text)
          .attr("x",1100 - 265)
          .attr("y", yPos + 10)
          .attr("font-size", "25px")
	      .attr("fill", "#818285");
}
  
  
  
function enableFirstTab(){
	$("#water").trigger( "click" );
}
  

$("document").ready(function(){
	
	
	firstChartInOverallReport(null, null, null, null, null, null, null);
	secondChartInOverallReport(null, null, null, null, null, null, null, 1, false, false, false, false);
	
	
	$(".SEARCH").on("change", function() {
		
		if(typeof $(this).find(":selected").attr("value") == "undefined"){
			return;
		}
		console.log("overrall report filter data retrieval")
		var divisionId = $('#division').find(":selected").attr("value");
		
		var districtId = $('#ngo\\.geoDistrict\\.id').find(":selected").attr("value");
		
		var upazillaId = $('#ngo\\.geoUpazilla\\.id').find(":selected").attr("value");
		
		var schoolId = $('#school').find(":selected").attr("value");
		
		var studentType = $('#schoolType').find(":selected").attr("value");

		console.log("divisionId : "+ divisionId + " districtId : " + districtId + " UpazillaId : "+ upazillaId
				+ " schoolId : "+ schoolId + " schoolType : " + schoolType);
		firstChartInOverallReport(divisionId,districtId,upazillaId,schoolId, studentType,null,null);
	});
	
	

	function firstChartInOverallReport(divisionId,districtId,upazillaId,schoolId, studentType,startDate,endDate) {
		
		return $.ajax({
				type: "GET",
              	url:  "/reports/firstChartInOverallReport",
				data : {
					divisionId : divisionId,
					districtId : districtId,
					upazillaId : upazillaId,
					schoolId : schoolId,
					studentType : studentType,
					startDate : null,
					endDate : null
				},
				
				success: function(data) {
					  var json = JSON.parse(data);
					
					
					
					  var lineData1 = [{x: "jan'16",y: 3.5}, {x: "feb'16",y: 3}, {x: "march'16",y: 2}, 
					                   {x: "april'16",y: 1}, {x: "may'16",y: 1}, {x: "jun'16",y: 1}];
					  var lineData2 = [{x: "jan'16",y: "1"}, {x: "feb'16",y: "3"}, {x: "march'16",y: "1"}, 
					                   {x: "april'16",y: "4"},{x: "may'16",y: "1"}, {x: "jun'16",y: "3"}];					 
					  var lineData3 = [{x: "jan'16",y: 5}, {x: "feb'16",y: 3}, {x: "march'16",y: 1}, 
					                   {x: "april'16",y: 5}, {x: "may'16",y: 3}, {x: "jun'16",y: 5}];
					  var lineData4 = [{x: "jan'16",y: 4}, {x: "feb'16",y: 3}, {x: "march'16",y: 5}, 
					                   {x: "april'16",y: 1}, {x: "may'16",y: 4}, {x: "jun'16",y: 3}];
					  
					  var index1 = 0, index2 = 0, index3 = 0, index4 = 0;
					  $.each(json, function( key, value ) {
						  if (key < 12){
							  if(key % 2 == 0){lineData1[index1].x = value;}
							  if(key % 2 == 1){lineData1[index1++].y = value;}
						  }else if (key >= 12 && key < 24){
							  if(key % 2 == 0){lineData2[index2].x = value;}
							  if(key % 2 == 1){lineData2[index2++].y = value;}
						  }else if (key >= 24 && key < 36){
							  if(key % 2 == 0){lineData3[index3].x = value;}
							  if(key % 2 == 1){lineData3[index3++].y = value;}
						  }else{
							  if(key % 2 == 0){lineData4[index4].x = value;}
							  if(key % 2 == 1){lineData4[index4++].y = value;}
						  }
						  
					  });
					  
					  
					  $("#container1").empty();
					  initLineChart("#container1",1100,350,[lineData1[0].x, lineData1[1].x,lineData1[2].x,lineData1[3].x,lineData1[4].x,lineData1[5].x])
					  console.log(lineData1);
					  lineChart(lineData1,"green")
					  console.log(lineData2);
					  lineChart(lineData2, "red")
					  console.log(lineData3);
					  lineChart(lineData3,"yellow")
					  console.log(lineData4);
					  lineChart(lineData4,"blue")
						  
					  drawLegend(150, "green", "Water")
					  drawLegend(190, "red", "Sanitation")
					  drawLegend(230, "yellow", "SchoolEnvironment")
					  drawLegend(270, "blue","Sports&Recreation")
					  
					  
                }
			
			}
		);
	}
	
	$("button").click(function(){
		
		$(".btn-danger").addClass("btn-default");
		$(".btn-danger").removeClass("btn-danger");
		$(this).addClass("btn-danger");
		$(this).removeClass("btn-default");
		
		wrapperSecondChart($(this).attr("value"));
	})
	
	
	
	$(".ADD").on("change", function() {
		if(typeof $(this).find(":selected").attr("value") == "undefined"){
			return;
		}
		console.log($(".btn-danger").attr("value"));
		wrapperSecondChart($(".btn-danger").attr("value"));
	});
	
	$(".checkbox").on("change", function() {
		wrapperSecondChart($(".btn-danger").attr("value"));
	});
	
	function wrapperSecondChart(formId){
		console.log("formId : " + formId);
		var divisionId = $("#geoDivisionSecond").find('#division').find(":selected").attr("value");
		
		var districtId = $("#geoDistrictSecond").find('#ngo\\.geoDistrict\\.id').find(":selected").attr("value");
		
		var upazillaId = $("#geoUpazillaSecond").find('#ngo\\.geoUpazilla\\.id').find(":selected").attr("value");
		
		var schoolId = $("#geoSchoolSecond").find('#school').find(":selected").attr("value");
		
		var studentType = $("#schoolTypeSecond").find(":selected").attr("value");

		var divisionFilter = $("#divisionFilter").is(":checked");
		var districtFilter = $("#districtFilter").is(":checked");
		var upazillaFilter = $("#upazillaFilter").is(":checked");
		var schoolFilter = $("#schoolFilter").is(":checked");

		secondChartInOverallReport(divisionId, districtId, upazillaId, schoolId, studentType, null,
				null, formId, divisionFilter, districtFilter, upazillaFilter, schoolFilter);
	}
	
	function secondChartInOverallReport(divisionId, districtId, upazillaId, schoolId, studentType, startDate, endDate,
			formId, divisionFilter, districtFilter, upazillaFilter, schoolFilter) {
		console.log("second chart :::: divisionId : "+ divisionId + " districtId : " + districtId + " UpazillaId : "+ upazillaId
					+ " schoolId : "+ schoolId + " schoolType : " + schoolType + " formId : " + formId);

		return $.ajax({
				type: "GET",
              	url:  "/reports/secondChartInOverallReport",
				data : {
					divisionId : divisionId,
					districtId : districtId,
					upazillaId : upazillaId,
					schoolId : schoolId,
					studentType : studentType,
					startDate : startDate,
					endDate : endDate,
					formId : formId,
					divisionFilter : divisionFilter,
					districtFilter : districtFilter,
					upazillaFilter : upazillaFilter,
					schoolFilter : schoolFilter
				},
				
				success: function(data) {
					
				   	  var json = JSON.parse(data);
					  var lineData1 = [{x: "jan'16",y: 3.5}, {x: "feb'16",y: 3}, {x: "march'16",y: 2}, 
					                   {x: "april'16",y: 1}, {x: "may'16",y: 1}, {x: "jun'16",y: 1}];
					  var lineData2 = [{x: "jan'16",y: "1"}, {x: "feb'16",y: "3"}, {x: "march'16",y: "1"}, 
					                   {x: "april'16",y: "4"},{x: "may'16",y: "1"}, {x: "jun'16",y: "3"}];					 
					  var lineData3 = [{x: "jan'16",y: 5}, {x: "feb'16",y: 3}, {x: "march'16",y: 1}, 
					                   {x: "april'16",y: 5}, {x: "may'16",y: 3}, {x: "jun'16",y: 5}];
					  var lineData4 = [{x: "jan'16",y: 4}, {x: "feb'16",y: 3}, {x: "march'16",y: 5}, 
					                   {x: "april'16",y: 1}, {x: "may'16",y: 4}, {x: "jun'16",y: 3}];
					  
					  var index1 = 0, index2 = 0, index3 = 0, index4 = 0;
					  console.log("i am in 1" + json)
					  $.each(json, function( key, value ) {
						  if (key < 12){
							  if(key % 2 == 0){lineData1[index1].x = value;}
							  if(key % 2 == 1){lineData1[index1++].y = value;}
						  }else if (key >= 12 && key < 24){
							  if(key % 2 == 0){lineData2[index2].x = value;}
							  if(key % 2 == 1){lineData2[index2++].y = value;}
						  }else if (key >= 24 && key < 36){
							  if(key % 2 == 0){lineData3[index3].x = value;}
							  if(key % 2 == 1){lineData3[index3++].y = value;}
						  }else{
							  if(key % 2 == 0){lineData4[index4].x = value;}
							  if(key % 2 == 1){lineData4[index4++].y = value;}
						  }
						  
					  });
					  console.log(divisionFilter + " " + districtFilter + " " + upazillaFilter + " " + schoolFilter)
					  if(divisionFilter == false && districtFilter == false && upazillaFilter == false && schoolFilter == false){
						  $("#container2").empty();
						  initLineChart("#container2",780,350,[lineData1[0].x, lineData1[1].x,lineData1[2].x,lineData1[3].x,lineData1[4].x,lineData1[5].x])
					  }
					  
				
					  
					  if(formId == 1){
						  lineChart(lineData1,"green")
					  }
					  if(formId == 2){
						  lineChart(lineData1, "red")
					  }
					  
					  if(formId == 3){
						  lineChart(lineData1,"yellow")
					  }

					  if(formId == 4){
						  lineChart(lineData1,"blue")
					  }
						  
					  
				}
			
			}
		);
	}
	enableFirstTab();
	
});
