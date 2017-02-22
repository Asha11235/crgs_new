//********************Horizontal BAR CHART**********************************
/*function horizontalBarChart(data){
	var margin = {top: 20, right: 20, bottom: 30, left: 170},
	width = 700 - margin.left - margin.right,
	height = 478 - margin.top - margin.bottom;
	
	var y = d3.scale.ordinal()
	.rangeRoundBands([0, height], .1);
	
	var x = d3.scale.linear()
	.range([0, width]);
	
	var yAxis = d3.svg.axis()
	.scale(y)
	.orient("left");
	
	
	var xAxis = d3.svg.axis()
	.scale(x)
	.orient("bottom")
	.innerTickSize(-height)
	.outerTickSize(0)
	.tickPadding(5);
	
	var svg = d3.select("#graph").append("svg")  
	.attr("width", width + margin.left + margin.right)
	.attr("height", height + margin.top + margin.bottom)
	.append("g")
	.attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
	var tip = d3.tip()
	.attr('class', 'd3-tip')
	.offset([-10, 0])
	.html(function(d) {
	    return  d.title+":" + d.number;
	});
	svg.call(tip);
	
	var total=0;
	//console.log("Hello  " + d3.max(data, function(d,i) { total+=d.number[i]; return d.number[i]; }));
	y.domain(data.map(function(d) { return d.title; }));
	x.domain([0, d3.max(data, function(d,i) { total+=+d.number; 
	//console.log(d.number); 
	return +d.number; })]);
	//console.log("*******TOTAL  "+ total);
	svg.append("g")
	.attr("transform", "translate(0," + height + ")")
	  .attr("class", "x axis")
	  .call(xAxis);
	
	svg.append("g")
	  .attr("class", "y axis")
	  .call(yAxis);
	
	svg.selectAll(".bar")
	  .data(data)
	.enter().append("rect")
	  .attr("class", "bar")
	  .attr("x", function(d) { return 0; })
	  .attr("y", function(d) { return y(d.title); })
	  .attr("height", y.rangeBand())
	  .attr("width", function(d) { return x(d.number); } )
	  .on("mouseout", function(d){
		d3.select(this).style("fill", "steelblue");
		//legendText.style("fill", "black");
		tip.hide(d);
		})
	  .on("mouseover", function(d){
		d3.select(this).style("fill", "blue");
		//legendText.style("fill", "blue");
		tip.show(d);
		});
	

	// add legend   
	/*var colors =	[ ["Number of toilets", "#377EB8"]];
	var legend = svg.append("g")
			.attr("class", "legend")
			//.attr("x", w - 65)
			//.attr("y", 50)
			//.attr("height", 100)
			//.attr("width", 100)
			.attr('transform', 'translate(-20,50)');

	var legendRect = legend.selectAll('rect').data(colors);

	legendRect.enter()
	    .append("rect")
	    .attr("x", width - 65)
	    .attr("width", 10)
	    .attr("height", 10);

	legendRect
	    .attr("y", function(d, i) {
	        return i * 20;
	    })
	    .style("fill", function(d) {
	        return d[1];
	    });

	var legendText = legend.selectAll('text').data(colors);

	legendText.enter()
	    .append("text")
	    .attr("x", width - 52);

	legendText
	    .attr("y", function(d, i) {
	        return i * 20 + 9;
	    })
	    .text(function(d) {
	        return d[0];
	    });*/
	
/*	function type(d) {
	d.number = +d.number; // coerce to number
	return d;
	}
	
}*/

