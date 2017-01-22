<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.User"%>
<%@page import="de.folivora.model.UserCredit"%>

<head>
    <meta charset="utf-8">
    <title>Folivora - Webapp</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="res/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="res/js/jquery-ui-1.12.1.min.js"></script>
    <script type="text/javascript" src="res/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="res/js/jquery.cookie.plugin.js"></script>
    <script type="text/javascript" src="res/js/bootstrap.min-3.3.7.js"></script>
    <link href="res/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="res/css/bootstrap-pigendo-default-theme.css" rel="stylesheet" type="text/css">
    <link href="res/css/jquery-ui-1.12.1.min.css" rel="stylesheet" type="text/css">
    
    <script type="text/javascript" src="res/js/moment.min.js"></script>
    <link href="res/css/daterangepicker.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="res/js/daterangepicker.js"></script>
    
    <script type="text/javascript" src="res/js/rating.js"></script>
    <link href="res/css/rating.css" rel="stylesheet" type="text/css">
    
    <script type="text/javascript" src="res/js/notify.min.js"></script>
    
    <link href="res/css/folivora.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="res/js/folivora-utility.js"></script>
    <script type="text/javascript" src="res/js/folivora-signin.js"></script>
    <script type="text/javascript" src="res/js/folivora-signup.js"></script>
    <script type="text/javascript" src="res/js/folivora-webapp.js"></script>
    
    <script>
    <%
	// Do stuff when someone is signed in
	UserManager userManager = ApplicationManager.getApplicationManagerInstance().getuManager();
	User myUser = userManager.getUserWithSession(session);
	if(myUser != null) {
		%>
		webappDataObj.userData.id =  <% out.write("\"" + myUser.getId() + "\""); %>;
		webappDataObj.userData.balance = parseFloat(<% out.write("" + myUser.getCredit().getBalance()); %>).toFixed(2);
		
		$("#sr-toggle-btn").removeClass("hidden");
		$("#sr-function-btns").removeClass("hidden");
		<%
	}
	
	// Set actual user in attribute of request
	request.setAttribute("user", myUser);
	
	if(myUser == null) {
		if(!request.getRequestURL().toString().contains("webapp.jsp")
				&& !request.getRequestURL().toString().contains("index.jsp")
				&& !request.getRequestURL().toString().contains("signup.jsp")) {
			response.sendError(403, "Authentication required");
			return;
		}
	}
	%>
    </script>
  </head>