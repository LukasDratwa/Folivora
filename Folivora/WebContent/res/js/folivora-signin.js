$(document).ready(function() {
	initSignIn();
});

function initSignIn() {
	$("#loginForm").validate({
		rules: {
			username: {
				required: true
			},
			
			password: {
				required: true
			}
		},
		messages: {
			username: {
				required: "Please enter your username"
			},
			password: {
				required: "Please enter your password"
			}
		}
	});
	
	$("#loginForm").keyup(function(event) {
		if(event.keyCode == 13){
			$("#loginButton").click();
		}
	});
	
	$("#loginButton").click(function() {
		$("#loginButton").prop('disabled', true);
		var objPost = {
				name: $("#loginform-username").val(),
		  		password: $("#loginform-password").val(),
		  		email: $("#loginform-username").val()
		}
		
		createRest("POST", "signinservlet", JSON.stringify(objPost));
		
		console.log("Login of: " , objPost);
		
		setTimeout(function() {
			if(lastRestStatus != null && lastRestStatus == 200) {
				$.notify("Login erfolgreich.", "success");
				
				$.removeCookie("token");
				$.cookie("token", lastResponse.token);
				
				setTimeout(function() {
					window.location = "webapp.jsp";
				}, 850);
			} else {
				
				$.notify("Username und/oder Passwort falsch!", "error");
			}
			
			$('button').prop('disabled', false);
			lastRestStatus = null;
		}, 500);
	});
}