
   function reset(){

    	$("#inEqual").empty();    	
    	$("#facilities").empty();
    	$("#event").empty();
    }
   
  function drawInEqual(dataArray,id){
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
	      var horizontalLabel = ["cause1","cause2","cause3","cause4","cause5","cause6","cause7","cause8"]
	      
	      var sportsRecreationCauseLabel = ['Not enough space to play split together', 
			'Boys and Girls are not split interested to play simultaneously',
			 'Respective teachers are split not interested', 'Parents are not interest', 'Not have much time split to play',
			 'Do not have common room', 'No instruments for girls', 'Other'];
	      
	      var yheight = [650, 625, 630, 630, 600, 650, 630, 500]
	      var dx = [60, 230, 100, 220, 60, 220, 190, 80]
	      canvas.selectAll(".toiletX").append("text").data(sportsRecreationCauseLabel).enter().append("text")
	                              .text(function(d) {return d.split("split")[0];})
	                              .attr("class", "toiletX")
	                              .attr("text-anchor", "left")
	                              
	                              .attr("transform", function(d,i){
                            	  
	                            	  return "translate("+ (55 + 120 * i) +","+yheight[i]+") rotate(-80)";
	                               })
	                              .append("tspan")
	                              .text(function(d){return d.split("split")[1]})
	                              .attr("dx", function(d,i){ return -dx[i]})
	                              .attr("dy", function(d,i){return 15;})
	                              
	                              //.attr("x", function (d, i){return margin.left + 20 + i * 120})
	                              //.attr("y", height - 20)
	      
	      canvas.selectAll(".toiletBar").append("rect").data(dataArray).enter().append("rect")
	                              .attr("class", "toiletBar")
	                              .attr("width", 30)
	                              .attr("height",function(d){ return d * 43 / 10})
	                              .attr("x", function(d,i){return margin.left + 25 + 120 * i;})
	                              .attr("y", function(d,i){return height - margin.bottom - d * 43 / 10 })
	                              .attr("fill", function(d,i){return colorScale(i)})
	  }
  
    function drawFacilities(dataArray, id){
        var width = 1000, height = 500;
        var margin = {left: 220, right : 0, top: 20, bottom: 70}
        var verticalGap = 60;
        var horizontalGap = 10;
        var scaleMultiplier = 7;
        
        var widthScale = d3.scale.linear().domain([0, 100]).range([0,100])
        
        var colorScale = d3.scale.ordinal().domain([0,1,2,3,4,5,6])
							.range(["#D7DF21","#FCB040","#F1592A","#0F75BC","#61CCEE","#C49A6C","#13A89E"])
							
        var canvas = d3.select(id).append("svg").attr("width",width).attr("height", height);
        
        canvas.append("line").attr("x1", margin.left).attr("y1", height - margin.bottom).attr("x2", width)
               	.attr("y2", height - margin.bottom).attr("stroke-width", 2).attr("stroke", "#A6A7A6");
        
        canvas.append("line").attr("x1", margin.left).attr("y1", height - margin.bottom + 1)
                .attr("x2", margin.left).attr("y2", 5).attr("stroke-width", 2).attr("stroke", "#A6A7A6");
        
        
        
        canvas.selectAll(".facilitiesBar").append("rect").data(dataArray).enter().append("rect").attr("class", "facilitiesBar")
                .attr("width", function(d) { return widthScale(d) * scaleMultiplier;}).attr("height", 25)
                .attr("x", margin.left + 1).attr("y", function(d,i){ return margin.top + i * verticalGap;})
                .attr("fill", function(d,i){return colorScale(i);})
                
        var verticalLabel = ["Sports Field","Sports Instruments","Musical Instruments","Different types of competition",
                            "Scout,Girls/Boys Guide","No Instrument Available","Library"]
        
        canvas.selectAll(".facilitiesY").append("text")
              .data(verticalLabel).enter().append("text").text(function(d){return d}).attr("class", "facilitiesY")
                .attr("x", 0).attr("y", function(d,i){return margin.top + 20 + i * verticalGap})
                .attr("text-align", "left")
        
        var horizontalLabel = [1,2,3,4,5,6,7,8,9,10]
        canvas.selectAll(".facilitiesX").append("text").data(horizontalLabel).enter().append("text")
               	 .text(function(d){return ""+d * 10;}).attr("class", "facilitiesX")
        		 .attr("x", function(d){return (margin.left + 10 * d * scaleMultiplier);})
        		 .attr("y", 450).attr("text-align", "left")
        
        canvas.append("text").text("(Response in %)").attr("x", 500).attr("y", 480)
        
    }

    
    function drawEvent(dataArray, id){
        var width = 1000, height = 500;
        var margin = {left: 250, right : 0, top: 20, bottom: 70}
        var verticalGap = 60;
        var horizontalGap = 10;
        var scaleMultiplier = 7;
        
        var widthScale = d3.scale.linear().domain([0, 100]).range([0,100])
        
        var colorScale = d3.scale.ordinal().domain([0,1,2,3,4,5,6])
							.range(["#D7DF21","#FCB040","#F1592A","#0F75BC","#61CCEE","#C49A6C","#13A89E"])
							
        var canvas = d3.select(id).append("svg").attr("width",width).attr("height", height);
        
        canvas.append("line").attr("x1", margin.left).attr("y1", height - margin.bottom).attr("x2", width)
               	.attr("y2", height - margin.bottom).attr("stroke-width", 2).attr("stroke", "#A6A7A6");
        
        canvas.append("line").attr("x1", margin.left).attr("y1", height - margin.bottom + 1)
                .attr("x2", margin.left).attr("y2", 5).attr("stroke-width", 2).attr("stroke", "#A6A7A6");
        
        
        
        canvas.selectAll(".eventBar").append("rect").data(dataArray).enter().append("rect").attr("class", "eventBar")
                .attr("width", function(d) { return widthScale(d) * scaleMultiplier;}).attr("height", 25)
                .attr("x", margin.left + 1).attr("y", function(d,i){ return margin.top + i * verticalGap;})
                .attr("fill", function(d,i){return colorScale(i);})
                
        var verticalLabel = ["Annual Sports Competition", "Inter-School Debate Competition", "Inter-School Sports Competition",
                            "Study Tour", "No Event Organized","Cultural Festival", "Other"]
        
        canvas.selectAll(".eventY").append("text")
              .data(verticalLabel).enter().append("text").text(function(d){return d}).attr("class", "eventY")
                .attr("x", 0).attr("y", function(d,i){return margin.top + 20 + i * verticalGap})
                .attr("text-align", "left")
        
        var horizontalLabel = [1,2,3,4,5,6,7,8,9,10]
        canvas.selectAll(".eventX").append("text").data(horizontalLabel).enter().append("text")
               	 .text(function(d){return ""+d * 10;}).attr("class", "eventX")
        		 .attr("x", function(d){return (margin.left + 10 * d * scaleMultiplier);})
        		 .attr("y", 450).attr("text-align", "left")
        
        canvas.append("text").text("(Response in %)").attr("x", 500).attr("y", 480)
        
    }
    
    $("document").ready(function(){
         //drawInEqual([10,20,30,40,50,60,70,80],"#inEqual");
    	 //drawFacilities([10,50,32,100,90,30, 60], "#facilities");
    	 //drawEvent([10,50,32,100,90,30, 60], "#event");
    });
   
