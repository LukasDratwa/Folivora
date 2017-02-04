<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="de.folivora.controller.UserManager"%>
<%@page import="de.folivora.controller.ApplicationManager"%>
<%@page import="de.folivora.model.SearchRequest"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="webappHead.jsp" %>
<%
User signedInUser = ((User) request.getAttribute("user"));
List<SearchRequest> searchRequests = ApplicationManager.getApplicationManagerInstance().getSearchRequestsCreatedByUser(signedInUser.getId().toString());

searchRequests.sort(new Comparator<SearchRequest>() {
	public int compare(SearchRequest o1, SearchRequest o2) {
		return o2.getCreationTimestamp().compareTo(o1.getCreationTimestamp());
	}
});

System.out.println(searchRequests);
%>

<body>
    <%@ include file="webappNavbar.jsp" %>
    <div class="container">
    	<div class="row">
    		<h2>Meine Gesuche</h2>
    		<%
    		int i = 0;
    		for (SearchRequest sr : searchRequests) {
    			boolean done = sr.getStatus() == SearchRequestStatus.STATISFIED && sr.getUserStasisfier() != null;
    			%>
    			<div class="search-request">
    				<small>
    					Am <% out.write(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, Locale.GERMANY).format(sr.getCreationTimestamp())); %>
    				</small>
	    			<h4>
	    				<span class="badge"><% out.write(new DecimalFormat("#.##").format(sr.getCostsAndReward()) + "&euro;"); %></span>
	    				<% out.write(sr.getTitle()); %>
	    				<span class="label label-<% out.write(sr.getStatus().getColorClassString()); %>"><% out.write(sr.getStatus().getTextInView()); %></span>
	    			</h4>
	    			<p><% out.write(sr.getDescription()); %></p>
	    			<%
	    			if (done) {
	    				User u = sr.getUserStasisfier();
	    				%>
	    				<p><em>Gesuch befriedigt durch <a href="user.jsp?id=<% out.write(u.getId().toString()); %>"><% out.write(u.getName()); %></a></em>.</p>
	    			<% } else { %>
		    			<p><strong>Lieferort: </strong> <% out.write(sr.getAddress()); %></p>
		    			<p>
		    				<strong>Lieferung bis spätestens: </strong>
		    				<%
		    				out.write(
		    	    				SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, Locale.GERMANY).format(new Date(sr.getPossibleDelivery_to()))
		    				);
		    				%>
		    			</p>
	    				<%
	    			}
	    			%>
	    			<div class="btn-group" role="group">
	    				<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-zoom-in"></span> Details</button>
	    				<% if (done) { %>
	    					<button type="button" class="btn btn-success"><span class="glyphicon glyphicon-star"></span> Feedback geben</button>
	    				<% } else { %>
	    					<button type="button" class="btn btn-primary"><span class="glyphicon glyphicon-pencil"></span>&nbsp;</button>
	    					<button type="button" class="btn btn-warning"><span class="glyphicon glyphicon-remove"></span>&nbsp;</button>
	    					<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span>&nbsp;</button>
	    				<% } %>
	    			</div>
	    			<% if (++i < searchRequests.size()) { %>
	    				<hr />
	    			<% } %>
    			</div>
    		<% } %>
    	</div>
    </div>
</body>
</html>