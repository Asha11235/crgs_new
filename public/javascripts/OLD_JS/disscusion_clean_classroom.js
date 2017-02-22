$(function(){
	$('#data\\/disscusion_clean_classroom').on('click',function(){
  	var div = $("#disscusion_clean_classroom");
	div.html();
	div.attr('value','');
	div.attr('name','');
	var anotherText = "";
  
  	$.msgbox({ 
  	'message'  : 'দয়া করে নির্দিষ্ট প্রশ্নের জন্য নিচের  ৩ টি কারণ নির্বাচন করুন', 
 	'type'     : 'prompt', 
  	'inputs'   : [{'type' : 'checkbox', 'label' : 'দায়িত্বপ্রাপ্ত শিক্ষক নাই', 'name' : 'Absence of responsible teacher', 'value' : 
				'দায়িত্বপ্রাপ্ত শিক্ষক নাই' },
  		      {'type' : 'checkbox', 'label' : 'স্কুল এর চলমান পরীক্ষার কারনে বাস্ততা','name' : 'Busy due to running examination','value':
				'স্কুল এর চলমান পরীক্ষার কারনে বাস্ততা'},
  		      {'type' : 'checkbox', 'label' : 'দায়িত্ব প্রাপ্ত লোকের অবহেলা','name' : 'Negligence of responsible parson','value':
				'দায়িত্ব প্রাপ্ত লোকের অবহেলা'},
		      {'type' : 'checkbox', 'label' : 'কি কি সম্পর্কে আলোচনা করতে হবে সে সম্পর্কে ধারণা না থাকা','name' : 
				'Not clear concept about the discussion points','value':'কি কি সম্পর্কে আলোচনা করতে হবে সে সম্পর্কে ধারণা না থাকা'},
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
					$('#data\\/disscusion_clean_classroom' ).trigger( "click");
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
