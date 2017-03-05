
    
function myfun(x,y,z){
		document.getElementById(x).value =	parseInt(document.getElementById(y).value) 
										+ parseInt(document.getElementById(z).value);									
	}
	
function test(){
$("#school.schoolType").on('change',function(){
	    var vall = ("#school.schoolType").val();
		console.log(vall);
		if( document.getElementById('school.schoolType').value == "Girls school"){
		
		   document.getElementById('school.maleStruden').disabled = true;
		   document.getElementById('school.maleToilets').disabled = true;
		}
		
		else if ( document.getElementById('school.schoolType').value == "Boys School"){
		 
		 document.getElementById('school.femailStudent').disabled = true;
		 document.getElementById('school.femailToilets').disabled = true;
		}
		
		else{
		
		document.getElementById('school.maleStruden').disabled = false;
		document.getElementById('school.maleToilets').disabled = false;
		document.getElementById('school.femailStudent').disabled = false;
		document.getElementById('school.femailToilets').disabled = false;
	
		}
	
	});	
}

$(document).ready(function (){
	console.log("heolow");
	$("#school.schoolType").on('change',function(){
	console.log("heolow");
	    var val = ("#school.schoolType").val();
		alert(val);
		if( document.getElementById('school.schoolType').value == "Girls school"){
		
		   document.getElementById('school.maleStruden').disabled = true;
		   document.getElementById('school.maleToilets').disabled = true;
		}
		
		else if ( document.getElementById('school.schoolType').value == "Boys School"){
		 
		 document.getElementById('school.femailStudent').disabled = true;
		 document.getElementById('school.femailToilets').disabled = true;
		}
		
		else{
		
		document.getElementById('school.maleStruden').disabled = false;
		document.getElementById('school.maleToilets').disabled = false;
		document.getElementById('school.femailStudent').disabled = false;
		document.getElementById('school.femailToilets').disabled = false;
	
		}
	
	});	
});
