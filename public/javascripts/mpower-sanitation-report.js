
   function reset(){
	   	$("#unUsableToilet").empty();
		$("#issue").empty();
		$("#response").empty();
    }
   
   
function causeOfToilet(dataArray,id){
    var width = 1000, height = 700, scaleMultiplier = 5
        margin = {left:50, right: 0, top: 20, bottom: 250};
        colorScale = d3.scale.ordinal([0,1,2,3,4,5,6,7,8,9,10,11,12])
                       .range(["#F1592A","#F7941E","#FCB040","#F1EA30",
                              "#D6DE21","#8CC63F","#38B149","#0B9344",
                              "#056839","#2BB673","#13A79D","#0F75BC","#283891"])
        
        var canvas = d3.select(id).append("svg").attr("width",width).attr("height",height);
        
        
        canvas.append("line")
                         .attr("x1",margin.left)
                         .attr("y1",height - margin.bottom)
                         .attr("x2",width)
                         .attr("y2", height - margin.bottom)
                         .attr("stroke-width", 2).attr("stroke", "#A6A7A6");
        canvas.append("line")
                         .attr("x1",margin.left)
                         .attr("y1",margin.top - 10)
                         .attr("x2",margin.left)
                         .attr("y2", height - margin.bottom)
                         .attr("stroke-width", 2).attr("stroke", "#A6A7A6");
    
      var verticalLabel = [1,2,3,4,5,6,7,8,9,10];
      canvas.selectAll(".toiletY").append("text").data(verticalLabel).enter().append("text")
                               .text(function(d){return "" + d * 10})
                               .attr("class", "toiletY")
                               .attr("x",margin.left - 30)
                               .attr("y", function(d,i){return height - margin.bottom - d * 43})
                               .attr("text-anchor", "middle")
      
      var sanitationCauseLabel = ["Tap not functional",
  		 "Source of water/water tap split not within the reach","No water in the toilet split", 
  		 "Toilet lock is split not functional","Toilet door broken/a  split whole in the door", 
  		 "No mug, Bucket,  split Bodna in the toilet", 
  		 "overflow of stagnant dirty  split water in the pan/commode", 
  		 "Stagnant water  split in the toilet", 
  		 "No separate bucket for  split female for throwing napkin/pad", 
  		 "No Sufficient ventilation and  split lighting in the toilet", 
  		 "Insects in the toilet","Foul smell", "Other"];
     // canvas.selectAll(".toiletX").append("text").data(horizontalLabel).enter().append("text")
       //                       .text(function(d) {return d;})
         //                     .attr("class", "toiletX")
           //                   .attr("x", function (d, i){return margin.left + 20 + i * 70})
             //                 .attr("y", height - 20)
      var yheight = [590, 650, 610, 550, 600, 580, 640, 570, 630, 650, 600, 540, 500]
      var dx = [200, 150, 200, 90, 120, 120, 190, 80, 210, 140, 200, 200, 200]
      canvas.selectAll(".toiletX").append("text").data(sanitationCauseLabel).enter().append("text")
                              .text(function(d) {return d.split("split")[0];})
                              .attr("class", "toiletX")
                              .attr("text-anchor", "left")
                              .attr("transform", function(d,i){
                            	  
                            	  return "translate("+ (55 + 70 * i) +","+yheight[i]+") rotate(-80)";
                              })
                              .append("tspan")
                              .text(function(d){return d.split("split")[1]})
                              .attr("dx", function(d,i){ return -dx[i]})
                              .attr("dy", function(d,i){return 15;})
                              
      
      canvas.selectAll(".toiletBar").append("rect").data(dataArray).enter().append("rect")
                              .attr("class", "toiletBar")
                              .attr("width", 30)
                              .attr("height",function(d){ return d * 43 / 10})
                              .attr("x", function(d,i){return margin.left + 25 + i * 70;})
                              .attr("y", function(d,i){return height - margin.bottom - d * 43 / 10 })
                              .attr("fill", function(d,i){return colorScale(i)})
  }
  
  
function drawIssue(dataArray, id){
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
	causeOfToilet([10,20,30,40,50,60,70,80,90,100,10,20,30],"#unUsableToilet");
	drawIssue([100,80,70,60,5], "#issue");
	drawResponse([100,90,30,95,90,30, 60], "#response");
});
