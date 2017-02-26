$(function(){
	$('#data\\/is_stu_present_meeting').on('click',function(){
  	var div = $("#is_stu_present_meeting");
	div.html();
	div.attr('value','');
	div.attr('name','');
	var anotherText = "";
  
  	$.msgbox({ 
  	'message'  : 'দয়া করে নির্দিষ্ট প্রশ্নের জন্য নিচের  ৩ টি কারণ নির্বাচন করুন', 
 	'type'     : 'prompt', 
  	'inputs'   : [{'type' : 'checkbox', 'label' : 'ছাত্রছাত্রীদের অংশগ্রহণের সুযোগ নাই', 'name' : 'Student are not eligible to participate', 					'value' : 'ছাত্রছাত্রীদের অংশগ্রহণের সুযোগ নাই' },
  		      {'type' : 'checkbox', 'label' : 'মিটিং/ সভার পূর্বে ছাত্রছাত্রীদের মতামত নেওয়া হয়েছে','name' : 
 				'Student opinion have taken before the meeting','value':
				'মিটিং/ সভার পূর্বে ছাত্রছাত্রীদের মতামত নেওয়া হয়েছে'},
  		      {'type' : 'checkbox', 'label' : 'মিটিং সভায় ছতারছাত্রিদের অংশগ্রহণের প্রয়োজন নাই','name' : 
				'Student’s participation is not needed in these meeting','value':
				'মিটিং সভায় ছতারছাত্রিদের অংশগ্রহণের প্রয়োজন নাই'},
		      {'type' : 'checkbox', 'label' : 'স্কুল ছুটির পর মিটিং হয়েছে','name' : 
				'The meeting held at vacation/after close of school','value':'স্কুল ছুটির পর মিটিং হয়েছে'},
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
					$('#data\\/is_stu_present_meeting' ).trigger( "click");
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
					'inputs'   : [{'type' : 'text', 'label' : 'অন্যান্য কারণ','name' :'observation','value' : ''}],
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