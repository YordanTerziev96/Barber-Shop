/*function fetchHours(value){
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
	             'Authorization': 'Basic '+btoa('dancho:0000')}),
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
		        for (var i = 0; i < data.length; i++)
					options += '<option value="'+data[i]+'">'+data[i]+'</option>';
		        	document.getElementById('times').innerHTML = options;
		        });
		   	}
		)
		.catch(function(err) {
			console.log('Fetch Error :-S', err);
		});
}


$(document).ready(function() {
	  fetch('http://localhost:8080/appointment/appointmentsForThisWeek', {
			method: "GET",
		    credentials: "include",
		    headers: new Headers({'Content-Type': 'application/json',
		                 'Authorization': 'Basic '+ 'dancho:0000'}),
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
		            	$('#calendar').fullCalendar({
		            		defaultDate: '2018-11-05',
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
});*/