function groupedBarChartTest(data){
	console.log("Grouped bar chart here!!!!!");
	console.log(data);
	
	var margin = {top: 20, right: 20, bottom: 80, left: 40},
    	width = 900 - margin.left - margin.right,
    	height = 400 - margin.top - margin.bottom;

	var x0 = d3.scale.ordinal()
	    .rangeRoundBands([0, width], .4);
	
	var x1 = d3.scale.ordinal();
	
	var y = d3.scale.linear()
	    .range([height, 0]);
	
	var color = d3.scale.ordinal()
	    .range([ "#21610B", "#ff9933", "#6699ff", "#999966", "#d27979" ]);
	
	var xAxis = d3.svg.axis()
	    .scale(x0)
	    .orient("bottom");
	
	var yAxis = d3.svg.axis()
	    .scale(y)
	    .orient("left")
	    .tickFormat(d3.format(".2s"));
	
	var svg = d3.select("#graph").append("svg") 
	    .attr("width", width + margin.left + margin.right)
	    .attr("height", height + margin.top + margin.bottom+40)
	  .append("g")
	    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
	

	//Name of grouped bar
	var ageNames = d3.keys(data[0]).filter(function(key) { return key !== "Month"; });
	//console.log(ageNames);
	
	//data against each bar
	 data.forEach(function(d) {
		    d.ages = ageNames.map(function(name) { return {name: name, value: +d[name]}; });
		    console.log(d.ages);
	 });
	 
	 //x values as jan, feb, march
	 x0.domain(data.map(function(d) { return d.Month; }));
	 //bar name in x coordinate sets in x1 domain
	 x1.domain(ageNames).rangeRoundBands([0, x0.rangeBand()]);
	 //y value
	 y.domain([0, d3.max(data, function(d) { return d3.max(
			 d.ages, function(d) {  return d.value; });
	 		
	 })]);
	
	 svg.append("g")
     .attr("class", "x axis")
     .attr("transform", "translate(0," + height + ")")
     .call(xAxis)
     .selectAll("text") 
            .style("text-anchor", "end")
            .style("font-size", 12 + "px")
            //.attr("dx", "-.8em")
            .attr("dy", ".55em")
	    .attr("x", 10)
            .attr("transform", "rotate(-30)" )
            .call(wrap, x1.rangeBand());  //for wrapping text
	 
	 
	
	 svg.append("g")
	     .attr("class", "y axis")
	     .call(yAxis)
	   .append("text")
	     .attr("transform", "rotate(-90)")
	     .attr("y", 6)
	     .attr("dy", ".71em")
	     .style("text-anchor", "end");
	     //.text("Population");font-size: 200%
	
	 var month = svg.selectAll(".month")
	     .data(data)
	   .enter().append("g")
	     .attr("class", "month")
	     .attr("transform", function(d) { return "translate(" + x0(d.Month) + ",0)"; });
	
	 month.selectAll("rect")
	     .data(function(d) { return d.ages; })
	   .enter().append("rect")
	     .attr("width", x1.rangeBand())
	     .attr("x", function(d) { return x1(d.name); })
	     .attr("y", function(d) { return y(d.value); })
	     .attr("height", function(d) { return height - y(d.value); })
	     .style("fill", function(d) { return color(d.name); });
	 
	 
	//for append text in each bar 
	 month.selectAll("bartext")
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
     .style("font-size", 11 + "px").text(function(d,i){
        if(d.value !=0)
         //return d.y1-d.y0 +"%";
         return d.value+"%";
     });
	 
	//legend
	/* var legend = svg.selectAll(".legend")
	     .data(ageNames.slice().reverse())
	     .enter().append("g")
	     .attr("class", "legend")
	     .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });
	
	 legend.append("rect")
	     .attr("x", 687)
	     .attr("y", 5)
	     .attr("width", 10)
	     .attr("height", 10)
	     .style("fill", color);
	
	 legend.append("text")
	     .attr("x", 707)
	     .attr("y", 10)
	     .attr("dy", ".35em")
	     .style("text-anchor", "start")
	     .style("font-size", "80%")
	     .text(function(d) { return d; });*/
	 var legend = svg.selectAll(".legend")
     .data(ageNames.slice().reverse())
     .enter().append("g")
     .attr("class", "legend")
     .attr("transform", function(d, i) { return "translate(" + i * 150 + ",0)"; });
     //.attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });

	 legend.append("rect")
	     .attr("x", 6)
	     .attr("y", 382)
	     .attr("width", 15)
	     .attr("height", 15)
	     .style("fill", color);
	
	 legend.append("text")
	     .attr("x", 30)
	     .attr("padding",20)
	     .attr("y", 390)
	     .attr("width", 40)
	     .attr("dy", ".35em")
	     .style("text-anchor", "start")
	     .style("font-size", "70%")
	     .text(function(d) { return d; });
		 
	 ///
		//For Wraping Text in x axis . This part is totally taken from here>> bl.ocks.org/mbostock/7555321 . no idea whats going on! <PERSIA> 
		 function wrap(text, width) {
		   text.each(function() {
		     var text = d3.select(this),
		         words = text.text().split(/\s+/).reverse(),
		         word,
		         line = [],
		         lineNumber = 0,
		         lineHeight = 1.1, // ems
		         y = text.attr("y"),
		         dy = parseFloat(text.attr("dy")),
		         tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em");
		     while (word = words.pop()) {
		       line.push(word);
		       tspan.text(line.join(" "));
		       if (tspan.node().getComputedTextLength() > width) {
		         line.pop();
		         tspan.text(line.join(" "));
		         line = [word];
		         tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", ++lineNumber * lineHeight + dy + "em").text(word);
		       }
		     }
		   });
		 }
		 
		 //
	 
}
