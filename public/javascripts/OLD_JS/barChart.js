//********************BAR CHART**********************************
//**************Data source: index.php -> data******************
//svg tag is neeed in index file with .chart class >>>>> ex: <svg class="chart"></svg>
//1. Parameter name: data object
	// In Data Object-> 2 var
	//1. y_data: itz number 
	//2. x_data:  itz string
//2. Parameter name: w -> width of the graph
//3. Parameter name: h -> height of the graph
//4. Parameter name: element ->html element name/id in which graph will be contained.
//5. Parameter name: xLabel->string, graph x axis Label
//6. Parameter name: yLabel-> string, graph y axis Label 

function bar_chart_single(data,obj) {
//var w=850,h=330,element="#body",xLabel="x values",yLabel="y values";
var w=obj['w'];
var h=obj['h'];
var element=obj['element'];
var xLabel=obj['xLabel'];
var yLabel=obj['yLabel'];
var colorRange=obj['color'];
var afterColorRange="";
var deg=0;
var bottombar=40;
var  xLabelTranslate=0;


if('xLabelTranslate'  in obj)
	xLabelTranslate=obj['xLabelTranslate'];



if('afterColor'  in obj)
   afterColorRange=obj['afterColor'];
else afterColorRange=colorRange;

if('xTextDegree'  in obj)
   deg=obj['xTextDegree'];
else deg="0";
var anchor="middle";
if(deg==0) anchor="middle";
else anchor="middle";

if('barFontSize' in obj)
	tsize=obj['barFontSize'];
else tsize=12; 

if('extendHeight' in obj)
	bottombar=obj['extendHeight'];
else bottombar=40; 


//by default itz true>>>> % will be visible
var percentage="true";
if('percentage' in obj)
	percentage=obj['percentage'];
else percentage="true"; 

//If there is a special Bar, which is different color than others, use this object. This is only for 1 bar,
//Only specily x_value and color of that bar
var diffXDataColor=null;
var diffX=null;
var diffXColor=null;

if('diffXDataColor' in obj)
{	diffXDataColor=obj['diffXDataColor'];
	diffX=diffXDataColor[0];
	diffXColor=diffXDataColor[1];
}
else diffXDataColor=null; 




//For Many different color bar, Specify The starting number of the bar from where different color will be started as well as color
var diffXStartPoint=null;
var StartPoint=1000000;
var StartPointColor=-1;

if('diffXStartPoint' in obj)
{	diffXStartPoint=obj['diffXStartPoint'];
	StartPoint=diffXStartPoint[0] -1;
	StartPointColor=diffXStartPoint[1];
}
else StartPointColor=null; 
//console.log("yes " + diffXStartPoint);

var total=0; // total for percentage  calculation
var margin = {top: 20, right: 10, bottom: 30, left: 10},
    width = w- margin.left - margin.right,
    height = h - margin.top - margin.bottom;

var x = d3.scale.ordinal()
    .rangeRoundBands([0, width], .6);

var y = d3.scale.linear()
    .range([height, 0]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var color = d3.scale.ordinal()
    .range(colorRange);



var afterColor = d3.scale.ordinal()
    .range(afterColorRange);


var yAxis = d3.svg.axis()
    .scale(y)
.orient("left") 
	.innerTickSize(-width)
    .outerTickSize(0)
    .tickPadding(10)
//.ticks(10,"%");

var chart = d3.select(element).append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom+ bottombar)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//tooltip
var tip = d3.tip()
    .attr('class', 'd3-tip')
    .offset([-10, 0])
    .style("left", "px")
    .html(function(d) {
        return "<strong>Age:  </strong> <span style='color:blue'>" + d.x_data+"</span> </br> <strong> Number of member:  </strong><span style='color:blue'>" + d.y_data + "</span> </br> <strong> Total:  </strong><span style='color:blue'>" + total + "</span>";

  //return "<span style='color:gray'>Meassure Name: </span><span style='color:black'>  <strong> " + d.x_data+"  </strong> </span> </br> <span style='color:gray'>Meassure Value</span> <span style='color:black'>  <strong> " +Number((d.y_data)).toFixed(1)+ "  </strong> </span> </br><span style='color:gray'> Total Value: <span style='color:black'> <strong> " + total.toFixed(1) + " </strong> </span></br><span style='color:gray'> % is:  <span style='color:black'> <strong> " + d.pData+ " </strong> </span>"; 


return "<span style='color:gray'>Measure Name: </span><span style='color:black'>  <strong> " + d.x_data+"  </strong> </span></br><span style='color:gray'> Value:  <span style='color:black'> <strong> " + d.pData+ " </strong> </span>"; 

});
chart.call(tip);
    //console.log("Hello  " + d3.max(data, function(d,i) { total+=d.y_data[i]; return d.y_data[i]; }));
  
  x.domain(data.map(function(d) { return d.x_data; }));

  y.domain([0, d3.max(data, function(d,i) { total+=+d.y_data;  return +d.y_data; })]);
  //console.log("*******TOTAL  "+ total);
  chart.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
	.selectAll("text") 
            .style("text-anchor", anchor)
            .style("font-size", 12 + "px")
            //.attr("dx", "-.8em")
            .attr("dy", ".55em")
	    .attr("x", 10)
            .attr("transform", "rotate(-"+deg+")" );
     // .call(wrap, x.rangeBand());

  chart.append("g")
      .attr("class", "y axis")
.attr("transform", "translate(" + 20 + ",0)")
      .call(yAxis);
  

  chart.selectAll(".bar")
      .data(data)
    .enter().append("rect")
      .attr("class", "bar")

      .style("font-size", tsize + "px")
      .attr("x", function(d) { return x(d.x_data); })
      .attr("y", function(d) { return y(d.y_data); })
      .attr("height", function(d) { return height - y(d.y_data); })
      .style("fill", function(d,i) 
		{  
		if(i >= diffXStartPoint)  return "#FF0000";
		  else return color(d.x_data); 
		})
      .attr("width", x.rangeBand())
	  .on("mouseout", function(d,i){
		var clr=0;
		if(d.x_data==diffX) clr= diffXColor;
		else if(i>=StartPoint) clr=StartPointColor 
		else  clr=color(d.x_data);
		//d3.select(this).style("fill",color(d.x_data));		
		d3.select(this).style("fill", clr);
		//legendText.style("fill", "black");
		tip.hide(d);
		})
	  .on("mouseover", function(d,i){
		var clr=0;
		//d3.select(this).style("fill",  afterColor(d.x_data));
		if(d.x_data==diffX) clr= "gray"; 
		else if(i>=StartPoint) clr="gray";
		else  clr=afterColor(d.x_data);
		d3.select(this).style("fill", clr );
		//legendText.style("fill", "blue");
		tip.show(d);
		});
		var xv=0;
//var tsize=0.05*h;
chart.selectAll(".bartext")
.data(data)
.enter()
.append("text")
.attr("class", "bartext")
.attr("text-anchor", "middle")
.attr("fill", "black")
.style("font-size", tsize + "px")
.attr("x", function(d,i) {
    return x(d.x_data)+x.rangeBand()/2;
})
.attr("y", function(d,i) {
    return y(d.y_data)- 6; // previously 20
})
.text(function(d){ //percentage calculation here :)
	 //var percen= (100*d.y_data)/total;
	 //xv +=percen;
	 percen=Number((d.y_data)).toFixed(2);
	//percen= Math.round(percen);
	if(percentage=="false")
	{
		d.pData=percen;
		return percen;
	}
	else 
	{	d.pData=percen;
	 	return percen;
	}
});

//X axis label
chart.append("text")
        .attr("transform", "translate(" + ((width / 2)+ xLabelTranslate) + " ," + (height + margin.bottom+ bottombar-10) + ")")
.style("fill", "steelblue")
        .style("text-anchor", "middle")
	.style("font-size", "12px")
        .text(xLabel);

//Y asis Label
chart.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin.left+60)
        .attr("x",0 - (height / 2))
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .text(yLabel);
function type(d) {
  d.y_data = +d.y_data; // coerce to y_data
  return d;
}

//For Wraping Text in x axis . This part is totally taken from here>> bl.ocks.org/mbostock/7555321 . no idea whats going on! <PERSIA> 
function wrap(text, width) {
  text.each(function() {
    var text = d3.select(this),
	//y = text.attr("y"),
        //dy = parseFloat(text.attr("dy")),
 	//tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em");
 	//y = text.attr("y"),
       /* words = text.text().split(/\s+/).reverse(),
        word,
        line = [],
        lineNumber = 0,
        lineHeight = 1, // ems
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

        tspan = text.append("tspan").attr("x", 0-20).attr("y", y-10).attr("dy", ++lineNumber * lineHeight + dy + "em").text(word);
	
      }

    }*/
tspan = text.append("tspan").attr("x", 40).attr("y", y).text(text.text());
	
  });
}

}
