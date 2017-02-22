
//*************************************************************
//***********************Multi-line chart*****************************
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


function multi_line(data,obj) {

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

var anchor="end";
if(deg==0) anchor="middle";
else anchor="end";

if('barFontSize' in obj)
	tsize=obj['barFontSize'];
else tsize=12; 

if('extendHeight' in obj)
	bottombar=obj['extendHeight'];
else bottombar=40; 


// Set the dimensions of the canvas / graph
var margin = {top: 20, right: 50, bottom: 70, left: 50},
    width = w - margin.left - margin.right,
    height = h - margin.top - margin.bottom;

// Parse the date / time
var parseDate =d3.time.format("%b %Y").parse;

//Set the scales
//minDate.setDate(minDate.getDate() - 1);
//console.log(minDate);
//var maxDate = d3.max(data, function(d) { return new Date(d.date); });
//console.log(maxDate);

// Set the ranges
var x = d3.time.scale()
		.range([0, width]);
var y = d3.scale.linear().range([height, 0]);

var xAxis = d3.svg.axis().scale(x)
    .orient("bottom")//.ticks(5)
    .ticks(d3.time.months)
    .tickPadding(10)		  // Sets the distance of words from x axis 
   .innerTickSize(-height, 0, 0)
   .tickFormat(d3.time.format("%B"));


var yAxis = d3.svg.axis().scale(y)
    .orient("left")
     .tickSize(-width, 0, 0)
      .ticks(5);


// Define the line
var y_valueline = d3.svg.line()	
    .x(function(d) { return x(d.date); })
    .y(function(d) { return y(d.y_value); });
    
// Adds the svg canvas
var svg = d3.select(element)
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
    .append("g")
        .attr("transform", 
              "translate(" + margin.left + "," + margin.top + ")");

var monthNames = ["January", "February", "March", "April", "May", "June",
  	               "July", "August", "September", "October", "November", "December"];
/*var monthNames = [ "July", "August", "September", "October", "November", "December","January", "February", "March", "April", "May", "June"];*/
	//***Tool tip
	var tip = d3.tip()
	    .attr('class', 'd3-tip')
	    .offset([-10, 0])
	    .html(function(d) {
	       // return "<strong>Month: </strong> <span style='color:blue'>" +monthNames[d.date.getMonth()]  +"</span> </br> <strong> Rating:  </strong><span style='color:blue'>" + d.symbol +" satisfaction level "+d.y_value + "</span>";

 return "<span style='color:gray'>Month: </span><span style='color:black'>  <strong> " + monthNames[d.date.getMonth()] +"  </strong> </span> </br> <span style='color:gray'>Satisfaction level</span> <span style='color:black'>  <strong> " +d.y_value + "  </strong> </span> </br><span style='color:gray'> Rating: <span style='color:black'> <strong> " + d.symbol + " </strong> </span>";

	});
	svg.call(tip);



// Get the data
    data.forEach(function(d) {
		d.date = parseDate(d.date);
		d.y_value = +d.y_value;
    });

    // Scale the range of the data
    x.domain(d3.extent(data, function(d) { return d.date; }));
    y.domain([0, 5]);//d3.max(data, function(d) { return d.y_value; })]);

    // Nest the entries by symbol
    var dataNest = d3.nest()
        .key(function(d) {return d.symbol;})
        .entries(data);

    var color = d3.scale.category10();   // set the colour scale

    legendSpace = width/dataNest.length; // spacing for the legend
    var allSymbol=[];
    var iterator=0;
    
    // Loop through each symbol / key
    dataNest.forEach(function(d,i) { 

        svg.append("path")
            .attr("class", "line")
            .style("stroke", function() { // Add the colours dynamically
            allSymbol[iterator]=d.key;
            iterator++;
            //console.log(d.key +"  "+iterator);  
            return d.color = color(d.key); 
         })
            .attr("id", 'tag'+d.key.replace(/\s+/g, '')) // assign ID
            .attr("d", y_valueline(d.values));

  //     console.log(allSymbol[0] +" sybolList ");
  var point = svg.append("g")
        .attr("class", "line-point");
	point.selectAll('circle')
	.data(data)
	.enter().append('circle')
	.attr("cx", function(d, i) {
		return x(d.date);
	  })
	 .attr("cy", function(d, i) { return y(d.y_value) })
	 .attr("r", 4)
	 .on("mouseout", function(d){
		d3.select(this).style("fill", "black")
				.attr("r", 4);
		tip.hide(d);
		})
	  .on("mouseover", function(d){
		d3.select(this).style("fill", "gray") .attr("r", 7);
		//legendText.style("fill", "blue");
		tip.show(d);
		});
	  

        // Add the Legend
	/*legend = svg.selectAll(".legend") // selectall as syntax
    .data(color.domain().slice()) // binds data with array:color.domain() ; here [male,female] 
  .enter().append("g")			// append g element to svg
    .attr("class", "legend")		// add class to g
   // .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; }); //translate with index
    .attr("transform", function(d, i) { 
    	console.log
    	return "translate("+i*20+",0)"; });
	
	
	 legend.append("rect")	// append rect to g element
	   .attr("x", 100)		// sets x 18px left to width
	   .attr("y", 335)
	    .attr("width", 18)			// width 18
	    .attr("height", 18)			// height 18
	    .style("fill", color);
	 */
	 
	 
	 //legend add start
      /* svg.append("text")
            .attr("x", (legendSpace/2)+i*legendSpace)  // space legend
            .attr("y", height + (margin.bottom/2)+ 5)
            .attr("class", "legend")   // style the legend
            .attr("font-size","14px")	
            .style("fill", function() { // Add the colours dynamically
                return d.color = color(d.key); })
            /*.on("click", function(){
                // Determine if current line is visible 
                var active   = d.active ? false : true,
                newOpacity = active ? 0 : 1; 
                // Hide or show the elements based on the ID
                d3.select("#tag"+d.key.replace(/\s+/g, ''))
                    .transition().duration(100) 
                    .style("opacity", newOpacity); 
                // Update whether or not the elements are active
                d.active = active;
                })*/  
            //.text(d.key);*/ 
       
      /* svg.append("rect")
       .attr("x", (legendSpace/2)+i*legendSpace)  // space legend
       .attr("y", height + (margin.bottom/2)+ 5)
       .attr("class", "legend")   // style the legend
       //.attr("font-size","14px")	
       .style("fill", function() { // Add the colours dynamically
           return d.color = color(d.key); })
       
       .rect(d.key); */
	 //legend add ended
	
	
	
	
	//side bar    
	  ly_value = 340
	  lx_value = 200
	  legend = svg.selectAll(".legend")
	      .data(color.domain().slice())
	    .enter().append("g")
	      .attr("class", "legend")
	      .attr("transform", function(d, i) { return "translate(" + i * 120 + ",0)"; });

	  
	  legendRect=legend.append("rect")
	      .attr("y", ly_value)
	      .attr("x", lx_value-20)
	      .attr("width", 18)
	      .attr("height", 18)
	      .style("fill", color)
	      .on("click", function(){
                // Determine if current line is visible
                var active   = d.active ? false : true,
                newOpacity = active ? 0 : 1;
                // Hide or show the elements based on the ID
                d3.select("#tag"+d.key.replace(/\s+/g, ''))
                    .transition().duration(100)
                    .style("opacity", newOpacity);
                // Update whether or not the elements are active
                d.active = active;
                })




	 legendText =legend.append("text")
	      .attr("y", ly_value+10)
	      .attr("x", lx_value)
	      .attr("dy", ".35em")
	      .style("text-anchor", "start")
		  .style("font-size", "10px")
	      .text(function(d,i) { console.log("legendtext" + allSymbol[i]);  return allSymbol[i]; });
	
    });
    
    
  

    // Add the X Axis
    svg.append("g")
        .attr("class", "axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);

    // Add the Y Axis
    svg.append("g")
        .attr("class", "axis")
        .call(yAxis);

}