function horizontalBarChartTest(data, obj){
	console.log("HB Test here.")
	var w=obj['w'];
	var h=obj['h'];
	var element=obj['element'];
	var xLabel=obj['xLabel'];
	var yLabel=obj['yLabel'];
	var color=obj['color'];
	var afterColor="";
	var deg=0;
	var bottombar=40;
	if('afterColor'  in obj)
	   afterColor=obj['afterColor'];
	else afterColor="gray";

	if('xTextDegree'  in obj)
	   deg=obj['xTextDegree'];
	else deg="0";

	if('barFontSize' in obj)
		tsize=obj['barFontSize'];
	else tsize=12; 

	if('extendHeight' in obj)
		bottombar=obj['extendHeight'];
	else bottombar=40; 


	var margin = {top: 20, right: 70, bottom: 30, left:190},     //graph margin
	    width = w - margin.left - margin.right,           
	    height = h - margin.top - margin.bottom;

	var y = d3.scale.ordinal()
	    .rangeRoundBands([0, height], .1);  //bar height

	var x = d3.scale.linear()
	    .range([0, width]);                 //bar width

	var yAxis = d3.svg.axis()
	    .scale(y)
	    .orient("left");
		

	var xAxis = d3.svg.axis()
	    .scale(x)
	    //.orient("bottom")
		.innerTickSize(-height)
	    .outerTickSize(0)
	    .tickPadding(5);
	//var svg = d3.select("#graph").append("svg");
	var chart = d3.select(element).append("svg")
	    .attr("width", width + margin.left + margin.right)
	    .attr("height", height + margin.top + margin.bottom)
	  .append("g")
	    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
		
	var tip = d3.tip()
	    .attr('class', 'd3-tip')
	    .offset([-10, 0])
	    .html(function(d) {
	       // return "<strong>x_data:  </strong> <span style='color:blue'>" + d.x_data+"</span> </br> <strong> y_data:  </strong><span style='color:blue'>" + d.y_data + "</span>";
	  return "<span style='color:gray'>Measure Name: </span><span style='color:black'>  <strong> " + d.title+"  </strong> </span> </br> <span style='color:gray'>Measure Value</span> <span style='color:black'>  <strong> " + d.number + "  </strong> </span> </br><span style='color:gray'> Total Value: <span style='color:black'> <strong> " + total + " </strong> </span></br><span style='color:gray'> % is:  <span style='color:black'> <strong> " + d.pData+ " </strong> </span>";
	});
	chart.call(tip);

	  var total=0;
	  //console.log("Hello  " + d3.max(data, function(d,i) { total+=d.y_data[i]; return d.y_data[i]; }));
	  y.domain(data.map(function(d) { return d.title; }));
	  x.domain([0, d3.max(data, function(d,i) { total+=+d.number; console.log(d.number); return +d.number; })]);
	  console.log("*******TOTAL  "+ total);
	  /*chart.append("g")
		.attr("transform", "translate(0," + height + ")")
	      .attr("class", "x axis")
	      .call(xAxis); */

	  chart.append("g")
	      .attr("class", "y axis")
		.style("font-size", tsize + "px")
	      .call(yAxis);

	  chart.selectAll(".bar")
	      .data(data)
	    .enter().append("rect")
	      .attr("class", "bar")
		
	      .attr("x", function(d) { return 0; })
	       .style("fill", color)
	      .attr("y", function(d) { return y(d.title); })
	      .attr("height", y.rangeBand())
	      .attr("width", function(d) { return x(d.number); } )
		  .on("mouseout", function(d){
			d3.select(this).style("fill", color);
			//legendText.style("fill", "black");
			tip.hide(d);
			})
		  .on("mouseover", function(d){
			d3.select(this).style("fill", afterColor);
			//legendText.style("fill", "blue");
			tip.show(d);
			});
			
	 var xv=0;
	 chart.selectAll(".bartext")
	.data(data)
	.enter()
	.append("text")
	.attr("class", "bartext")
	.attr("text-anchor", "middle")
	.attr("fill", "black")
	   .style("font-weight", "normal")
	.attr("y", function(d,i) {
	    return y(d.title)+ y.rangeBand()/2;
	})
	.attr("x", function(d,i) {
	    return x(d.number)+ 30;
	})
	.style("font-size", tsize + "px")
	.text(function(d){
		if(total){
		 var percen= (100* d.number)/total;
		}
		else{
			var percen = 0;
		}
		 xv +=percen;
		
		 percen=Number((percen).toFixed(1));
	d.pData=percen +"%";
		 return percen +"%";
	});
	//console.log("***********" + xv);

	function type(d) {
	  d.number = +d.number; // coerce to y_data
	  return d;
	}

}
