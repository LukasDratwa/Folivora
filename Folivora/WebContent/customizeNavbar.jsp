<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<%
	UserManager uManager = ApplicationManager.getApplicationManagerInstance().getuManager();
	User usr = uManager.getUserWithSession(session);
	
	if(usr != null) {
		%>
		var username = "<% out.write(usr.getName()); %>";
			
		// Navbar userSettings
		if(window.location.href.indexOf("userSettings.jsp") != -1) {
			$(".navbar-li-element").removeClass("active");
			$("#navbar-right-ul").append("<li class='navbar-li-element active' id='navbar-li-element-username'><a href='userSettings.jsp'>" + username + "</a></li>");
		} else {
			$("#navbar-right-ul").append("<li class='navbar-li-element' id='navbar-li-element-username'><a href='userSettings.jsp'>" + username + "</a></li>");
		}
		
		$("#navbar-right-ul").append("<li class='navbar-li-element' id='navbar-li-element-logout'><a href='webapp.jsp' onclick='signOut()'>Logout</a></li>");
		
		$("#btn-show-loginform").css("display", "none");
		<%
	}
%>