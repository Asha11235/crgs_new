    $(document).ready(function(){
       
      
        var pollId;
        var polls=new Array();
        
        $.ajax({
        
        type: "GET",
        url:  "/PollManagement/loadPoll",
        
        success: function(data) {
        
        var json = JSON.parse(data);
         var pollIdarray;
       
        if(json!="not publish"){
        
        var polllist = data.split(':');
        
        
        
        console.log(polllist.length); 
        var n=0;
        pollDesign(polllist,n);
                      
                                            
        }
        
        }
        
        });
       });
       
		
		function pollDesign(data,n){
		
			  	var startdiv =      ' <div class="col-md-12">'+
                        '<div class="landing-poll">'+
                            '<div class="poll-header">'+
                                'PARTICIPATE IN POLL'+ 
                            '</div>'+
                            '<div class="poll-body">'+
                               
                                    '<div class="form-body">'+
                                       ' <div class="form-group">'+
                                            '<label class="col-md-3 control-label"><strong>Age</strong></label>'+
                                            '<div class="col-md-9">'+
                                                '<div class="radio-list">'+
                                                
                                                    '<label>'+
                                                        '<input type="radio" class="ageClass" name="age" value="1"> Below 18'+
                                                    '</label>'+
                                                    '<label>'+
                                                        '<input type="radio" class="ageClass" name="age" value="2"> Above 18'+
                                                    '</label>'+
                                                '</div>'+
                                              '</div>'+
                                       '</div>'+
                                     '<div class="form-group" >'+
                                            '<label class="col-md-3 control-label"><strong>Gender</strong></label>'+
                                            '<div class="col-md-9">'+
                                               ' <div class="radio-list">'+
                                                    '<label>'+
                                                        '<input type="radio" class="genderClass" name="gender" value="1"> Male'+
                                                    '</label>'+
                                                    '<label>'+
                                                        '<input type="radio" class="genderClass" name="gender" value="2"> Female'+
                                                    '</label>'+
                                                    '<label>'+
                                                        '<input type="radio" class="genderClass" name="gender" value="3"> Other'+
                                                    '</label>'+
                                                '</div>'+
                                            '</div>'+
                                        '</div>'+
                                        '<div class="form-group"  id="pollmanage">';

                   
				var pollInfo = data[n].split(';');
		        var optionarray;
		         for (var i = 1; i < pollInfo.length; i = i + 6) {
                                var title = pollInfo[i];
                                var questionType = pollInfo[i+1];
                                var startDate = pollInfo[i+2];
                                var endDate = pollInfo[i+3];
                                var options = pollInfo[i+4]
                                var poll = pollInfo[i+5];
                               // var todayDate=pollInfo[i+6];
                                //console.log(todayDate);
                                
                                
                             
                                startdiv += ' <h4> ' + title + '</h4>'+
                                       ' </div> ' +
                                       '<div class="form-group">'+
                                            '<div class="radio-list">';
                                            
                                            
                                            
                                            
                       if(questionType=="Single Select"){
                                       
                                     var option = options.split(',');
                                      
                                      //console.log("option: "+ option.length);
                                       for( var j=1; j< option.length ; j++){
                                   startdiv+= '<input type="radio" class="answer" name="answerr" value=" ' + option[j] +'"> ' +option[j] + ' <br>'+
                                               ' <input name="pollId" type="hidden" class="form-control" id="pollIdd" value="' + poll +' ">';
                                               console.log(poll);
 
                                      }
                                     
                                  
                                      }
                                      
                                      
                                      
                        else if(questionType=="Multiple Select"){
                                        var option = options.split(',');
                                //console.log("option: "+ option.length);
                               var option = options.split(',');
                               for( var j=1; j< option.length ; j++){
                                   startdiv+= '<input type="checkbox" class="answer" name="answerr" value=" ' + option[j] +'"> ' +option[j] + ' <br>'+
                                               ' <input name="pollId" type="hidden" class="form-control" id="pollIdd" value="' + poll +' ">';
                                                console.log(poll);
                                      }
                                 
                               }
                               
                                  startdiv +=   '</div>'+
                                       ' </div>'+
                                      ' <div class="form-group">'+
                                            '<div class="col-md-5">'+
                                                '<button type="submit" id="submitBtn" class="btn btn-sm black-background">VOTE</button>'+
                                            '</div>'+
                                            '<div class="col-md-7">'+
                                               '<button type="submit" id="previousBtn" class="btn btn-sm black-background">PREVIOUS</button>'+
                                            '</div>'+
                                        '</div>'+

                                        

                                        '<div class="form-group">'+
                                           ' <label class="col-md-offset-3 control-label">'+
                                                '<input type="hidden" class="btn btn-default btn-xs"></input>'+
                                                '<input type="hidden" class="btn btn-default btn-xs"></input>'+
                                                '<input type="hidden" class="btn btn-default btn-xs"></input>'+
                                                '<input type="hidden" class="btn btn-default btn-xs"></input>'+
                                            '</label>'+
                                        '</div>'+
                                    '</div>'+    
                                        '</div>'+
                                      '</div>'+
                               
                            '</div>'+
                        '</div>'+
                    '</div>';
                                    
                    $("#replace").html("");
                    $("#replace").html(startdiv);                     
                    
                    var polliiid =  $('#pollIdd').val();
	         
	             $("#submitBtn").on("click",function(){
	    
	               console.log("clickS");
					  var gender =  $('.genderClass').val();
						
					  var age = $('.ageClass').val();
					  
					  var option=new Array();
					  
					  
					   $(".answer:checked").each(function(){
                                     option.push($(this).val());
					  });
					  
					  //console.log("checkedvalue: "+option);
					  
					  var pollId = $('#pollIdd').val();
					  
					  console.log(gender +" " + age +" " + option +" " + polliiid);
					  
					  savePollReply(gender,age,option , polliiid);
	             });
                
                   $("#previousBtn").on("click",function(){ 
                   
                   console.log("clickS");
                   n++;
                   if(n> data.length-1){
                     n=0;
                     pollDesign(data,n);
                   }
                   else if(n < data.length-1){
                   
                     pollDesign(data,n);
                   }
                  
                  });   
                                      
                                           
             }  
   
		}
		
		 function savePollReply(gender,age,options,pollId) {
		
		var option = JSON.stringify(options);
		  console.log(gender +" " + age +" " + options +" " + pollId);
		return $.ajax({
				type: "POST",
              	url:  "/PollManagement/voteReply",
				data : {
					gender : gender,
					age : age,
					option : option,
					pollId : pollId
				},
				
				success: function(data) {
				
				console.log("value saved");
				}
			});
		
		}
		
		