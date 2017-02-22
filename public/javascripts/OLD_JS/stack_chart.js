
//*************************************************************
//***********************Normalized*****************************
//*************************************************************


//Last PERSIA 9/8/25
//   Input: JSON data.
//1. Parameter name: data
	//     type: JSON object
	// In this Object->
	//1. tag: name of the pie chart division
	//2. number: number of the corresponding tag
//2. Parameter name: obj
	//     Type: object
	//     keys of the object are: 'w', 'h', 'element', 'xLabel', 'yLabel', 'color', 'afterColor', 'barFontSize','extendHeight'


function normalStack_chart(data,obj){
var legendRect="";
var legend="";
var district="";
var counter=0;
var bar="";

//var w=850,h=330,element="#body",xLabel="x values",yLabel="y values";
var w=obj['w'];
var h=obj['h'];
var element=obj['element'];
var xLabel=obj['xLabel'];
var yLabel=obj['yLabel'];
var colorRange=obj['color'];
var afterColor="";
var deg=0;
var bottombar=40;
if('afterColor'  in obj)
   afterColor=obj['afterColor'];
else afterColor="gray";

if('xTextDegree'  in obj)
   deg=obj['xTextDegree'];
else deg="0";

var anchor="end";
if(deg==0) anchor="middle";
else anchor="end";

if('barFontSize' in obj)
	tsize=obj['barFontSize'];
else tsize=12; 

if('extendHeight' in obj)
	bottombar=obj['extendHeight'];
else bottombar=40; 

//var sub_total;

var margin = {top: 20, right: 40, bottom: 100, left: 50},
    width = w - margin.left - margin.right,
    height = h - margin.top - margin.bottom;

 /*
 * rangeRoundBands: The array interval contains two elements
 * representing the minimum and maximum numeric value
 * 
 * This interval is subdivided into n evenly-spaced bands
 * 
 * A value of 0.5 means that the band width will be equal to the padding width
 */
var x = d3.scale.ordinal()
    .rangeRoundBands([0, width], 0.5);

var y = d3.scale.linear()
    .rangeRound([height, 0]);
/*
 * The first element in the domain will be mapped to the first
 * element in values, the second domain value to the second 
 * range value, and so on.values are recycled if fewer values specified.
 */
var color = d3.scale.ordinal()
    .range(colorRange);

var xAxis = d3.svg.axis() // Create a new default axis.
    .scale(x)			  // Sets the scale
    .tickPadding(5)		  // Sets the distance of words from x axis 
    .orient("bottom");    // Horizontal axis with ticks below the domain path

var yAxis = d3.svg.axis() // Create a new default axis.
    .scale(y)			  // Sets the scale
    .orient("left")		  // vertical axis with ticks to the left of the domain path
    .tickFormat(d3.format(".0%"));//tickformat: sets the tick-format to the specified function
						  //d3.format(.2s): SI-prefix with two significant digits, i.e "42M"

var svg = d3.select(element)					// Appends a svg element with the body as the last child
    .attr("width", width + margin.left + margin.right)      // Sets the width attribute of svg to specified value
    .attr("height", height + margin.top + margin.bottom)	// Sets the height attribute of svg to specified value
    .append("g")	// Appends a 'g' element to the SVG. g is used to group SVG shapes together. Once grouped you can transform the whole group of shapes like one element.
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")"); // Set transfrom attrb of g to translate
															// translated to the point (margin.left, margin.top) instead of specified (x,y) attrb.

//**************TIP***************
//d3.tip: suggestion on hover.
var tip = d3.tip()
    .attr('class', 'd3-tip')
    .offset([-10, 0])
    .html(function(d) {
       // return "<strong>Range:  </strong> <span style='color:blue'>" + d.name+"</span> </br> <strong> Total:  </strong><span style='color:blue'>" + ((d.y1-d.y0)*100).toFixed(2)+"%" + "</span>";
   return "<span style='color:gray'>Measure Name: </span><span style='color:black'>  <strong> " + d.name+"  </strong> </span> </br> <span style='color:gray'>Measure Value</span> <span style='color:black'>  <strong> " + (d.y1).toFixed(1) + "  </strong> </span></br><span style='color:gray'> % is:  <span style='color:black'> <strong> " + ((d.y1-d.y0)*100).toFixed(1)+"%"+ " </strong> </span>";
		//+ "</span> </br> <strong> Percentage:  </strong><span style='color:blue'>" + ((100*(d.y1-d.y0))/d.y1) + "</span>";
});

