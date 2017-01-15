<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.User"%>
<%@page import="de.folivora.model.UserCredit"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
  
    <%@ include file="webappNavbar.jsp" %>
    
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
						<input min="0.10" max="<% if(request.getAttribute("user") != null) out.write("" + ((User)request.getAttribute("user")).getCredit().getMaxPossiblePriceForSr()); %>" step="0.1" type="number" name="maxcosts" class="form-control" id="srform-maxcosts" placeholder="Max. Kosten" required>
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