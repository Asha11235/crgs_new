function stack_area(data, obj) {

var w=obj['w'];
var h=obj['h'];
var element=obj['element'];
var xLabel=obj['xLabel'];
var yLabel=obj['yLabel'];
var colorRange="";
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
if(deg==0) anchor= "middle";
else anchor="end";

if('barFontSize' in obj)
	tsize=obj['barFontSize'];
else tsize=12; 

if('extendHeight' in obj)
	bottombar=obj['extendHeight'];
else bottombar=40; 

if('color'  in obj)
   colorRange=obj['color'];
else colorRange = d3.scale.category20();


var color = d3.scale.ordinal()
    .range(colorRange);


var margin = {top: 40, right: 30, bottom: 30, left: 170},
    width = w - margin.left - margin.right,
    height = h - margin.top - margin.bottom;

var parseDate = d3.time.format("%b %Y").parse,
    formatPercent = d3.format(".0%");

var x = d3.time.scale()
    .range([0, width]);

var y = d3.scale.linear()
    .range([height, 0]);


if('xTextDegree'  in obj)
   deg=obj['xTextDegree'];
else deg="0";

//var color = d3.scale.category20();

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
	 .ticks(d3.time.months)
	.tickPadding(10)		  // Sets the distance of words from x axis 
   .innerTickSize(-height, 0, 0)
   .tickFormat(d3.time.format("%B"));	

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .tickFormat(formatPercent);
//.tickSize(-width, 0, 0)
  //    .ticks(5);

var area = d3.svg.area()
    .x(function(d) { return x(d.date); })
    .y0(function(d) { return y(d.y0); })
    .y1(function(d) { return y(d.y0 + d.y); });
var allvalue=[];
var stack = d3.layout.stack()
    .values(function(d,i) { return d.values;});

var svg = d3.select(element)
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


  color.domain(d3.keys(data[0]).filter(function(key) { return key !== "date"; }));

  data.forEach(function(d) {
    d.date = parseDate(d.date);
  });

  var browsers = stack(color.domain().map(function(name) {
    return {
      name: name,
      values: data.map(function(d,i) {

//console.log("******************"+i+"*****************************"+d.date +"*******************************************************");
        return {date: d.date, y: d[name] / 100};
      })
    };
  }));

  x.domain(d3.extent(data, function(d) { return d.date; }));

	
	//***Tool tip
	var tip = d3.tip()
	    .attr('class', 'd3-tip')
	    .offset([-10, 0])
	    .html(function(d,i) {
	       // return "<strong>Month: </strong> <span style='color:blue'>" +monthNames[d.date.getMonth()]  +"</span> </br> <strong> Rating:  </strong><span style='color:blue'>" + d.symbol +" satisfaction level "+d.y_value + "</span>";</br> <span style='color:gray'>% is</span> <span style='color:black'>  <strong> " +d.values[d.values.length - 2].y *100  + "%  </strong> </span>

	 return "<span style='color:gray'>Month: </span><span style='color:black'>  <strong> " + d.name +"  </strong> </span> ";

		});
	svg.call(tip);
  var browser = svg.selectAll(".browser")
      .data(browsers)
    .enter().append("g")
      .attr("class", "browser");

  browser.append("path")
      .attr("class", "area")
      .attr("d", function(d,i) {  return area(d.values); })
      .style("fill", function(d) { return color(d.name); })
.on("mouseout", function(d){
		 d3.select(this).style("fill", function(d) { return color(d.name); })
		tip.hide(d);
		})
	  .on("mouseover", function(d){
		 d3.select(this).style("fill", "gray");
		tip.show(d);
		});

var allname=[];
// TEXT NAME
/*
  browser.append("text")
      .datum(function(d) { return {name: d.name, value: d.values[d.values.length - 1]}; })
      .attr("transform", function(d) { return "translate(" + x(d.value.date) + "," + y(d.value.y0 + d.value.y / 2) + ")"; })
      .attr("x", -6)
	.style("font-weight", "normal")
	.style("font-size", tsize + "px")
      .attr("dy", ".35em")
      .text(function(d,i) { allname[i]=d.name ;return d.name; });
*/

//TEXT PERCENTAGE
 var browser = svg.selectAll(".browser")
      .data(browsers);


for(var j=0; j<3;j++)
{
 browser.append("text")
    // .attr("transform", function(d) { console.log("sdsss"+ d.values[j].date+"  "+ d.values[j].y+"   "+j); y0=d.values[j].y; return "translate(" + x(d.values[j].date) + "," + y(d.values[j].y+y0/2) + ")"; })
      .attr("x", -width+30)
	.style("font-weight", "normal")
	.style("font-size", tsize + "px")
      .attr("dy", ".35em")
      .text(function(d,i) { allname[i]=d.name ; return (d.values[j].y*100 ) +"%"; });
}


/*
 browser.append("text")
      .data(function(d) {
console.log(d.values.length+" kdxjhldhdhihdihihddddddddddddddddd"); return {name: d.name, value: d.values[d.values.length - 1]}; })
      .attr("transform", function(d) { return "translate(" + x(d.value.date) + "," + y(d.value.y0 + d.value.y / 2) + ")"; })
      .attr("x", -width+30)
	.style("font-weight", "normal")
	.style("font-size", tsize + "px")
      .attr("dy", ".35em")
      .text(function(d,i) { allname[i]=d.name ; return (d.value.y *100) +"%"; });
*/



  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis);
   
//side bar    
  ly_value = -10
  lx_value = -140
  legend = svg.selectAll(".legend")

      .data(color.domain().slice())
    .enter().append("g")
      .attr("class", "legend")
      .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });


  legendRect=legend.append("rect")
      .attr("y", ly_value)
      .attr("x", lx_value-20)
      .attr("width", 18)
      .attr("height", 18)
      .style("fill", color)


  legendText =legend.append("text")
      .attr("y", ly_value+10)
      .attr("x", lx_value)
      .attr("dy", ".35em")
      .style("text-anchor", "start")
	  .style("font-size", "10px")
      .text(function(d,i) { return allname[i]; });
};

