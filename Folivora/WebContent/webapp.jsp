<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.User"%>
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
    <link href="res/css/folivora.css" rel="stylesheet" type="text/css">
    
    <script type="text/javascript" src="res/js/moment.min.js"></script>
    <link href="res/css/daterangepicker.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="res/js/daterangepicker.js"></script>
    
    <script type="text/javascript" src="res/js/notify.min.js"></script>
    
    <script type="text/javascript" src="res/js/folivora-utility.js"></script>
    <script type="text/javascript" src="res/js/folivora-signin.js"></script>
    <script type="text/javascript" src="res/js/folivora-webapp.js"></script>
    
    <script>
    	var ownUserId;
    	$(document).ready(function() {
    		<%@ include file="customizeNavbar.jsp" %>
    		
    		<%
    			// Do stuff when someone is signed in
    			UserManager userManager = ApplicationManager.getApplicationManagerInstance().getuManager();
    			User myUser = userManager.getUserWithSession(session);
    			if(myUser != null) {
    				%>
    				ownUserId =  <% out.write("" + myUser.getId()); %>;
    				
    				$("#sr-toggle-btn").removeClass("hidden");
    				$("#sr-function-btns").removeClass("hidden");
    				<%
    			}
    		%>
    	});
    </script>
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
						<input name="username" class="form-control" id="loginform-username" placeholder="Nutzername oder email eingeben" >
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
          <ul class="nav navbar-nav" id="navbar-left-ul">
            <li class="active">
              <a href="#">WebApp <span class="sr-only">(current)</span></a>
            </li>
            <li role="separator" class="divider"></li>
            <!-- <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li>
                  <a href="#">Action</a>
                </li>
                <li>
                  <a href="#">Another action</a>
                </li>
                <li>
                  <a href="#">Something else here</a>
                </li>
                <li role="separator" class="divider"></li>
                <li>
                  <a href="#">Separated link</a>
                </li>
                <li role="separator" class="divider"></li>
                <li>
                  <a href="#">One more separated link</a>
                </li>
              </ul>
            </li>-->
          </ul>
          <!--<form class="navbar-form navbar-left">
            <div class="form-group">
              <input type="text" class="form-control" placeholder="Search">
            </div>
            <button type="submit" class="btn btn-default">Submit</button>
          </form>-->
          <ul class="nav navbar-nav navbar-right" id="navbar-right-ul">
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
    		<div class="col-md-10 left-side full-height" id="webapp-map-container">
    			<div id="webapp-map" class="full-height"></div>
    		</div>
    		
    		<div class="col-md-2 right-side" id="webapp-map-details-container">
    			<input type="button" class="btn btn-default hidden" id="sr-toggle-btn" value="Neues Gesuch erstellen">
    			
    			<br><br>
    			
    			<form role="form" id="srform"  method="post" class="hidden">
					<div class="form-group">
						<label for="title">Titel:</label>
						<input name="title" class="form-control" id="srform-title" placeholder="Ttel eingeben" required>
					</div>
					<div class="form-group">
						<label for="description">Beschreibung:</label>
						<input name="description" class="form-control" id="srform-description" placeholder="Beschreibung eingeben" required>
					</div>
					<label for="address" style="width: 100%;">Lieferadresse:</label>
					<div class="input-group">
					   <input type="text" name="address" id="srform-address" class="form-control" placeholder="Adresse wählen" disabled required>
					   <span class="input-group-btn">
					        <button class="btn btn-default" id="btn-select-address" type="button">+</button>
					   </span>
					</div>
					
					<!--<div class="form-group">
						<label for="daterange-possible">Möglicher Lieferzeitraum:</label>
						<input name="daterange-possible" id="srform-delivery-possible" required>
					</div>-->
					
					<br>
					<div class="dropdown">
						<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"
							id="btn-possible-delivery-dropdown" style="width: 100%;">Mögliche Lieferzeit<span class="caret"></span></button>
					  	<ul class="dropdown-menu">
					  	<li><a href="#" onclick="updatePossibleDelivery(1)">1h</a></li>
					    	<li><a href="#" onclick="updatePossibleDelivery(2)">2h</a></li>
					    	<li><a href="#" onclick="updatePossibleDelivery(3)">3h</a></li>
					    	<li><a href="#" onclick="updatePossibleDelivery(4)">4h</a></li>
					    	<li><a href="#" onclick="updatePossibleDelivery(8)">8h</a></li>
					    	<li><a href="#" onclick="updatePossibleDelivery(12)">12h</a></li>
					    	<li><a href="#" onclick="updatePossibleDelivery(24)">24h</a></li>
					  	</ul>
					</div>
					<br>
					
					<div class="form-group">
						<label for="maxcosts">Kosten:</label>
						<input min="0.10" max="<% if(myUser!= null) out.write("" + myUser.getCredit().getMaxPossiblePriceForSr()); %>" step="0.1" type="number" name="maxcosts" class="form-control" id="srform-maxcosts" placeholder="Max. Kosten" required>
					</div>
					
					<div class="form-group">
						<label for="charges">Gebühren:</label>
						<input type="number" name="charges" class="form-control" id="srform-charges" placeholder="Gebühren" disabled>
					</div>
					
					<div class="form-group">
						<label for="final-costs">Gesamtkosten:</label>
						<input type="number" name="final-costs" class="form-control" id="srform-final-costs" placeholder="Gesamtkosten" disabled>
					</div>
					
					<input type="submit" value="Submit" class="btn btn-default" id="btn-srform-submit">
				</form>
				
				<div class="container" id="sr-filter-container">
					<div class="row">
						<div class="col-md-12">
							<h4>Filtereinstellungen:</h4>
							<p>Bald ...</p>
						</div>
					</div>
				</div>
    		</div>
    	</div>
    </div>
    
    
  <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB-BsbTc5WFULPD9m4QAXOdolIq0za7QOo&callback=loadMapData"></script>
</body></html>