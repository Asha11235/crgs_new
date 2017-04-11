   function reset(){
    	$("#first").empty();
    	$("#second").empty();
    	$("#third").empty();
    	$("#fourth").empty();
    	
    	$("#issue").empty();
    	$("#response").empty();
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
    
    function drawIssue(dataArray, id){

        console.log(dataArray);
        var width = 1020, height = 500;
        var margin = {left: 240, right : 0, top: 70, bottom: 70}
        var verticalGap = 70;
        var horizontalGap = 10;
        var scaleMultiplier = 7;
        
        var widthScale = d3.scale.linear().domain([0, 100]).range([0,100])
        
        var colorScale = d3.scale.ordinal().domain([0,1,2,3,4])
							.range(["#D7DF21","#F1592A","#0F75BC","#61CCEE","#FCB040"])
							
        var canvas = d3.select(id).append("svg").attr("width",width).attr("height", height);
        
        canvas.append("line").attr("x1", margin.left).attr("y1", height - margin.bottom).attr("x2", width)
               	.attr("y2", height - margin.bottom).attr("stroke-width", 2).attr("stroke", "#A6A7A6");
        
        canvas.append("line").attr("x1", margin.left).attr("y1", height - margin.bottom + 1)
                .attr("x2", margin.left).attr("y2", 5).attr("stroke-width", 2).attr("stroke", "#A6A7A6");
        
        
        
        canvas.selectAll(".issueBar").append("rect").data(dataArray).enter().append("rect").attr("class", "issueBar")
                .attr("width", function(d) { return widthScale(d) * scaleMultiplier;}).attr("height", 30)
                .attr("x", margin.left + 1).attr("y", function(d,i){ return margin.top + i * verticalGap;})
                .attr("fill", function(d,i){return colorScale(i);})
                
        var verticalLabel = ["Application to", "School Authority/SMC",
                            "Verbal Complain:", "School Management Committee",
                            "Verbal Complain:","Head Master/Asst. Head Master",
                            "Verbal Complain:", "Class Teacher",
                            "Other"]
        
        canvas.selectAll(".issueY").append("text")
              .data(verticalLabel).enter().append("text").text(function(d){return d}).attr("class", "issueY")
                .attr("x", 0).attr("y", function(d,i){
                    if(i == verticalLabel.length - 1) return 100 + i * 34;
                    if(i % 2 == 0) return 83 + i * 34;
                    else return 102 + (i - 1) * 34;
                 })
                .attr("text-align", "left")
        
        var horizontalLabel = [1,2,3,4,5,6,7,8,9,10]
        canvas.selectAll(".issueX").append("text").data(horizontalLabel).enter().append("text")
               	 .text(function(d){return ""+ d * 10;}).attr("class", "issueX")
        		 .attr("x", function(d){return (margin.left + 10 * d * scaleMultiplier);})
        		 .attr("y", 450).attr("text-align", "left")
        
        canvas.append("text").text("(Response in %)").attr("x", 550).attr("y", 480)
        
    }
    
    function drawResponse(dataArray, id){
        var width = 1000, height = 500;
        var margin = {left: 160, right : 0, top: 20, bottom: 70}
        var verticalGap = 60;
        var horizontalGap = 10;
        var scaleMultiplier = 8;
        
        var widthScale = d3.scale.linear().domain([0, 100]).range([0,100])
        
        var colorScale = d3.scale.ordinal().domain([0,1,2,3,4,5,6])
							.range(["#D7DF21","#FCB040","#F1592A","#0F75BC","#61CCEE","#C49A6C","#13A89E"])
							
        var canvas = d3.select(id).append("svg").attr("width",width).attr("height", height);
        
        canvas.append("line").attr("x1", margin.left).attr("y1", height - margin.bottom).attr("x2", width)
               	.attr("y2", height - margin.bottom).attr("stroke-width", 2).attr("stroke", "#A6A7A6");
        
        canvas.append("line").attr("x1", margin.left).attr("y1", height - margin.bottom + 1)
                .attr("x2", margin.left).attr("y2", 5).attr("stroke-width", 2).attr("stroke", "#A6A7A6");
        
        
        
        canvas.selectAll(".responseBar").append("rect").data(dataArray).enter().append("rect").attr("class", "responseBar")
                .attr("width", function(d) { return widthScale(d) * scaleMultiplier;}).attr("height", 25)
                .attr("x", margin.left + 1).attr("y", function(d,i){ return margin.top + i * verticalGap;})
                .attr("fill", function(d,i){return colorScale(i);})
                
        var verticalLabel = ["Within 4-7 Days","Within 2-3 Days","Within 8-30 Days","More Than 30 Days",
                            "No Measure till now","Instantly/Within 1 day","Not Known"]
        
        canvas.selectAll(".responseY").append("text")
              .data(verticalLabel).enter().append("text").text(function(d){return d}).attr("class", "responseY")
                .attr("x", 0).attr("y", function(d,i){return margin.top + 20 + i * verticalGap})
                .attr("text-align", "left")
        
        var horizontalLabel = [1,2,3,4,5,6,7,8,9,10]
        canvas.selectAll(".responseX").append("text").data(horizontalLabel).enter().append("text")
               	 .text(function(d){return ""+d * 10;}).attr("class", "responseX")
        		 .attr("x", function(d){return (margin.left + 10 * d * scaleMultiplier);})
        		 .attr("y", 450).attr("text-align", "left")
        
        canvas.append("text").text("(Response in %)").attr("x", 500).attr("y", 480)
        
    }
    
    $("document").ready(function (){
    	//console.log($("#issueOne").val());
    	//drawCircles($("#causeOne").val(), "#first");drawCircles($("#causeTwo").val(), "#second");
    	//drawCircles($("#causeThree").val(), "#third");drawCircles($("#causeFour").val(), "#fourth");
    	//drawIssue([$("#issueOne").val(),$("#issueTwo").val(),$("#issueThree").val(),$("#issueFour").val(),$("#issueFive").val()], "#issue");
    	//drawResponse([100,90,30,95,90,30,60], "#response");
    });
