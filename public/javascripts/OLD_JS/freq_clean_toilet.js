$(function(){
	$('#data\\/freq_clean_toilet').on('click',function(){
  	var div = $("#freq_clean_toilet");
	div.html();
	div.attr('value','');
	div.attr('name','');
	var anotherText = "";
  
  	$.msgbox({ 
  	'message'  : 'দয়া করে নির্দিষ্ট প্রশ্নের জন্য নিচের  ৩ টি কারণ নির্বাচন করুন', 
 	'type'     : 'prompt', 
  	'inputs'   : [{'type' : 'checkbox', 'label' : 'পরিচ্ছনতা কর্মী নাই', 'name' : 'No WASH worker available', 'value' : 'পরিচ্ছনতা কর্মী নাই' },
  		      {'type' : 'checkbox', 'label' : 'পানি সরবরাহ নাই','name' : 'No water supply','value':'পানি সরবরাহ নাই'},
  		      {'type' : 'checkbox', 'label' : 'কাউকে দায়িত্ব দেয়া হয়নি','name' : 'Not assigned any person for it','value':
				'কাউকে দায়িত্ব দেয়া হয়নি'},
		      {'type' : 'checkbox', 'label' : 'প্রয়োজনীয় উপকরন নাই','name' : 'Shortage of necessary materials','value':
				'প্রয়োজনীয় উপকরন নাই'},
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
					$('#data\\/freq_clean_toilet' ).trigger( "click");
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
  							//result.pop();
							anotherArray = result;
							Array.prototype.push.apply(checkArray, anotherArray);
  			 		 		for(var j in checkArray){
  			 		 		outputText = outputText + checkArray[j].value +',';
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
