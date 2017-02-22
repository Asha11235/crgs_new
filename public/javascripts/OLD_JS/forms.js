$(document).ready(function() {
console.log( "ready!" );
/*
var tableData = [
                 [
	            "Tiger Nixon",
	            "System Architect",
	            "Edinburgh"
	          ],
	          [
	            "Garrett Winters",
	            "Accountant",
	            "Tokyo"
	          ]
             ];
console.log(tableData);
/*$('#example').DataTable( {
	"data":tableData,
	"columns": ["id", "name", "price"],
    "dataType": "jsonp"
} );*/
	$('#search_school').live("blur", function() { 
		var search_string = $(this).val();
		$.ajax({
			cache:false,
            async:false,
             type : 'GET',
             //url : 	"@{Forms.searchSchoolList}",
             url: '/searchSchool',
             data : {search_string:search_string},
             success:function(data){
            	 $('#schoolDiv').html(data);
             }
			
		});
	});
	
	
	$('.data_search_String').live("blur", function() {  
		var search_string = $(this).val();
		var id  = $(this).attr('id');
		//console.log(id);
		var res = id.split(",");
		var formId = res[0];
		var schoolId = res[1];
		console.log(formId+schoolId);//console.log(schoolId);
		$.ajax({
			cache:false,
            async:false,
             type : 'GET',
             //url : 	"@{Forms.searchDataList()}",
             url: '/searchData',
             data : {search_string:search_string, formId:formId, schoolId:schoolId},
             success:function(data){
            	 $('#data_search').html(data);
             }
			
		});
	});
	
	/*$('.view_data').live("click", function(){
		console.log("successful!");
		var id = $(this).attr('id');
		var res = id.split(",");
		var formId = res[0];
		var schoolId = res[1];
		console.log("Form Id:"+formId+"School Id:"+schoolId);
		$.ajax({
			cache:false,
			async:false,
			type:'GET',
			url:'/viewDataList',
			data:{formId:formId, schoolId:schoolId},
			success: function(data){
				var dataJson = JSON.parse(data);
				var dataSubArray = [];
				var dataArray = [];
				var cols = 3;
				
				jQuery.each(dataJson, function(i, val) {
					var dataTest = [];
					dataTest.push(val.DataId,val.SenderId,val.Received,val.StartTime, val.EndTime, val.Actions);
					dataArray.push(dataTest);
					
			    });
				
				console.log(dataArray);
				
				$('#example').dataTable( {
					"aaData": dataArray
			    } );
			}
		});
	});
	*/
	//var data = ${dataString.raw()};
	
	
});