svg.call(tip);// calls the tip in svg element
//data.forEach(function(d) {
//	tip.show(d);
//});
  /* selects color from list of colors where data is not district i.e. male, female
   * d3.keys(object): returns assoc array of containing the property names of the specified object
   * in this case [district,male,female]
   * 
   * array.filter(callback fn): filters out the undesired element from array according to
   * callback function. In this case from [district,male,female] to [male,female]
   */
  color.domain(d3.keys(data[0]).filter(function(key) { return key !== "axis_map"; }));
  var stack_count = 0 ;
//  sub_total=0;
  data.forEach(function(d) {
    var y0 = 0;
    //name = male/female y0 = 0[starting of bar] y1 = limit [ending of bar]
    //d[name] contains the value of male or female json
    d.ages = color.domain().map(function(name) {
    	return {name: name, y0: y0, y1: y0 += parseInt(d[name])}; 
	});
    stack_count = 0 ;
    d.ages.forEach(function(d) { d.y0 /= y0; d.y1 /= y0; stack_count++ ;});
//  console.log("data1::"+ d.ages[1].name);//d.ages[0] = male d.ages[1] = female
    d.total = d.ages[d.ages.length - 1].y1;// the last index of array d.ages' y1 contains the total value.
//	sub_total[d.district]+= (d.y1-d.y0);
  });
  
  //for sorting data array from the heighest bar to lowest bar(descending order)
  data.sort(function(a, b) { return b.total - a.total; });

  //maps x axis based on the property of defined json(data array). In this case districts i.e. Dhaka Khulna  
  x.domain(data.map(function(d) { return d.axis_map; }));
  
  /* sets the lower and upper limit of y axis; loops through data array 
   * to get the max total of data arrays any index.
  */
  y.domain([0, d3.max(data, function(d) { return d.total; })]);

  // appends g to the parent g and calls the xAxis to the child g element.
  /*svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);*/
  
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
            .call(wrap, x.rangeBand());  //for wrapping text
  
  
  
  
  
  // appends g to the parent g and calls the yAxis to the child g element.
  svg.append("g")              
      .attr("class", "y axis")
      .call(yAxis)
    .append("text") 				// appends text and rotates it -90 degree
      .attr("transform", "rotate(-90)")
	  .attr("y", 6)        			// y axis co-ordinate of text
      .attr("dy", ".71em") 			// shift along the yaxis of text
      .style("text-anchor", "end");  //align (start, middle or end) a string from given point
      //.text("Population");			// The text value
  
  //ref: http://bost.ocks.org/mike/circles/
  //  selectAll + data + enter + append <= this pattern for entering elements
  district = svg.selectAll(".district") 	// returns all elements that match the .district; here empty 
      .data(data)							// binding data with array:data
    .enter().append("g")					// entering elements bound to data and appending a g element
      .attr("class", "g")					// add class g as attribute
      .attr("transform", function(d) { return "translate(" + x(d.axis_map) + ",0)"; });  
  		// x(d.district): adjusts the x value with number of d.elements and leading and trailing space in this case x=430 data=[dhaka,khulna]
  		// with ___ dhaka ___ khulna ___ so 5 elements 430/5 = 86, first bar starts at 86 second bar at 258.
  var i = 0 ;
  var bar_count = -1 ;
  district.selectAll("rect")				
      .data(function(d) { return d.ages; })		// bind only the ages of the array:data as data
    .enter().append("rect")						// appends rect to the g element of
	  .attr("class", "barD")					// add class to the rect element
      .attr("width", x.rangeBand()) 			// shows the width of the bars based on x
      .attr("y", function(d) { return y(d.y1); })// d.y1 value of y1 and then scaled acording to y axis; y1 : yo+=bar_value
      .attr("height", function(d) { return y(d.y0) - y(d.y1); }) // height = y1 - y0
      .style("fill", function(d) { return color(d.name); }) //color(d.name) gets color from color domain in seq order; d.name=[male,female] 
	  .attr("index_value", function(d, i) { return i; })    // set index_value attribute with data indexes
	  .attr("chart_id", function() { counter+=1; return (Math.ceil(counter/4)); })
	  .attr("bar_count", function() {
		  if(i % stack_count == 0 ){ // try replacing 2 with the number of stacked bars in a single bar
			  bar_count++;
			  }
		  i++;
		  return bar_count;
		  ;})
	  .on("click", function(d,i) {
	      bar=d3.select(this);
		  //console.log(pieChart);
		  
		  var index= bar.attr("index_value");
		  console.log(index + "  "+ i);
		  //if(index==i) {
		  d3.selectAll("index_value")
		      .style("stroke", "black")
			  .style("stroke-width", 3)
			  .style("stroke-opacity", 0.9); //}
    })
      .on("mouseover", function(d,i){
    	  var bar=d3.select(this); 		// on mouseover gets the selected bar
    	  bar.style("fill", afterColor[i]);	// fill the bar with color
		  var index= bar.attr("index_value"); //get selected bars index_value attrb value
		  legendText.style("fill", function(d,i) {if(index==i) return "red"; })
		  .style("font-weight", function(d,i) {if(index==i) return "bold"; });
		  tip.show(d);
//		  console.log(d);
		})
		
	  .on("mouseout",function(d){
		  var bar=d3.select(this);
	      bar.style("fill", color(d.name)); // on mouseout gets color from domain based on selected data and fills bar
		  var index= bar.attr("index_value");// gets bar index
		
		  legendText.style("fill", function(d,i) {if(index==i) return "black"; })
		      .style("font-weight", function(d,i) {if(index==i) return ""; });
		  tip.hide(d);
		})
		.on("click",function(d,i){
			var bar = d3.select(this);
			var index= bar.attr("bar_count");
			if ( eval("typeof generatePie === 'function'") ){
				generatePie(x.domain()[index]); 
			}
			
//			console.log(x.domain()[index]);
		});

   district.selectAll("text")				
	  .data(function(d) { return d.ages; })		// bind only the ages of the array:data as data
	.enter().append("text")						// appends rect to the g element of
	.attr("x", function(d) { return 15 })
    .attr("y", function(d,i) {
//    	console.log(((d.y1-d.y0)/d.y1)*100+20+ "they are  "+ d.y0+"  "+d.y1); 
    	var value_bar = y(d.y0)+(y(d.y1)-y(d.y0))/2 ;
    	if(!isNaN(value_bar)){
    		console.log(value_bar)
    		return value_bar;
    	}
    	console.log(0);
    	return 0 ; 
    	})
    .text( function (d) { if((d.y1-d.y0)*100 > 0 ) return ((d.y1-d.y0)*100).toFixed(1)+"%" ; })
    .attr("text-anchor", "start")
    .attr("font-family", "sans-serif")
    .attr("font-size", tsize)
    .attr("fill", "black");
   
 /* legend = svg.selectAll(".legend") // selectall as syntax
      .data(color.domain().slice()) // binds data with array:color.domain() ; here [male,female] 
    .enter().append("g")			// append g element to svg
      .attr("class", "legend")		// add class to g
      .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; }); //translate with index

  //rect icons in the top right corner of graph i.e. male/female
  legendRect=legend.append("rect")	// append rect to g element
      .attr("x", width - 80)		// sets x 18px left to width
      .attr("width", 18)			// width 18
      .attr("height", 18)			// height 18
      .style("fill", color)			// fill from color domain sequentially from data
	
  //**************legendText is public here.**********
  // text in the top right corner of graph i.e. male/female
  legendText =legend.append("text") // appent text to g element
      .attr("x", width - 60)		// x attribute to 24px left to width
      .attr("y", 9)			// y to 9
      .attr("dy", ".35em")		// dy to .35em
      .style("text-anchor", "start")     // text alligned to end
	  .style("font-size", tsize)  // font size 15px
      .text(function(d) { return d; });*/  // d [male,female] gets from binded data in legend
  
	//legend in bottom
	 var legend = svg.selectAll(".legend")
   .data(color.domain().slice())
  .enter().append("g")
  .attr("class", "legend")
   .attr("transform", function(d, i) { return "translate(" + i * 150 + ",0)"; });
  //.attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });

	 legend.append("rect")
	     .attr("x", 6)
	     .attr("y", 357)
	     .attr("width", 15)
	     .attr("height", 15)
	     .style("fill", color);
	
	 legend.append("text")
	     .attr("x", 30)
	     .attr("padding",20)
	     .attr("y", 366)
	     .attr("width", 40)
	     .attr("dy", ".35em")
	     .style("text-anchor", "start")
	     .style("font-size", "101%")
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