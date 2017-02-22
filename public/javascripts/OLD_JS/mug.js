$(function(){
	$('#data\\/mug').on('click',function(){
  	var div = $("#mug");
	div.html();
	div.attr('value','');
	div.attr('name','');
	var anotherText = "";
  
  	$.msgbox({ 
  	'message'  : 'দয়া করে নির্দিষ্ট প্রশ্নের জন্য নিচের  ৩ টি কারণ নির্বাচন করুন', 
 	'type'     : 'prompt', 
  	'inputs'   : [{'type' : 'checkbox', 'label' : 'ছাত্রছাত্রীরা নষ্ট করেছে', 'name' : 
				'Student demolish these utensils', 'value' : 'ছাত্রছাত্রীরা নষ্ট করেছে' },
  		      {'type' : 'checkbox', 'label' : 'পর্যাপ্ত বাজেট নাই তাই এই মাস এ কেনা হয়নি','name' : 
				'Didn’t bought due to unavailability of budget','value':
				'পর্যাপ্ত বাজেট নাই তাই এই মাস এ কেনা হয়নি'},
  		      {'type' : 'checkbox', 'label' : 'বাবস্থাপনা কমিটিকে জানানো হয়েছে কিন্তু এখনো বাবস্থা নেয়নি','name' : 
				'SMC have been informed but no decision made yet','value':
				'বাবস্থাপনা কমিটিকে জানানো হয়েছে কিন্তু এখনো বাবস্থা নেয়নি'},
		      {'type' : 'checkbox', 'label' : 'দায়িত্ব প্রাপ্ত লোকের অবহেলা','name' : 
				'Negligence by the responsible person','value':'দায়িত্ব প্রাপ্ত লোকের অবহেলা'},
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
					$('#data\\/mug' ).trigger( "click");
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
