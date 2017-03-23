    //$('.delete_user').("click", function(){
	    $(".delete_user").click(function(){  
			var userId = $(this).attr("id");
	        if(confirm("Are you sure to delete this user?"))
	        {
	            $.ajax({
	                type: "GET",
	              	url:  "@{Users.delete}",
	                data: {
	                	id: userId
	                },
	                success: function(data) {
						console.log(data);
	                    if(data == 1){
	                    	$('#delete_msg').html("<h3 style="+"color:green"+">User deleted successfully.</h3>");
	                    	$("#"+userId).remove(); //row remove when deleted 
	                     	
	                    }
	                    else{
	                    	$('#delete_msg').html("<h3 style="+"color:red"+">User cannot be deleted.</h3>");
	                    }
	                }


	            });
	           
	        }
	        return false;
	         
	    });
	    
	    $("document").ready(function(){
	    
	   $('#sample_2').DataTable();
	
	$(".SEARCH").on("change", function() {
		
		if(typeof $(this).find(":selected").attr("value") == "undefined"){
			return;
		}
		var divisionId = $('#division').find(":selected").attr("value");
		
		var districtId = $('#ngo\\.geoDistrict\\.id').find(":selected").attr("value");
		
		var upazillaId = $('#ngo\\.geoUpazilla\\.id').find(":selected").attr("value");
		
		var schoolId = $('#school').find(":selected").attr("value");
		
		var roleId = $('#roleId').find(":selected").attr("value");
		
		
		console.log("divisionId : "+ divisionId + " districtId : " + districtId + " UpazillaId : "+ upazillaId
				+ " schoolId : "+ schoolId + " roleId : " + roleId);
		loadData(divisionId,districtId,upazillaId,schoolId, roleId, null, null);
	});
	
	

	function loadData(divisionId,districtId,upazillaId,schoolId, roleId, startDate, endDate) {
		var formId = $("#formId").val();
		//var tbl = "<table class="table table-striped table-bordered table-hover" id="userTable">";
		var tbl = "" ;
        var thead =  "<table class='table table-striped table-bordered table-hover' id='userTable'>"+
                                    "<thead>"+
                                        "<tr>"+
                                            "<th>User ID</th>"+
                                            "<th>User Name</th>"+
                                            "<th>Role</th>"+
                                            "<th>Division</th>"+
                                            "<th>District</th>"+
                                            "<th>Upazilla</th>"+
                                            "<th>Institution</th>"+
                                            "<th>&nbsp;</th>"+
                                            "<th>&nbsp;</th>"+
                                            "<th>&nbsp;</th>"+
                                        "</tr>"+
                                    "</thead>";           
		return $.ajax({
				type: "GET",
              	url:  "/users/loadUser",
				data : {
					divisionId : divisionId,
					districtId : districtId,
					upazillaId : upazillaId,
					schoolId : schoolId,
					roleId : roleId,
					startDate : startDate,
					endDate : endDate
				},
				
				success: function(data) {
					var json = JSON.parse(data);
					var userInfo = data.split(';');
					 console.log(json);
					 var row = "";
		             //var tbl = "";
					 for (var i = 1; i < userInfo.length; i = i + 7) {
                                var schoolid = userInfo[i];
                                var username = userInfo[i+1];
                                var rolename = userInfo[i+2];
                                var divisionname = userInfo[i+3];
                                var districtname = userInfo[i+4];
                                var upazillaname = userInfo[i + 5];
                                var schoolname = userInfo[i + 6];
                                
                           row = row + "<tr><td>" + schoolid + "</td><td>" + username + "</td><td>" + rolename + 
                           "</td><td>" + divisionname + "</td><td>" + districtname + "</td><td>" + upazillaname + 
                           "</td><td>" + schoolname + "</td>"+
                           "#{deadbolt.externalizedRestriction externalRestrictions:['Edit User']}"+
						   "<td><a class='btn btn-mini' href='@{Users.details(user.id)}'>Details</a></td>"+
				           "<td><a class='btn btn-mini' href='@{Users.edit(user.id)}'><i class='icon-edit'></i> Edit</a></td>"+
						   "<td>"+
						   "<a class='btn btn-mini btn-danger delete_user' id = '${user.id}' onClick='deleteFunction()' ><i class='icon-remove icon-white'></i> Delete</a>"+
						   "<i class='icon-remove icon-white'></i>"+ 
						   "Delete"+
						   "</a>"+
						   "</td>"+
						   "#{/deadbolt.externalizedRestriction}"+
						   "</td></tr>";
                           
                           
                     }
                            tbl =thead+ tbl+"<tbody>" + row + "</tbody>";
                            $("#sample_2").html("");
                            $("#sample_2").html(tbl);         
                                
                }
			
			});
	}
	});
