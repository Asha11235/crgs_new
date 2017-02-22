function groupedBarChartTestSolve(data){
	console.log("Grouped bar chart here!!!!!");
	
	var margin = {top: 20, right: 20, bottom: 80, left: 40},
    	width = 800 - margin.left - margin.right,
    	height = 400 - margin.top - margin.bottom;

	var x0 = d3.scale.ordinal()
	    .rangeRoundBands([0, width], .4);
	
	var x1 = d3.scale.ordinal();
	
	var y = d3.scale.linear()
	    .range([height, 0]);
	
	var color = d3.scale.ordinal()
	    .range([ "#ff3333","#040081","#6b486b", "#a05d56", "#d0743c", "#ff8c00","#CCCC00"]);
	
	var xAxis = d3.svg.axis()
	    .scale(x0)
	    .orient("bottom");
	
	var yAxis = d3.svg.axis()
	    .scale(y)
	    .orient("left")
	    .tickFormat(d3.format(".2s"));
	
	var svg = d3.select("#solvegraph").append("svg") 
	    .attr("width", width + margin.left + margin.right)
	    .attr("height", height + margin.top + margin.bottom)
	  .append("g")
	    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	//Name of grouped bar
	var ageNames = d3.keys(data[0]).filter(function(key) { return key !== "State"; });
	//console.log(ageNames);
	
	//data against each bar
	 data.forEach(function(d) {
		    d.ages = ageNames.map(function(name) { return {name: name, value: +d[name]}; });
		    //console.log(d.ages);
	 });
	 
	 //x values as jan, feb, march
	 x0.domain(data.map(function(d) { return d.State; }));
	 //bar name in x coordinate sets in x1 domain
	 x1.domain(ageNames).rangeRoundBands([0, x0.rangeBand()]);
	 //y value
	 y.domain([0, d3.max(data, function(d) { return d3.max(d.ages, function(d) { return d.value; }); })]);
	 
	 svg.append("g")
     .attr("class", "x axis")
     .attr("transform", "translate(0," + height + ")")
     .call(xAxis);

	 svg.append("g")
	     .attr("class", "y axis")
	     .call(yAxis)
	   .append("text")
	     .attr("transform", "rotate(-90)")
	     .attr("y", 6)
	     .attr("dy", ".71em")
	     .style("text-anchor", "end");
	     //.text("Population");font-size: 200%
	
	 var state = svg.selectAll(".state")
	     .data(data)
	   .enter().append("g")
	     .attr("class", "state")
	     .attr("transform", function(d) { return "translate(" + x0(d.State) + ",0)"; });
	
	 state.selectAll("rect")
	     .data(function(d) { return d.ages; })
	     .enter().append("rect")
	     .attr("width", x1.rangeBand())
	     .attr("x", function(d) { return x1(d.name); })
	     .attr("y", function(d) { return y(d.value); })
	     .attr("height", function(d) { return height - y(d.value); })
	     .style("fill", function(d) { return color(d.name); });
	     
	state.selectAll("bartext")
		.data(function(d) { return d.ages; })
		.enter()
		.append("text")
		.attr("class", "bartext")
		.attr("text-anchor", "middle")
		.attr("fill", "black")
		.attr("x", function(d) { return x1(d.name) + 13; })
		.attr("y", function(d) {
		  //return y(d.y0)+((y(d.y1)-y(d.y0))/2); // previously 20  
			return y(d.value) - 6;
		})
		.style("font-size", 10 + "px").text(function(d,i){
		   if(d.value !=0)
		    //return d.y1-d.y0 +"%";
			return d.value;
		});

    
	
	 /*var legend = svg.selectAll(".legend")
	     .data(ageNames.slice().reverse())
	   .enter().append("g")
	     .attr("class", "legend")
	     .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });
	
	 legend.append("rect")
	     .attr("x", width - 135)
	     .attr("width", 10)
	     .attr("height", 10)
	     .style("fill", color);
	
	 legend.append("text")
	     .attr("x", width - 124)
	     .attr("y", 5)
	     .attr("dy", ".35em")
	     .style("text-anchor", "start")
	     .style("font-size", "70%")
	     .text(function(d) { return d; });*/
var legend = svg.selectAll(".legend")
    .data(ageNames.slice().reverse())
    .enter().append("g")
    .attr("class", "legend")
    .attr("transform", function(d, i) { return "translate(" + i * 100 + ",0)"; });
    //.attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });     
	legend.append("rect")
         .attr("x", 6)
         .attr("y", 345)
         .attr("width", 15)
         .attr("height", 15)
         .style("fill", color);
    
     legend.append("text")
         .attr("x", 30)
         .attr("padding",20)
         .attr("y", 350)
         .attr("width", 40)
         .attr("dy", ".35em")
         .style("text-anchor", "start")
         .style("font-size", "70%")
         .text(function(d) { return d; });

	 
}
