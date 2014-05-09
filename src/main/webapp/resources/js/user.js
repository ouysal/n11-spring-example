/**
 * Contains custom JavaScript code
 */
var urlHolder = new Object();

function findAll() {
	
	console.log('findAll');
	
    $.ajax({
        type: 'GET',
        url: urlHolder.users,
        beforeSend: function() {
			$('#loading').html('<img src="resources/img/loading.gif"> Loading...');
		},
		complete: function(html) {
			$('#loading').html(html);
		},
        success: function(data, textStatus, jqXHR){
        	$('#tableUsers').find('tbody').remove();
     		
     		for (var i=0; i<data.length; i++) {
    			var row = '<tr>';
    			row += '<td><input type="radio" name="index" id="index" value="'+i+'"></td>';
    			row += '<td>' + data[i].firstName + '</td>';
    			row += '<td>' + data[i].lastName + '</td>';
    			row += '<td>' + data[i].phoneNumber + '</td>';
    			row += '</tr>';
    	 		$('#tableUsers').append(row);
     		}
     		
     		$('#tableUsers').data('model', data);
        },
        error: function(jqXHR, textStatus, errorThrown){
            alert('findAll error: ' + textStatus);
        }
    });		
}

function addUser() {
	
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: urlHolder.users, 
        data: "{\"user\":" + formToJSON() + ",\"captcha\":" + captchaToJson() + "}",
        success: function(data, textStatus, jqXHR){
        	findAll();
        	$("#dialog-form").dialog( "close" );
        },
        error: function(jqXHR, textStatus, errorThrown){
        	
        	if (jqXHR.responseText === "Captcha is incorrect.") {
        		
        		$("#errorMessage").text(jqXHR.responseText);
        		$("#errorMessage").addClass( "ui-state-highlight");
        		setTimeout(function() {
        			$("#errorMessage").removeClass( "ui-state-highlight", 1500 );
        		}, 500 );
        		
        		Recaptcha.create("6LftOvMSAAAAADF7i9s0o7x4piE7nqHbHZA_m34_", 'captchadiv', {
        	        tabindex: 1,
        	        theme: "clean",
        	        callback: Recaptcha.focus_response_field
        	    });
        	} else {
        		alert('addUser error: ' + textStatus);
        	}
        }
    });	
}

function updateUser() {
	
	console.log('updateUser');
	
	var selected = $('input:radio[name=index]:checked').val();
	var userId = $('#tableUsers').data('model')[selected].id;
	
    $.ajax({
    	type: 'PUT',
        contentType: 'application/json',
        url: urlHolder.users + "/" + userId,
        dataType: "json",
        data: formToJSON(),
        success: function(data, textStatus, jqXHR){
        	findAll();
        	$("#dialog-form").dialog( "close" );
        },
        error: function(jqXHR, textStatus, errorThrown){
            alert('updateUser error: ' + textStatus);
        }
    });	
}

function deleteUser() {
	
	console.log('deleteUser');
	
	var selected = $('input:radio[name=index]:checked').val();
	var userId = $('#tableUsers').data('model')[selected].id;
	
    $.ajax({
        type: 'DELETE',
        url: urlHolder.users + "/" + userId,
        success: function(data, textStatus, jqXHR){
        	findAll();
        },
        error: function(jqXHR, textStatus, errorThrown){
            alert('deleteUser error: ' + jqXHR.statusText);
        }
    });	
}

function formToJSON() {
	
	var userId = null;
	
	if ($("#dialog-form").data('operation') == "edit") {
		var selected = $('input:radio[name=index]:checked').val();
		userId = $('#tableUsers').data('model')[selected].id;
	}
	
	return JSON.stringify({
	    	"id": userId,
	        "firstName": $('#firstName').val(),
	        "lastName": $('#lastName').val(),
	        "phoneNumber": $('#phoneNumber').val()
			});
}

function captchaToJson() {
	return JSON.stringify({
    		"challenge": Recaptcha.get_challenge(),
    		"response": Recaptcha.get_response()
		    });
}

function fillEditForm() {
	var selected = $('input:radio[name=index]:checked').val();
	$('#firstName').val( $('#tableUsers').data('model')[selected].firstName );
	$('#lastName').val( $('#tableUsers').data('model')[selected].lastName );
	$('#phoneNumber').val( $('#tableUsers').data('model')[selected].phoneNumber );
}

function hasSelected() {
	var selected = $('input:radio[name=index]:checked').val();
	if (selected == undefined) { 
		alert('Select a user first!');
		return false;
	}
	
	return true;
}