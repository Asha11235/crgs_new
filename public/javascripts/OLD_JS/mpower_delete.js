$(document).ready(function() {
$('a.deleted').click(function(e) {
			var url=$(this).attr('data-url');
	        e.preventDefault();
	        bootbox.dialog("Are you sure?", [{
	            "label" : "No",
	            "class" : "btn-success"
	        }, {
	            "label" : "Delete",
	            "class" : "btn-danger",
	            "callback": function() {
	                $.ajax(
							{	
								url : url,
								type : "get",	
							})
							.done(function(msg,status, data){
								if(status=='success'){
									//bootbox.dialog({message: msg,title: "Confirmation",buttons: {success: {label: "OK",className: "btn-success",}} });
									
									bootbox.alert(msg, function() {
							                console.log("Alert deleted");
							                window.location.reload(); 
							        });
									
								}
								else { 
									bootbox.alert(msg, function() {
						                console.log("Not deleted deleted"); 
						        });
					             }
								 
							})
							.error(function(msg){
								bootbox.alert(msg, function() {
					                console.log("Error occured when deleting.."); 
					        });  
							});
	            }
	        }]);
	        
		});
});