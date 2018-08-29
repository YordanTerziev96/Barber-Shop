function popup() {
    var popup = document.getElementById("myPopup");
    popup.classList.toggle("show");
}

function fetchHours(value){
		var mydate = new Date(value);
		var month = mydate.getMonth() + 1;
		if(month<10){
		var newDate = mydate.getDate() + "-0" + month + "-" + mydate.getFullYear();
		}else{
			var newDate = mydate.getDate() + "-" + month + "-" + mydate.getFullYear();
		}
		fetch('http://localhost:8080/appointment/freeAppointments?date='+newDate, {
			method: "GET",
			credentials: "include",
			headers: new Headers({'Content-Type': 'application/json',
	             'Authorization': 'Basic '+btoa('petars:0000')}),
		  	body: console.log("Connecting to back-end")
		}).then(
			function(response) {
		    	if (response.status !== 200) {
		        	console.log('Looks like there was a problem. Status Code: ' +
		            response.status);
		          	return;
			}

		   	// Examine the text in the response
		    response.json().then(function(data) {
		    	var options = '';
		        for (var i = 0; i < data.length; i++){
					options += '<option value="'+data[i]+'">'+data[i]+'</option>';
		        }
		        	document.getElementById('times').innerHTML = options;
		        });
		   	}
		)
		.catch(function(err) {
			console.log('Fetch Error :-S', err);
		});
}

$(document).ready(function() {
	if(window.location.pathname === "/create"){ 
	  fetch('http://localhost:8080/appointment/appointmentsForThisWeek', {
			method: "GET",
		    credentials: "include",
		    headers: new Headers({'Content-Type': 'application/json',
		                 'Authorization': 'Basic '+ 'petars:0000'}),
		    body: console.log("Connecting to back-end")
		}).then(
				function(response) {
		        	if (response.status !== 200) {
		            	console.log('Looks like there was a problem. Status Code: ' +
		                response.status);
		                return;
		            }
		            // Examine the text in the response
		            response.json().then(function(data) {
						var chararr = data[0].start.split('');
						var startarr = [];
						for(var i = 0; i<10; i++){
							startarr.push(chararr[i]);
                        }
						var startdate = startarr.join("");
		            	$('#calendar').fullCalendar({
		      				defaultDate: startdate,
		      				navLinks: true, // can click day/week names to
											// navigate views
		      				defaultView: 'agendaWeek',
		      				weekNumbers: true,
		      				weekNumbersWithinDays: true,
		      				weekNumberCalculation: 'ISO',
		      				minTime: "08:00:00",
					    	maxTime: "19:00:00",
		      				editable: true,
		      				eventLimit: true, // allow "more" link when too
												// many events
		      				events: data
		    			});
		            });
		       }) 
		       
		       
		       
	    	   if($(".message").text() === "Invalid date!" || $(".message").text() === "You cannot make an appointment in such a close range!"){
	    		   $(".message").css('color', 'red');
	    	   }else{
	    		   $(".message").css('color', 'green');
	    	   }
	}
	});