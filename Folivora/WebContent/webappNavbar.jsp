<%@page import="de.folivora.model.*"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

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
			<a href="index.jsp"><img src="res/img/brand.png" class="img-responsive webapp-brand"></a>
		</div>
		
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav" id="navbar-left-ul">
				<li class="active">
					<a href="webapp.jsp">WebApp <span class="sr-only">(current)</span></a>
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
				<li class='navbar-li-element hidden' id='navbar-li-element-messages'>
					<a href='messages.jsp'>Nachrichten</a>
	          		
					<div class="button">
						<span class="notification-number" id="messages-notification-number"></span>
					</div>
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

<script>
	$(document).ready(function() {
		<%
			User usr = (User)request.getAttribute("user");
			
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
				var amountUnseenMessages = "<% out.write("" + ApplicationManager.getApplicationManagerInstance().countUnreadMsgsInList(usr, usr.getRelevantMessages())); %>";
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
	});
</script>