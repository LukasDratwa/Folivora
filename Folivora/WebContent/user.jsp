<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ include file="webappHead.jsp" %>
  
<body>
	<%@ include file="webappNavbar.jsp" %>
	<script type="text/javascript" src="res/js/folivora-user.js"></script>
    
    <div class="container">
    	<div class="row">
    		<div class="col-md-12 left-side full-height" id="message-list-container">
    			<h2>Profil von <span id="user-name"></span></h2>
    			<p><strong>Registriert seit:</strong> <span id="user-registered"></span></p>
    			<p><strong>Alter:</strong> <span id="user-age"></span></p>
    			<p><strong>Geschlecht:</strong> <span id="user-gender"></span></p>
    			<p><strong>Wohnort:</strong> <span id="user-hometown"></span></p>
    			<p><strong>Guthaben:</strong> <span id="user-credit"></span></p>
    			<h3>Bewertungen</h3>
    		</div>
    	</div>
    </div>
</body>
</html>