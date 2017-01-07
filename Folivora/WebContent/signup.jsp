<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head>
    <meta charset="utf-8">
    <title>Folivora - Webapp</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="res/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="res/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="res/js/jquery.cookie.plugin.js"></script>
    <script type="text/javascript" src="res/js/bootstrap.min-3.3.7.js"></script>
    <link href="res/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="res/css/bootstrap-pigendo-default-theme.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="res/js/folivora-utility.js"></script>
    <script type="text/javascript" src="res/js/folivora-signup.js"></script>
    <script type="text/javascript" src="res/js/folivora-signin.js"></script>
    <script type="text/javascript" src="res/js/notify.min.js"></script>
    <link href="res/css/folivora.css" rel="stylesheet" type="text/css">
  </head>
  
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
  
    <nav class="navbar navbar-default">
      <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a href="index.jsp"><img src="res\img\brand.png" class="img-responsive webapp-brand"></a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav">
            <li class="active">
              <a href="#">Webapp <span class="sr-only">(current)</span></a>
            </li>
            <li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li>
              <a href="index.jsp">About</a>
            </li>
            <li>
          		<a href="" id="btn-show-loginform" class="" data-toggle="modal" data-target="#login">Login</a>
            </li>
          </ul>
        </div>
        <!-- /.navbar-collapse -->
      </div>
      <!-- /.container-fluid -->
    </nav>
  
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