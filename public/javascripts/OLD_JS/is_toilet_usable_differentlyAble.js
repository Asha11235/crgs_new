$(function(){
	$('#data\\/is_toilet_usable_differentlyAble').on('click',function(){
  	var div = $("#is_toilet_usable_differentlyAble");
	div.html();
	div.attr('value','');
	div.attr('name','');
	var anotherText = "";
  
  	$.msgbox({ 
  	'message'  : 'দয়া করে নির্দিষ্ট প্রশ্নের জন্য নিচের  ৩ টি কারণ নির্বাচন করুন', 
 	'type'     : 'prompt', 
  	'inputs'   : [{'type' : 'checkbox', 'label' : 'টয়লেট নির্মাণের সময় বা ডিজাইন এর সময় রাখা হয়নি', 'name' : 
				'Constructed of the toilet wasn’t disable friendly', 'value' : 'টয়লেট নির্মাণের সময় বা ডিজাইন এর সময় রাখা হয়নি'},
  		      {'type' : 'checkbox', 'label' : 'টয়লেট নির্মাণ প্রক্রিয়াধীন আছে','name' : 
				'Construction of disable friendly toilet is under process','value':'টয়লেট নির্মাণ প্রক্রিয়াধীন আছে'},
  		      {'type' : 'checkbox', 'label' : 'বাজেট বরাদ্ধ নাই','name' : 'No available budget allocation','value':'বাজেট বরাদ্ধ নাই'},
		      {'type' : 'checkbox', 'label' : 'সংশ্লিষ্ট বিষয় এ ধারণা নাই','name' : 'Not clear about the issue','value':'সংশ্লিষ্ট বিষয় এ ধারণা নাই'},
 		      {'type' : 'checkbox', 'label' : 'অন্যান্য','name' : 'Others','value':'অন্যান্য'}], 
  	'buttons'  : [{'type' : 'yes', 'value' : 'OK'}], 
 	 'callback' : function(result){
  				var checkArray = [];
  				var anotherArray = [];
  				var outputText = "";
  				var sendServer = "";
				var sendName = "";
  				result.pop();
  				//result.pop();
  				checkArray = result;
				if(checkArray.length > 3 || checkArray.length == 0){
					$('#data\\/is_toilet_usable_differentlyAble' ).trigger( "click");
				}
				else{
  			 	for(var i in result) {
  			 		if(result[i].name === "Others"){
					anotherText = 1;
					div.html();
					div.attr('value','');
  					$.msgbox({
  					'message'  : 'অন্যান্য কারণ সমূহ :', 
					'type'     : 'prompt',
					'inputs'   : [{'type' : 'text', 'label' : 'অন্যান্য কারণ','name' :'observation','value' : '' }],
					'buttons'  : [{'type' : 'yes', 'value' : 'OK'}], 
					'callback' : function(result) {
							outputText = "";
							sendServer = "";
					        	result.pop();
  							//result.pop();,{'type' : 'close', 'value' : 'Exit'}
							anotherArray = result;
							Array.prototype.push.apply(checkArray, anotherArray);
  			 		 		for(var j in checkArray){
  			 		 		outputText = outputText + checkArray[j].value +",";
  			 		 		sendServer = sendServer + checkArray[j].value + "*";
							sendName = sendName  + checkArray[j].name + "*";
  			 		 		}
  			 		 		div.html(outputText);
  			 		 		div.attr('value',sendServer);
							div.attr('name',sendName);
							}
							});
							}
					else{
						if(anotherText != 1){
						outputText = outputText + checkArray[i].value +",";
  			 		 	sendServer = sendServer + checkArray[i].value + "*";
						sendName = sendName  + checkArray[i].name + "*";
						}
					}
  			 		 //console.log(i +"-i-"+ result[i].name + ':' + result[i].value ); 
  			 		 }
					if(anotherText != 1){
						div.html(outputText);
	  			 		div.attr('value',sendServer);
						div.attr('name',sendName);
					}
  				} 
			}
		}); 
	});


});
