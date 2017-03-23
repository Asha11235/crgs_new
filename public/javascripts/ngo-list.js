      //$('.delete_user').("click", function(){
	    $(".delete_ngo").click(function(){  
			var ngoId = $(this).attr("id");
	        if(confirm("Are you sure to delete this user?"))
	        {
	            $.ajax({
	                type: "GET",
	              	url:  "@{Ngos.delete}",
	                data: {
	                	id: ngoId
	                },
	                success: function(data) {
						console.log(data);
	                    if(data == 1){
	                    	$('#delete_msg').html("<h3 style="+"color:green"+">User deleted successfully.</h3>");
	                    	$("#"+ngoId).remove(); //row remove when deleted 
	                     	
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
		

		console.log("divisionId : "+ divisionId + " districtId : " + districtId + " UpazillaId : "+ upazillaId);
		loadData(divisionId,districtId,upazillaId );
	});
	
	

	function loadData(divisionId,districtId,upazillaId) {
		var formId = $("#formId").val();
		//var tbl = "<table class="table table-striped table-bordered table-hover" id="userTable">";
		var tbl = "" ;
        var thead =  "<table class='table table-striped table-bordered table-hover' id='ngoTable'>"+
                                    "<thead>"+
                                        "<tr>"+
                                            "<th>NGO Name</th>"+
                                            "<th>Division</th>"+
                                            "<th>District</th>"+
                                            "<th>Upazilla</th>"+
                                            "<th>&nbsp;</th>"+
                                            "<th>&nbsp;</th>"+
                                            "<th>&nbsp;</th>"+
                                        "</tr>"+
                                    "</thead>";           
		return $.ajax({
				type: "GET",
              	url:  "/ngos/loadNgo",
				data : {
					divisionId : divisionId,
					districtId : districtId,
					upazillaId : upazillaId
				},
				
				success: function(data) {
					var json = JSON.parse(data);
					var ngoInfo = data.split(';');
					 console.log(json);
					 var row = "";
		             //var tbl = "";
					 for (var i = 1; i < ngoInfo.length; i = i + 4) {
                                var ngoname = ngoInfo[i];
                                var divisionname = ngoInfo[i+1];
                                var districtname = ngoInfo[i+2];
                                var upazillaname = ngoInfo[i+3];
                                
                           row = row + "<tr><td>" + ngoname + "</td><td>" + divisionname + "</td><td>" + districtname + "</td><td>" + upazillaname + 
                           "</td>" +
                           "#{deadbolt.externalizedRestriction externalRestrictions:['Edit NGO']}"+
						   "<td><a class='label label-sm label-warning' href='@{Ngos.details(ngo.id)}'> Details</a></td>"+
				           "<td><a href='@{Ngos.initEdit(ngo.id)}' class='label label-sm label-warning'> Edit</a></td>"+
						   "<td><a class='label label-sm label-warning delete_ngo' id='${ngo.id}'> Delete</a></td>"+
						   "#{/deadbolt.externalizedRestriction}</tr>";
                           
                           
                     }
                            tbl =thead+ tbl+"<tbody>" + row + "</tbody>";
                            $("#sample_2").html("");
                            $("#sample_2").html(tbl);         
                                
                }
			
			});
	}
	});
