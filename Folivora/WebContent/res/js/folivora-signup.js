/**
 * @author Lukas Dratwa
 */
$(document).ready(function() {
	$("#signupForm").validate({
		rules: {
			username: {
				required: true,
				minlength: 3
			},
			password: {
				required: true,
				minlength: 3
			}, 
			confirm_password: {
				required: true,
				minlength: 3,
				equalTo: "#signupform-password"
			}
		}, 
		messages: {
			username: {
				required: "Please choose your username",
				minlength: "Your username must consist of at least 3 characters"
			},
			password: {
				required: "Please provide a password",
				minlength: "Your password must be at least 3 characters long"
			},
			confirm_password: {
				required: "Please provide a password",
				minlength: "Your password must be at least 3 characters long",
				equalTo: "Please enter the same password as above"
			}
		},
		submitHandler: function (event, currentIndex) {
			var signUpObj = {
					name: $("#signupform-username").val(),
					password: $("#signupform-password").val(),
					email: $("#signupform-email").val(),
					male: $("#signupform-gender-male").is(":checked"),
					female: $("#signupform-gender-female").is(":checked"),
					birthday: $.dateFromString($("#signupform-birthday").val()).valueOf(),
					hometown: $("#signupform-hometown").val()
			};
			
			// Only allow user +16 years
			if(calculateAge(new Date($("#signupform-birthday").val())) < 16) {
				$.notify("Leider darf diese Plattform erst ab 16 Jahren benutzt werden!", "error");
				return;
			}
			
			if(signUpObj.male && !signUpObj.female) {
				signUpObj.gender = "MALE";
			} else if(!signUpObj.male && signUpObj.female) {
				signUpObj.gender = "FEMALE";
			}
			
			console.log(signUpObj);
			
			createRest("POST", "signupservlet", JSON.stringify(signUpObj));
			
			setTimeout(function() {
				if(lastRestStatus != null && lastRestStatus == 201 || lastRestStatus == 200) {
					$.notify("Nutzer \"" + signUpObj.name + "\" erfolgreich erstellt.", "success");
					
					$.removeCookie("token");
					$.cookie("token", lastResponse.token);
					
					setTimeout(function() {
						window.location = "webapp.jsp";
					}, 850);
				} else {
					$.notify("Fehler beim Erstellen des Nutzers!", "error");
				}
				
				lastRestStatus = null;
			}, 1000);
		}
	});
})