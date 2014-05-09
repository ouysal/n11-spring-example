<!doctype html>
<html lang="en">

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<c:url value="/users" var="usersUrl" />

<head>
	<meta charset="utf-8">
	<title>Users</title>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
	<script src="//code.jquery.com/jquery-1.10.2.js"></script>
	<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
	<link rel="stylesheet" href="/resources/demos/style.css">
	<style>
		body { font-size: 62.5%; }
		label, input { display:block; }
		input.text { margin-bottom:12px; width:95%; padding: .4em; }
		fieldset { padding:0; border:0; margin-top:25px; }
		h1 { font-size: 1.2em; margin: .6em 0; }
		div#users-contain { width: 350px; margin: 20px 0; }
		div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
		div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
		.ui-dialog .ui-state-error { padding: .3em; }
		.validateTips { border: 1px solid transparent; padding: 0.3em; }
	</style>
	
	
	<script>	
	
	$(function() {
		var firstName = $( "#firstName" ),
			lastName = $( "#lastName" ),
			phoneNumber = $( "#phoneNumber" ),
			captchadiv = $( "#captchadiv" ),
			allFields = $( [] ).add( firstName ).add( lastName ).add( phoneNumber ).add( captchadiv ),
			tips = $( ".validateTips" );

		function updateTips( t ) {
			tips
				.text( t )
				.addClass( "ui-state-highlight" );
			setTimeout(function() {
				tips.removeClass( "ui-state-highlight", 1500 );
			}, 500 );
		}

		function checkLength( o, n, min, max ) {
			if ( o.val().length > max || o.val().length < min ) {
				o.addClass( "ui-state-error" );
				updateTips( "Length of " + n + " must be between " +
					min + " and " + max + "." );
				return false;
			} else {
				return true;
			}
		}

		function checkRegexp( o, regexp, n ) {
			if ( !( regexp.test( o.val() ) ) ) {
				o.addClass( "ui-state-error" );
				updateTips( n );
				return false;
			} else {
				return true;
			}
		}

		$( "#dialog-form" ).dialog({
			autoOpen: false,
			height: 320,
			width: 360,
			modal: true,
			buttons: {
				"Save": function() {
					var bValid = true;
					allFields.removeClass( "ui-state-error" );
					
					bValid = bValid && checkLength( firstName, "first name", 3, 16 );
					bValid = bValid && checkRegexp( firstName, /^[a-zA-Z]+$/ , "First name may consist of a-z" );
					
					bValid = bValid && checkLength( lastName, "last name", 3, 16 );
					bValid = bValid && checkRegexp( lastName, /^[a-zA-Z]+$/ , "Last name may consist of a-z" );
					
					if ( bValid ) {
						
						if ($(this).data('operation') == "add") {
							addUser();
						} else {
							updateUser();
						}
					}
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			open: function() {
		        
		        if ($(this).data('operation') == "add") {
		        	
		        	$(this).dialog("option", "title", "Create new user");
		        	$(this).dialog("option", "height", 420);
		        	$(this).dialog("option", "width", 494);
		        	
		        	Recaptcha.create("6LftOvMSAAAAADF7i9s0o7x4piE7nqHbHZA_m34_", 'captchadiv', {
		                tabindex: 1,
		                theme: "clean",
		                callback: Recaptcha.focus_response_field
		            });	
		        } else {
		        	
		        	$(this).dialog("option", "title", "Edit user");
		        	$(this).dialog("option", "height", 320);
		        	$(this).dialog("option", "width", 360);
		        	
		        	$("#captchadiv").empty();
		        }
		    },
			close: function() {
				allFields.val( "" ).removeClass( "ui-state-error" );
			}
		});

		$( "#create-user" )
			.button()
			.click(function() {
				$( "#dialog-form" ).dialog( "open" );
			});
		
		$( "#phoneNumber" ).mask('000 000 00 00');
	});
	
	$(function() {
		
		// init
		urlHolder.users = '${usersUrl}';
		findAll();
		
		$('#newBtn').click(function() {
			$( "#dialog-form").data('operation', "add"); 
			$( "#dialog-form").dialog( "open");
		});
		
		$('#editBtn').click(function() { 
			if (hasSelected()) {
				fillEditForm();
				$( "#dialog-form").data('operation', "edit"); 
				$( "#dialog-form").dialog( "open");
			}
		});
		
		$('#deleteBtn').click(function() {
			if (hasSelected()) { 
				if (confirm('Are you sure you want to delete this user?')) {
					deleteUser();	
				}
			}
		});
		
	});

	</script>
	
	<script type='text/javascript' src='<c:url value="/resources/js/jquery.mask.min.js"/>'></script>
	<script type="text/javascript" src="http://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
	<script type='text/javascript' src='<c:url value="/resources/js/user.js"/>'></script>	

</head>

<body>

	<div id="dialog-form" title="Create new user">
		<p class="validateTips" id="errorMessage" >All form fields except phone number are required.</p>
		
		<form>
			<fieldset>
				<label for="firstName">First Name</label>
				<input type="text" name="firstName" id="firstName" class="text ui-widget-content ui-corner-all">
				<label for="lastName">Last Name</label>
				<input type="text" name="lastName" id="lastName" class="text ui-widget-content ui-corner-all">
				<label for="phoneNumber">Phone Number</label>
				<input type="text" name="phoneNumber" id="phoneNumber" class="text ui-widget-content ui-corner-all">
			</fieldset>
		</form>
		
		<div id="captchadiv"></div>
	</div>
	
	<div id="header">
		<table class="ui-widget" style="width:80%">
			<tr>
				<td><h1>Existing Users:</h1></td>
				<td><div id="loading"></div></td>
			</tr>
		</table>
	</div>
	
	<div id="users-contain" class="ui-widget">		
		<table id="tableUsers" class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
				<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
				<th>First Name</th>
				<th>Last Name</th>
				<th>Phone Number</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<div id='controlBar'>
		<table>
			<tr>
				<td><input type='button' value='New' id='newBtn' /></td>
				<td><input type='button' value='Edit' id='editBtn' /></td>
				<td><input type='button' value='Delete' id='deleteBtn' /></td>
			</tr>
		</table>
	</div>
	
</body>
</html>