<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.User"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<%@ include file="webappHead.jsp" %>
<%
String userId = request.getParameter("id") != null ? request.getParameter("id") : ((User) request.getAttribute("user")).getId().toString();
User user = ApplicationManager.getApplicationManagerInstance().getuManager().getUserWithId(userId);
boolean isMe = ((User) request.getAttribute("user")).getId().toString().equals(userId);

List<Feedback> feedbacks = new ArrayList<>();
String registered = "";
String age = ""; 
String gender = "";
String hometown = "";
if (user != null) {
	registered = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale.GERMANY).format(user.getCreationTimestamp());
	age = "keine Angabe";
	if (user.getBirthday() != null) {
		long timeBetween = System.currentTimeMillis() - user.getBirthday().getTime();
		double yearsBetween = timeBetween / 3.156e+10;
		age = "" + (int) Math.floor(yearsBetween);
	}
	feedbacks = user.getReceivedFeedbacks();
	gender = user.getGender() == null ? "keine Angabe" : user.getGender().toString();
	hometown = user.getHometown() == null ? "keine Angabe" : user.getHometown();
}
%>
  
<body>
	<%@ include file="webappNavbar.jsp" %>
	<!-- <script type="text/javascript" src="res/js/folivora-user.js"></script> -->
    
    <div class="container">
    	<div class="row">
    		<div class="col-md-12 left-side full-height" id="message-list-container">
    			<%
    			if (user == null) {
					out.write("<h2>Benutzer nicht gefunden.</h2>");
				}
    			else { %>
	    			<% if (isMe) { %>
	    				<h2>
	    					<span>Dein Profil</span>
	    					<a href="userSettings.jsp" class="btn btn-primary">Profil bearbeiten</a>
	    				</h2>
	    			<% } else { %>
	    				<h2>Profil von <span id="user-name"><% out.write(user.getName()); %></span></h2>
	    			<% } %>
	    			<p><strong>Registriert seit:</strong> <span id="user-registered"><% out.write(registered); %></span></p>
	    			<p><strong>Alter:</strong> <span id="user-age"><% out.write(age); %></span></p>
	    			<p><strong>Geschlecht:</strong> <span id="user-gender"><% out.write(gender); %></span></p>
	    			<p><strong>Wohnort:</strong> <span id="user-hometown"><% out.write(hometown); %></span></p>
	    			<!-- <p><strong>Guthaben:</strong> <span id="user-credit"></span></p> -->
	    			<h3>Bewertungen</h3>
	    			<%
	    			for (Feedback feedback : feedbacks) {
	    				%>
	    				<p>
	    					<% for (int i = 0; i != 5; i++) { %>
	    						<i class="glyphicon glyphicon-star<% out.write(feedback.getRating().getVal() < (i + 1) ? "-empty" : ""); %>"></i>
	    					<% } %>
	    					<em><% out.write(feedback.getDescription()); %></em><br />
	    					- <a href="user.jsp?id=<% out.write(feedback.getFeedbackCreator().getId().toHexString());%>"><% out.write(feedback.getFeedbackCreator().getName()); %></a>
	    					am <% out.write(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, Locale.GERMANY).format(feedback.getCreationTimestamp())); %>
	    				</p>
	    				<%
	    			}
    			}
	    		%>
    		</div>
    	</div>
    </div>
</body>
</html>