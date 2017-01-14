<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	UserManager uManager = ApplicationManager.getApplicationManagerInstance().getuManager();
	User usr = uManager.getUserWithSession(session);
	
	if(usr != null) {
		%>
		// Navbar searchrequests
		if(window.location.href.indexOf("searchrequests.jsp") != -1) {
			$(".navbar-li-element").removeClass("active");
			$("#navbar-left-ul").append("<li class='navbar-li-element active' id='navbar-li-element-searchrequests'><a href='searchrequests.jsp'>Meine Gesuche</a></li>");
		} else {
			$("#navbar-left-ul").append("<li class='navbar-li-element' id='navbar-li-element-searchrequests'><a href='searchrequests.jsp'>Meine Gesuche</a></li>");
		}
		
		// Navbar credit
		var credit = parseFloat(<% out.write("" + usr.getCredit().getBalance()); %>).toFixed(2);
		if(window.location.href.indexOf("credit.jsp") != -1) {
			$(".navbar-li-element").removeClass("active");
			$("#navbar-left-ul").append("<li class='navbar-li-element active' id='navbar-li-element-credit'><a id='navbar-li-element-credit-link' href='credit.jsp'>Guthaben: " + credit + " €<span id='credit-animation-span'></span></a></li>");
		} else {
			$("#navbar-left-ul").append("<li class='navbar-li-element' id='navbar-li-element-credit'><a id='navbar-li-element-credit-link' href='credit.jsp'>Guthaben: " + credit + " €<span id='credit-animation-span'></span></a></li>");
		}
		
		// Navbar messages
		if(window.location.href.indexOf("messages.jsp") != -1) {
			$(".navbar-li-element").removeClass("active");
			$("#navbar-li-element-messages").removeClass("hidden");
		} else {
			$("#navbar-li-element-messages").removeClass("hidden");
			$("#navbar-li-element-messages").removeClass("active");
		}
		var amountUnseenMessages = "<% out.write("" + usr.getRelevantMessages().size()); %>";
		if(amountUnseenMessages > 0) {
			$("#messages-notification-number").html(amountUnseenMessages);
			$("#messages-notification-number").removeClass("hidden");
		} else {
			$("#messages-notification-number").html("");
			$("#messages-notification-number").addClass("hidden");
		}
		
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