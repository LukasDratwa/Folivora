<%@page import="java.text.SimpleDateFormat"%>
<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.User"%>
<%@page import="de.folivora.model.UserCredit"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="webappHead.jsp" %>

<%
User signedInUser = (User)request.getAttribute("user");
%>

<body>
    <%@ include file="webappNavbar.jsp" %>
    <div class="container">
    	<div class="row">
    		<div class="col-md-9">
    			<h3>Benutzerkonto verwalten</h3>
    			<form role="form"  method="post">
   					<div class="form-group">
						<label>Benutzername:</label>
						<input class="form-control" value="<% out.write(signedInUser.getName()); %>" disabled="disabled" />
					</div>
   					<div class="form-group">
						<label for="hometown">Geburtstag:</label>
						<input type="text" name="birthday" class="form-control" placeholder="Geburtstag eingeben" <% out.write(signedInUser.getBirthday() == null ? "" : SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(signedInUser.getBirthday())); %> />
					</div>
   					<div class="form-group">
   						<label>Geschlecht:</label>
   						<div class="input-group">
							<div class="input-group-btn">
								<button type="button" class="btn btn-default btn-block dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									<% out.write("" + signedInUser.getGender()); %> <span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li><a href="#">Keine Angabe</a></li>
									<li><a href="#">Männlich</a></li>
									<li><a href="#">Weiblich</a></li>
								</ul>
							</div>
						</div>
					</div>
   					<div class="form-group">
						<label for="hometown">Wohnort:</label>
						<input type="text" name="hometown" class="form-control" placeholder="Wohnort eingeben" value="<% out.write(signedInUser.getHometown()); %>" />
					</div>
   					<div class="form-group">
						<label for="hometown">Passwort:</label>
						<input type="password" name="pass" class="form-control" placeholder="******" required />
					</div>
   					<div class="form-group">
						<label for="hometown">Neues Passwort (optional):</label>
						<input type="password" name="new-pass" class="form-control" />
					</div>
   					<div class="form-group">
						<label for="hometown">Neues Passwort bestätigen:</label>
						<input type="password" name="new-pass-confirm" class="form-control" />
					</div>
					<button type="submit" class="btn btn-success">Speichern</button>
    			</form>
    			<h4>Guthaben aufladen</h4>
				<div class="form-group">
					<label for="title">Guthaben:</label>
					<div class="input-group">
						<input type="text" class="form-control" value="<% out.write("" + signedInUser.getCredit().getBalance()); %>" />
						<span class="input-group-addon">&euro;</span>
						<span class="input-group-btn">
							<button class="btn btn-default btn-primary" type="button">Aufladen…</button>
						</span>
					</div>
				</div>
    		</div>
    	</div>
    </div>
</body>
</html>