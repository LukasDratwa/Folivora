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
    		<div class="col-md-9 col-xs-12 left-side full-height" id="webapp-map-container">
    			<div id="webapp-map" class="full-height"></div>
    		</div>
    		
    		<div class="col-md-3 col-xs-12 right-side" id="webapp-map-details-container">
    			<%@ include file="webappAddSr.jsp" %>
    			<%@ include file="webappFilter.jsp" %>
    		</div>
    	</div>
    </div>
    
    
  <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB-BsbTc5WFULPD9m4QAXOdolIq0za7QOo&callback=loadMapData"></script>
</body></html>