
//*************************************************************
//***********************Pie chart*****************************
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


function pie_chart(data,obj) {

var w=obj['w'];
var h=obj['h'];
var element=obj['element'];
var xLabel=obj['xLabel'];
var yLabel=obj['yLabel'];
var x_legend=obj['legend_x'];
var y_legend=obj['legend_y'];
var translate_x = obj['translate_x'];
var rad_denominator = obj['rad_denominator'];
var afterColor="gray";
var tsize=12;

if('barFontSize' in obj)
	tsize=obj['barFontSize'];
else tsize=12; 

if('afterColor'  in obj)
   afterColor=obj['afterColor'];
else afterColor="gray";




var width = w,
    height = h,
    radius = Math.min(width, height) / rad_denominator;
var total=0;
	


var colorRange="";
var color="";
if('color'  in obj)
{
   colorRange= obj['color'];
   color = d3.scale.ordinal()
   	 .range(colorRange);
}
else {
	colorRange = d3.scale.category20b();
	var color = d3.scale.ordinal()
   	    .range(colorRange);
}


	
	
	
var arc = d3.svg.arc()
    .outerRadius(radius - 10)
    .innerRadius(0);

	
var pie = d3.layout.pie()
    .sort(null)
    .value(function(d) { return d.number; });

//d3.select("#pie_chart").selectAll("svg").remove();
var svg = d3.select(element)
    .attr("width", width)
    .attr("height", height+10)
  .append("g")
    .attr("transform", "translate(" + (translate_x) + "," + height / 2 + ")");

//**************TIP***************
var tip = d3.tip()
    .attr('class', 'd3-tip')
    .offset([-10, 0])
    .html(function(d) {
 return "<span style='color:gray'>Meassure Name: </span><span style='color:black'>  <strong> " +  d.data.tag+"  </strong> </span> </br> <span style='color:gray'>Meassure Value</span> <span style='color:black'>  <strong> " + d.data.number + "  </strong> </span> </br><span style='color:gray'> Total Value: <span style='color:black'> <strong> " + total + " </strong> </span></br><span style='color:gray'> % is:  <span style='color:black'> <strong> " + d.pData+ " </strong> </span>";
});
svg.call(tip);

  data.forEach(function(d) {
    d.number = +d.number; //type casting
    total+=d.number;
  });
  var g = svg.selectAll(".arc")
      .data(pie(data))
    .enter().append("g")
      .attr("class", "arc");

  g.append("path")
      .attr("d", arc)
      .style("fill", function(d) { return color(d.data.tag); })
      .on("mouseover", function(d,i){
	       d3.select(this).style("fill", afterColor[i]);
		   tip.show(d);
		})
	  .on("mouseout",function(d){
		
	      d3.select(this).style("fill", color(d.data.tag)); 
		  tip.hide(d);
		});

   //add percentage
  g.append("text")
      .attr("transform", function(d) {
 return "translate(" + (arc.centroid(d)) + ")"; })
      .attr("dy", ".35em")
      .style("text-anchor", "middle")
	  .style("font-size", 16+ "px")
	   .style("font-weight", "normal")
.style("font-size", tsize + "px")
      .text(function(d) { 
	 var percen= (100*d.data.number)/total;
	 percen=Number((percen).toFixed(1));
         d.pData=percen+"%";
	 return percen +"%"; });
 
  var ly_value = -120
  var lx_value = 300
  if(x_legend != lx_value){
	  lx_value = x_legend ;
  }
  if(y_legend != ly_value ){
	  ly_value  = y_legend ;
  }
  
  var legend = svg.selectAll(".legend")
      .data(pie(data))
    .enter().append("g")
      .attr("class", "legend")
      .attr("transform", function(d, i) {return "translate(0," + (i * 20) + ")"; });

  legendRect=legend.append("rect")
      .attr("x",lx_value-20)
	  .attr("y",ly_value)
      .attr("width", 18)
      .attr("height", 18)
      .style("fill", function(d) {return color(d.data.tag)});
	
  //**************legendText is public here.**********
  legendText =legend.append("text")
      .attr("x", lx_value)
      .attr("y", ly_value+10)
      .attr("dy", ".35em")
      .style("text-anchor", "start")
	  .style("font-size", "10px")
      .text(function(d) { return d.data.tag; });	 
};
