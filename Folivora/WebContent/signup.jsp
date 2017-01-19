<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head>
  <%@ include file="webappHead.jsp" %>
  
  <body>
  	<div id="login" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header form-group custom-background-orange-dark">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Login</h4>
			</div>
				
			<div class="modal-body">
  				<form role="form" id="loginForm"  method="post">
  					<div>
						<label for="loginform-username">Nutzername:</label>
						<input name="username" class="form-control" id="loginform-username" placeholder="Nutzername oder Email eingeben" >
					</div>
 					
					<div>
						<label for="loginform-password">Passwort:</label>
						<input name="password" type="password" class="form-control" id="loginform-password" placeholder="Passwort eingeben">
					</div>
  				</form>
			</div>
				
			<div class="modal-footer">
				<a href="signup.jsp" style="float:left;">Registrieren</a>
				<button type="submit" id="loginButton" class="btn btn-default">Login</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
	</div>
  
  <%@ include file="webappNavbar.jsp" %>
  
  <div class="container">
  	<div class="row">
  		<div class="col-md-12">
			<h2>Registration</h2>
				<form role="form" id="signupForm"  method="post">
					<div class="form-group">
						<label for="forename">Nutzername:</label>
						<input name="forename" class="form-control" id="signupform-username" placeholder="Nutzername eingeben" required>
					</div>
					<div class="form-group">
						<label for="email">Email:</label>
						<input name="email" type="email" class="form-control" id="signupform-email" placeholder="Email eingeben" required>
					</div>
					<div class="form-group">
						<label for="">Geschlecht:</label>
						<br>
						<label class="radio-inline"><input value="male" type="radio" id="signupform-gender-male" name="gender-male" checked>Männlich</label>
						<label class="radio-inline"><input value="female" type="radio" id="signupform-gender-female" name="gender-female">Weiblich</label>
					</div>
					<div class="form-group">
						<label for="birthday">Geburtsdatum:</label>
						<input name="birthday" type="date" class="form-control" id="signupform-birthday" placeholder="" >
					</div>
					<div class="form-group">
						<label for="password">Password:</label>
						<input name="password" type="password" class="form-control" id="signupform-password" placeholder="Enter password" required>
					</div>
					<div class="form-group">
						<label for="confirm_password"> Confirm Password:</label>
						<input name="confirm_password" type="password" class="form-control" id="signupform-confirm-password" placeholder="Passwort wiederholen" required>
					</div>
					<div class="form-group">
						<label for="hometown">Wohnort:</label>
						<input name="hometown" class="form-control" id="signupform-hometown" placeholder="Wohnort eingeben">
					</div>
					
					<button type="submit" id="submitButton" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>
  </div>

</body></html>